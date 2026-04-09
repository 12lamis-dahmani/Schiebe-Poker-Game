package service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

/**
 * Unit tests for the [GameService.startNewGame] method.
 * correct assignment of player names.
 * Correct distribution of hand and draw cards.
 */
class StartNewGameTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var refreshable: TestRefreshable

    /**
     * Sets up a fresh game environment before each test.
     */

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = rootService.gameService
        refreshable = TestRefreshable()
        rootService.addRefreshable(refreshable)
    }

    @Test
    fun testStartNewGame() {

        val name1 = "Alex"
        val name2 = "Bob"
        val rounds = 3

        gameService.startNewGame(name1, name2, rounds)
        val game = rootService.currentGame

        assertNotNull(game, "Game must not be null")

        // Player names
        assertEquals(name1, game!!.player1.name)
        assertEquals(name2, game.player2.name)

        // Cards distribution
        assertEquals(2, game.player1.hiddenCards.size)
        assertEquals(3, game.player1.openCards.size)

        assertEquals(2, game.player2.hiddenCards.size)
        assertEquals(3, game.player2.openCards.size)

        assertEquals(3, game.centerCards.size)

        // Draw pile size: 52 - 13 = 39
        assertEquals(39, game.drawPile.size)

        // Game state
        assertTrue(game.currentPlayer in listOf(1, 2))
        assertEquals(rounds, game.maxRounds)
        assertEquals(1, game.currentRound)

        // Log
        assertTrue(game.log.isNotEmpty())

        // Refresh called
        assertTrue(refreshable.refreshAfterStartNewGameCalled)

        refreshable.reset()
    }

}
