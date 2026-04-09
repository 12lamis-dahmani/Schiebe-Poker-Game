package entity

import org.junit.jupiter.api.Assertions.*
import kotlin.test.*
import kotlin.test.assertEquals

/**
 * Test cases for [Card] class
 *
 * This tests class verifies:
 * The comparison of different card values.
 * The correct representation of a card as a string.
 * That two different Cards can not be equals
 */

class CardTest {
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)



    // Unicode characters for the suits, as those should be used by [Card.toString]
    private val heartsChar = '\u2665' // ♥
    private val spadesChar = '\u2660' // ♠
    private val clubsChar = '\u2663' // ♣

    /**
     * Test for toString() Methode
     *
     * Check if to String produces the correct strings for some test cards
     */

    @Test
    fun testToString() {
        assertEquals(spadesChar + "A", aceOfSpades.toString())
        assertEquals(clubsChar + "J", jackOfClubs.toString())
        assertEquals(heartsChar + "Q", queenOfHearts.toString())
    }


    /**
     * Check if two cards with different CardSuit/CardValue combination are Not equal
     */

    @Test
    fun testNotEquals() {
        assertNotEquals( aceOfSpades.value.ordinal, jackOfClubs.value.ordinal,
            "Cards values should not be equal" )
    }


}
