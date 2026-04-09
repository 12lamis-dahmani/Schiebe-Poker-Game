package service
import entity.Card
import entity.Player

/**
 * Interface that allows service layer classes to notify UI components
 * about changes in the game state.
 *
 * Default (empty) implementations are provided, so UI classes only need
 * to override methods relevant to them.
 *
 * @see AbstractRefreshingService
 */
interface Refreshable {

    /**
     * Called after a new game has been started.
     */
    fun refreshAfterStartNewGame() {}

    /**
     * Called after a shift-left action was performed.
     * Updates the center cards and discard/draw piles.
     */
    fun refreshAfterShiftLeft() {}

    /**
     * Called after a shift-right action was performed.
     * Updates the center cards and discard/draw piles.
     */
    fun refreshAfterShiftRight() {}

    /**
     * Called after a single card swap between player and center.
     * Updates the player's open cards and center cards.
     */
    fun refreshAfterSwapSingle() {}

    /**
     * Called after all three open cards were swapped with the center.
     * Updates the player's open cards and center cards.
     */
    fun refreshAfterSwapAll() {}

    /**
     * Called after a player skips the swap action (pass).
     * No card changes, but UI/log may update.
     */
    fun refreshAfterSkipSwap() {}

    /**
     * Called after a new turn has started.
     */
    fun refreshAfterStartTurn() {}

    /**
     * Called after a turn has ended.
     */
    fun refreshAfterEndTurn() {}

    /**
     * Called after the game has ended.
     *
     * @param winner The player who won the game, or null if it is a draw.
     * @param message Result message to display.
     */
    fun refreshAfterEndGame(winner: Player?, message: String) {}

    /**
     * Called when the game is closed.
     */
    fun refreshAfterQuitGame() {}

    /**
     * Called after restarting the game (return to main menu).
     */
    fun refreshAfterRestartGame() {}
}