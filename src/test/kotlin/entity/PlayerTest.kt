package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlayerTest {

    private val player = Player(name = "Alex")

    /**
     * verifies that the score of a new player is 0.
     */
    @Test
    fun defaultScore0() {
        assertEquals(0, player.score, "Score should be 0 at start")
    }

    /**
     * verifies that the player's name is correct.
     */
    @Test
    fun testName() {
        assertEquals("Alex", player.name)
    }

    /**
     * Tests that openCards and hiddenCards are empty at start.
     */
    @Test
    fun initialCardsEmpty() {
        assertTrue(player.openCards.isEmpty(), "Open cards should be empty")
        assertTrue(player.hiddenCards.isEmpty(), "Hidden cards should be empty")
    }

    /**
     * Test for toString() method
     */
    @Test
    fun testToStringPlayer() {
        val expected = "Alex: Score=0 Open=0 Hidden=0"
        assertEquals(expected, player.toString())
    }
}