package service
import entity.*
import kotlin.random.Random

/**
 * Service layer class that provides the logic for actions not directly
 * related to a single player.
 *
 * @param rootService The [RootService] instance to access the other service methods and entity layer
 */

class GameService (private val rootService: RootService): AbstractRefreshingService() {

    /**
     * Starts a new game with two players, creates and shuffles a full deck,
     * distributes the cards, selects a random starting player,
     * and initializes the game state.
     *
     * @param p1Name the name of Player 1 (must be valid: not empty and within allowed length)
     * @param p2Name the name of Player 2 (must be valid: not empty and within allowed length)
     * @param maxRound the total number of rounds for the game (must be between 2 and 7)
     *
     * @throws IllegalArgumentException if:
     * - a game is already running
     * - a player name is invalid
     * - the number of rounds is outside the allowed range
     */

    fun startNewGame(p1Name: String, p2Name: String, maxRound: Int) {


        require(isValidName(p1Name)) { "Player 1 name is invalid." }
        require(isValidName(p2Name)) { "Player 2 name is invalid." }
        require(isValidMaxRounds(maxRound)) { "Invalid number of rounds (must be between 2 and 7)." }


        val player1 = Player(p1Name)
        val player2 = Player(p2Name)

        // Create and shuffle full deck
        val fullDeck = generateFullDeck().shuffled().toMutableList()

        // Choose startingPlayer 1 or 2 randomly
        val starter = Random.nextInt(1, 3)

        val game = Game(player1, player2, starter)
        game.maxRounds = maxRound
        game.startingPlayer = game.currentPlayer

        val currentPlayerName =
            if (game.currentPlayer == 1) player1.name else player2.name

        game.log.add("New Game with ${game.maxRounds} rounds started: $currentPlayerName starts the game.")
        game.log.add("Round 1 starts !")

        // Distribute cards
        distributeCards(fullDeck, game)
        rootService.currentGame = game

        // Refresh
        onAllRefreshables { refreshAfterStartNewGame()
        }
    }

    /**
     * Starts the current player turn, allowing them to perform actions
     *
     * @throws IllegalStateException if no game is active.
     * @throws IllegalArgumentException if the current player index is invalid
     *
     */
    fun startTurn() {

        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        // To make sure that currentPlayer is Player 1 or 2
        require(game.currentPlayer == 1 || game.currentPlayer == 2)
        { "There is no active player: $game expected 1 or 2" }

        val currentPlayerName =
            if (game.currentPlayer == 1) game.player1.name else game.player2.name

        game.log.add(" $currentPlayerName is playing her/his turn in Round ${game.currentRound}.")


        onAllRefreshables { refreshAfterStartTurn() }
    }

    /**
     * Ends the current player's turn and switches to the next player.
     *
     * @throws IllegalStateException If no game is active or the game has already ended.
     * @throws IllegalArgumentException If the current player index is invalid.
     */

    fun endTurn() {

        val game = rootService.currentGame
        // check the game
        checkNotNull(game) { "No game is currently active" }
        require(game.currentPlayer == 1 || game.currentPlayer == 2) {
            "There is no active player: $game expected 1 or 2"
        }

        // normal actions of end turn
        game.firstAction = null // reset firstAction for a new turn
        game.actionsTaken = 0 // reset actionsTaken

        // switch player
        game.currentPlayer = if (game.currentPlayer == 1) 2 else 1


        if (game.currentPlayer == game.startingPlayer) {
            game.currentRound += 1
            game.log.add("Round ${game.currentRound} starts !")

            if (game.currentRound > game.maxRounds) {
                rootService.playerActionService.endGame()
                return
            }
        }

        val nextPlayerName =
            if (game.currentPlayer == 1) game.player1.name else game.player2.name

        game.log.add("Next turn: $nextPlayerName")

        onAllRefreshables { refreshAfterEndTurn() }
    }

    /**
     * Validates a player's name: must be non-empty and ≤ 16 characters
     *
     * @param playerName The name to validate.
     * @return true if the name is valid
     */

    private fun isValidName(playerName: String): Boolean {
        return playerName.isNotEmpty() && playerName.length <= 16
    }

    private fun isValidMaxRounds(maxRounds: Int): Boolean {
        return maxRounds in 2..7
    }

    /**
     * Generates a full deck of 52 playing cards
     * all combinations of [CardSuit] and [CardValue]
     *
     * @return A complete list of [Card] objects.
     */

    private fun generateFullDeck(): List<Card> {
        // Create all possible Cards, for every value create four suit (52 Cards)
        return CardValue.entries.flatMap { value ->
            CardSuit.entries.map { suit ->
                Card(suit, value)
            }
        }
    }

    /**
     * Distributes cards from the full deck to both players and initializes the exchange area
     * and draw pile in the given game instance.
     *
     * @param fullDeck The complete and already shuffled deck of cards.
     * @param game The game instance whose players, exchange area, and draw pile will be updated.
     */

    private fun distributeCards(fullDeck: List<Card>, game: Game) {

        game.player1.hiddenCards = fullDeck.take(2).toMutableList()
        game.player1.openCards = fullDeck.drop(2).take(3).toMutableList()

        game.player2.hiddenCards = fullDeck.drop(5).take(2).toMutableList()
        game.player2.openCards = fullDeck.drop(7).take(3).toMutableList()

        game.centerCards = fullDeck.drop(10).take(3).toMutableList()
        game.drawPile = fullDeck.drop(13).toMutableList()
    }

    /**
     * Checks if the game has ended (both players have no cards left
     * and the exchange area is empty)
     *
     * @return true if the game has ended.
     * @throws IllegalStateException If no game is active.
     */

    // check whenever the game is already ended
    fun isGameEnded(): Boolean {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        return game.currentRound > game.maxRounds
    }

    /**
     * Exits Game application
     * */

    fun quitGame() {
        onAllRefreshables { refreshAfterQuitGame() }
    }
    /**
     *  returns to main menu after clicking restart game
     * */
    fun restartGame() {
        onAllRefreshables { refreshAfterRestartGame() }
    }


}