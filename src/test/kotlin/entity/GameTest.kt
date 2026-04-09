package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.*

class GameTest {
    private val player1 = Player(name = "Alex")
    private val player2 = Player(name = "Sam")
    private val game = Game(player1, player2, currentPlayer = 1)

    /**
     * Test for the Parameters [player1] and [player2]
     */
    @Test
    fun playerNameTest() {
        assertEquals("Alex", game.player1.name)
        assertEquals("Sam", game.player2.name)
    }

    /**
     * Tests that drawPile, centerCards and discardPile are initially empty.
     */
    @Test
    fun pilesAreEmptyAtStart() {
        assertTrue(game.drawPile.isEmpty(), "Draw pile should be empty at start")
        assertTrue(game.centerCards.isEmpty(), "Center cards should be empty at start")
        assertTrue(game.discardPile.isEmpty(), "Discard pile should be empty at start")
    }

    /**
     * Tests that firstAction is null at the beginning.
     */
    @Test
    fun firstActionNull() {
        assertEquals(null, game.firstAction, "firstAction should be null at start")
    }

    /**
     * Tests that currentPlayer is initialized correctly.
     */
    @Test
    fun currentPlayerTest() {
        assertEquals(1, game.currentPlayer)
    }

    /**
     * Tests initial round and maxRounds.
     */
    @Test
    fun roundInitialization() {
        assertEquals(1, game.currentRound)
        assertEquals(0, game.maxRounds)
    }

    /**
     * Tests that log is empty and no actions were taken.
     */
    @Test
    fun logAndActionsInitialization() {
        assertTrue(game.log.isEmpty(), "Log should be empty at start")
        assertEquals(0, game.actionsTaken)
    }
}