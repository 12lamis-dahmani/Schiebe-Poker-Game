package service
import entity.*

/**
* Service layer class that provides the logic for the possible actions a player can take in the game
*
* @param rootService The [RootService] instance to access the other service methods and entity layer
*/

class PlayerActionService (private val rootService: RootService) : AbstractRefreshingService() {


    /**
     * Executes the "shift left" action for the current player.
     *
     * Game logic:
     * - The three center cards are shifted one position to the left.
     * - The leftmost card (index 0) is removed from the center and placed onto the discard pile.
     * - The remaining two cards automatically move one position to the left.
     * - A new card is drawn from the draw pile and inserted at the rightmost position of the center.
     *
     * Turn logic:
     * - This action counts as one of the two actions per turn.
     * - If this is the first action of the turn, it is stored in [Game.firstAction].
     * - After two actions are performed, the turn automatically ends.
     *
     * Logging:
     * - The action is recorded in the game log in the format:
     *   "<PlayerName> shifted the cards to the left."
     *
     * UI updates:
     * - Triggers a refresh via [refreshAfterShiftLeft] to update the UI.
     *
     * Preconditions:
     * - A game must be active ([RootService.currentGame] must not be null).
     *
     * @throws IllegalStateException if no game is currently active.
     */

    fun shiftLeft() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val leftCard = game.centerCards.removeAt(0)
        game.discardPile.add(leftCard)
        val newCard = drawCard(game)
        game.centerCards.add(newCard)


        game.actionsTaken ++

        if (game.actionsTaken == 1) {
            game.firstAction = Action.SHIFT_LEFT
        }

        val playerName = getCurrentPlayerName(game)
        game.log.add("Action ${game.actionsTaken}: $playerName shifted the cards to the left.")

        onAllRefreshables { refreshAfterShiftLeft() }

