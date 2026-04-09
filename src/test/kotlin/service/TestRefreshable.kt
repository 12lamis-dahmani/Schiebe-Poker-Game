package service

import entity.Player

/**
 * [Refreshable] implementation for testing.
 * Tracks whether refresh methods were called.
 */
class TestRefreshable : Refreshable {

    var refreshAfterStartNewGameCalled = false
        private set

    var refreshAfterShiftLeftCalled = false
        private set

    var refreshAfterShiftRightCalled = false
        private set

    var refreshAfterSwapSingleCalled = false
        private set

    var refreshAfterSwapAllCalled = false
        private set

    var refreshAfterSkipSwapCalled = false
        private set

    var refreshAfterStartTurnCalled = false
        private set

    var refreshAfterEndTurnCalled = false
        private set

    var refreshAfterEndGameCalled = false
        private set

    var winnerFromGame: Player? = null
    var messageFromGame: String? = null

    var refreshAfterQuitGameCalled = false
        private set

    var refreshAfterRestartGameCalled = false
        private set

    /**
     * Resets all tracking flags.
     */
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterShiftLeftCalled = false
        refreshAfterShiftRightCalled = false
        refreshAfterSwapSingleCalled = false
        refreshAfterSwapAllCalled = false
        refreshAfterSkipSwapCalled = false
        refreshAfterStartTurnCalled = false
        refreshAfterEndTurnCalled = false
        refreshAfterEndGameCalled = false
        refreshAfterQuitGameCalled = false
        refreshAfterRestartGameCalled = false
        winnerFromGame = null
        messageFromGame = null
    }

    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterShiftLeft() {
        refreshAfterShiftLeftCalled = true
    }

    override fun refreshAfterShiftRight() {
        refreshAfterShiftRightCalled = true
    }

    override fun refreshAfterSwapSingle() {
        refreshAfterSwapSingleCalled = true
    }

    override fun refreshAfterSwapAll() {
        refreshAfterSwapAllCalled = true
    }

    override fun refreshAfterSkipSwap() {
        refreshAfterSkipSwapCalled = true
    }

    override fun refreshAfterStartTurn() {
        refreshAfterStartTurnCalled = true
    }

    override fun refreshAfterEndTurn() {
        refreshAfterEndTurnCalled = true
    }

    override fun refreshAfterEndGame(winner: Player?, message: String) {
        refreshAfterEndGameCalled = true
        winnerFromGame = winner
        messageFromGame = message
    }

    override fun refreshAfterQuitGame() {
        refreshAfterQuitGameCalled = true
    }

    override fun refreshAfterRestartGame() {
        refreshAfterRestartGameCalled = true
    }
}