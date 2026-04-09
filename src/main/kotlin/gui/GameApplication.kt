package gui

import tools.aqua.bgw.core.BoardGameApplication
import service.Refreshable
import service.RootService
import entity.Player


/**
 * Implementation of the BGW [BoardGameApplication]
 */

class GameApplication : BoardGameApplication("Schiebe-Poker"),Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes

    // This is where the actual game takes place
    private val gameScene = GameScene(rootService)

    // This menu scene is shown after application start and if the "new game" button
    // is clicked in the ResultMenuScene
    private val newGameMenuScene = NewGameMenuScene(rootService)

    // initialize game
    init {
        rootService.addRefreshables(
            this,
            gameScene,
            newGameMenuScene,
            )

        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer
        rootService.gameService.startNewGame("Bob", "Alice", maxRound = 2)

        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene, 0)

    }

    /**
     * Called after a new game has started.
     * Shows the change player screen for the first turn.
     */

    override fun refreshAfterStartNewGame() {
        val changePlayerScene = ChangePlayerScene(rootService)
        this.showMenuScene(changePlayerScene)
    }

    /**
     * Called after a game ends.
     * Shows the result screen with the winner and message.
     */

    override fun refreshAfterEndGame(winner : Player?, message: String) {
        val resultMenuScene = ResultMenuScene(rootService, message)
        this.showMenuScene(resultMenuScene)
    }

    /**
     * Called at the start of a player's turn.
     */

    override fun refreshAfterStartTurn() {
        this.hideMenuScene()
    }

    /**
     * Called at the end of a player's turn.
     * Shows a screen prompting the next player to get ready.
     */

    override fun refreshAfterEndTurn() {
        val changePlayerScene = ChangePlayerScene(rootService)
        this.showMenuScene(changePlayerScene)
    }

    /**
     * Called when a game is quit.
     * Exits the application.
     */

    override fun refreshAfterQuitGame() {
            this.exit()
        }


    /**
     * Called when the game is restarted.
     * Returns to the new game setup menu.
     */

    override fun refreshAfterRestartGame() {
            this.showMenuScene(newGameMenuScene)
    }

}