        if (game.actionsTaken == 2) {
            rootService.gameService.endTurn()
        }

    }

    /**
     * Executes the "shift right" action.
     *
     * - Removes the rightmost center card and places it on the discard pile.
     * - Shifts remaining center cards to the right.
     * - Draws a new card and inserts it at the leftmost position.
     *
     * Counts as one action, logs the move, updates the UI,
     * and ends the turn after two actions.
     *
     * @throws IllegalStateException if no game is active.
     */
    fun shiftRight() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val rightCard = game.centerCards.removeAt(2)
        game.discardPile.add(rightCard)
        val newCard = drawCard(game)
        game.centerCards.add(0, newCard)


        game.actionsTaken++
        if (game.actionsTaken == 1) { game.firstAction = Action.SHIFT_RIGHT}

        val playerName = getCurrentPlayerName(game)
        game.log.add("Action ${game.actionsTaken}: $playerName shifted the cards to the right.")

        onAllRefreshables { refreshAfterShiftRight() }

        if (game.actionsTaken == 2) {
            rootService.gameService.endTurn()
        }

    }

    /**
     * Swaps one open card of the current player with a center card.
     *
     * - The selected player card and center card are exchanged by index.
     * - Counts as one action and is logged.
     * - Updates the UI and ends the turn after two actions.
     *
     * @param playerCardIndex Index of the player's open card (0–2).
     * @param centerCardIndex Index of the center card (0–2).
     *
     * @throws IllegalStateException if no game is active.
     * @throws IllegalArgumentException if indices are out of bounds.
     */

    /*
    fun swapSingle(playerCardIndex: Int, centerCardIndex: Int) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val player = getCurrentPlayer(game)

        require(playerCardIndex in 0..2)
        require(centerCardIndex in 0..2)

        val playerCard = player.openCards[playerCardIndex]
        val centerCard = game.centerCards[centerCardIndex]

        // Swap
        player.openCards[playerCardIndex] = centerCard
        game.centerCards[centerCardIndex] = playerCard

        game.actionsTaken++

        if (game.actionsTaken == 1) {
            game.firstAction = Action.SWAP_ONE
        }

        game.log.add("${player.name} swapped a card.")

        onAllRefreshables { refreshAfterSwapSingle() }

        if (game.actionsTaken == 2) {
            rootService.gameService.endTurn()
        }
    }
    */

    fun swapSingle(playerCard: Card, centerCard: Card) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val player = getCurrentPlayer(game)

        val playerIndex = player.openCards.indexOf(playerCard)
        val centerIndex = game.centerCards.indexOf(centerCard)

        require(playerIndex in 0..2){"Card not found! Please select cards from your Hands "}
        require(centerIndex in 0..2) {"Card not found! Please select cards from your Hands"}

        // Swap
        player.openCards[playerIndex] = centerCard
        game.centerCards[centerIndex] = playerCard

        game.actionsTaken++

        if (game.actionsTaken == 1) {
            game.firstAction = Action.SWAP_ONE
        }

        game.log.add("Action ${game.actionsTaken}: ${player.name} swapped a card.")

        onAllRefreshables { refreshAfterSwapSingle() }

        if (game.actionsTaken == 2) {
            rootService.gameService.endTurn()
        }
    }


    /**
     * Swaps all three open cards of the current player with the center cards.
     *
     * - Each card is exchanged position-wise (left, middle, right).
     * - Counts as one action and is logged.
     * - Updates the UI and ends the turn after two actions.
     *
     * @throws IllegalStateException if no game is active.
     */
    fun swapAll(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val player = getCurrentPlayer(game)
        val first = player.openCards[0]
        val second = player.openCards[1]
        val third = player.openCards[2]

        val firstCenter = game.centerCards[0]
        val secondCenter = game.centerCards[1]
        val thirdCenter = game.centerCards[2]

        player.openCards[0] = firstCenter
        player.openCards[1] = secondCenter
        player.openCards[2] = thirdCenter

        game.centerCards[0] = first
        game.centerCards[1] = second
        game.centerCards[2] = third

        game.actionsTaken++

        if (game.actionsTaken == 1) {
            game.firstAction = Action.SWAP_ALL
        }

        game.log.add("Action ${game.actionsTaken}: ${player.name} swapped all three open cards with the center cards.")

        onAllRefreshables { refreshAfterSwapAll() }

        if (game.actionsTaken == 2) {
            rootService.gameService.endTurn()
        }
    }

    /**
     * Skips the swap action (pass).
     *
     * - Counts as one action without changing any cards.
     * - Logged as a pass action.
     * - Updates the UI and ends the turn after two actions.
     *
     * @throws IllegalStateException if no game is active.
     */
    fun skipSwap() {
        val game = rootService.currentGame
        checkNotNull(game)

        val player = getCurrentPlayer(game)

        game.actionsTaken++

        if (game.actionsTaken == 1) {
            game.firstAction = Action.PASS
        }

        game.log.add("Action ${game.actionsTaken}: ${player.name} skipped swapping.")

        onAllRefreshables { refreshAfterSkipSwap() }

        if (game.actionsTaken == 2) {
            rootService.gameService.endTurn()
        }
    }

    /**
     * Evaluates a 5-card hand and returns its poker rank.
     *
     * - Detects standard poker combinations (e.g., Pair, Straight, Flush).
     * - Supports special case: Ace-low straight (A-2-3-4-5).
     *
     * @param cards List of exactly 5 cards.
     * @return The corresponding [HandRank].
     */
    fun evaluateHand(cards: List<Card>): HandRank {
        val counts = cards.groupingBy { it.value }.eachCount()
        val isFlush = cards.map { it.suit }.distinct().size == 1
        val values = cards.map { it.value.ordinal }.sorted()

        val isStraight = values.zipWithNext().all { it.second == it.first + 1 } ||
                values == listOf(0, 1, 2, 3, 12)

        val isRoyal = values.containsAll(
            listOf(
                CardValue.TEN.ordinal,
                CardValue.JACK.ordinal,
                CardValue.QUEEN.ordinal,
                CardValue.KING.ordinal,
                CardValue.ACE.ordinal
            )
        )

        return when {
            isStraight && isFlush && isRoyal -> HandRank.ROYAL_FLUSH
            isStraight && isFlush -> HandRank.STRAIGHT_FLUSH
            counts.containsValue(4) -> HandRank.FOUR_OF_A_KIND
            counts.containsValue(3) && counts.containsValue(2) -> HandRank.FULL_HOUSE
            isFlush -> HandRank.FLUSH
            isStraight -> HandRank.STRAIGHT
            counts.containsValue(3) -> HandRank.THREE_OF_A_KIND
            counts.values.count { it == 2 } == 2 -> HandRank.TWO_PAIR
            counts.containsValue(2) -> HandRank.ONE_PAIR
            else -> HandRank.HIGH_CARD
        }
    }


    /**
     * Ends the current game, determines the winner, and notifies the UI.
     *
     * @throws IllegalStateException If no game is active or the game has already ended.
     * @throws IllegalArgumentException If the current player index is invalid.
     */

    fun endGame() {

        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val p1 = game.player1
        val p2 = game.player2

        val p1Cards = p1.hiddenCards + p1.openCards
        val p2Cards = p2.hiddenCards + p2.openCards

        val p1Rank = evaluateHand(p1Cards)
        val p2Rank = evaluateHand(p2Cards)

        when {
            p1Rank > p2Rank -> {
                onAllRefreshables {
                    refreshAfterEndGame(p1, "Congrats ${p1.name}! You win the game!")
                }
            }
            p2Rank > p1Rank -> {
                onAllRefreshables {
                    refreshAfterEndGame(p2, "Congrats ${p2.name}! You win the game!")
                }
            }
            else -> {
                onAllRefreshables {
                    refreshAfterEndGame(null, "Game ended with a Draw!")
                }
            }
        }

        rootService.currentGame = null
    }


    /**
     * Returns the name of the current player based on [Game.currentPlayer].
     *
     * @param game The current game instance.
     * @return The name of the active player.
     */
    private fun getCurrentPlayerName(game: Game): String {
        return if (game.currentPlayer == 1) game.player1.name else game.player2.name
    }


    /**
     * Draws a card from the draw pile.
     *
     * - If the draw pile is empty, the discard pile is shuffled and reused.
     * - The top card of the draw pile is then removed and returned.
     *
     * @param game The current game instance.
     * @return The drawn card.
     *
     * @throws IllegalArgumentException if no cards are available to draw.
     */
    private fun drawCard(game: Game): Card {
        if (game.drawPile.isEmpty()) {

            require(game.discardPile.isNotEmpty()) {
                "No cards left to draw."
            }

            game.drawPile = game.discardPile.shuffled().toMutableList()
            game.discardPile.clear()
        }

        return game.drawPile.removeAt(0)
    }


    private fun getCurrentPlayer(game: Game): Player {
        return when (game.currentPlayer) {
            1 -> game.player1
            2 -> game.player2
            else -> throw IllegalStateException("Invalid currentPlayer")
        }
    }

}

