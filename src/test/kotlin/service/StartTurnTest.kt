package service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

/**
 * Unit tests for the [GameService.startTurn] method.
 */
class StartTurnTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var refreshable: TestRefreshable

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = rootService.gameService
        refreshable = TestRefreshable()
        rootService.addRefreshable(refreshable)
    }

    /**
     * Should throw if no game is active.
     */
    @Test
    fun testNoActiveGame() {
        val exception = assertFailsWith<IllegalStateException> {
            gameService.startTurn()
        }

        assertEquals("No game is currently active", exception.message)
        assertFalse(refreshable.refreshAfterStartTurnCalled)
    }

    /**
     * Should throw if current player is invalid.
     */
    @Test
    fun testInvalidCurrentPlayer() {
        gameService.startNewGame("Alex", "Bob", 3)
        val game = rootService.currentGame!!

        game.currentPlayer = 10

        val exception = assertFailsWith<IllegalArgumentException> {
            gameService.startTurn()
        }

        assertTrue(exception.message!!.contains("There is no active player"))
        assertFalse(refreshable.refreshAfterStartTurnCalled)
    }

    /**
     * Should successfully start a turn.
     */
    @Test
    fun testStartTurn() {
        gameService.startNewGame("Alex", "Bob", 3)

        gameService.startTurn()

        assertTrue(refreshable.refreshAfterStartTurnCalled)
    }
}