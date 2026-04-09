package service

import entity.Action
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

/**
 * Unit tests for the [GameService.endTurn] method.
 */
class EndTurnTest {

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
            gameService.endTurn()
        }

        assertEquals("No game is currently active", exception.message)
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
            gameService.endTurn()
        }

        assertTrue(exception.message!!.contains("There is no active player"))
        assertFalse(refreshable.refreshAfterEndTurnCalled)
    }

    /**
     * Should correctly switch player and reset turn state.
     */
    @Test
    fun testEndTurn() {
        gameService.startNewGame("Alex", "Bob", 3)
        val game = rootService.currentGame!!

        game.currentPlayer = 1
        game.firstAction = Action.SHIFT_LEFT
        game.actionsTaken = 2

        gameService.endTurn()

        // Player switched
        assertEquals(2, game.currentPlayer)

        // Reset turn state
        assertNull(game.firstAction)
        assertEquals(0, game.actionsTaken)

        // Refresh called
        assertTrue(refreshable.refreshAfterEndTurnCalled)
    }
}