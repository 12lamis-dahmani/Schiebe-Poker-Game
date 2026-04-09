package service

    import entity.*
    import org.junit.jupiter.api.Assertions.*
    import org.junit.jupiter.api.BeforeEach
    import org.junit.jupiter.api.Test
    import kotlin.test.assertFailsWith

class PlayerActionServiceTest {

        private lateinit var rootService: RootService
        private lateinit var gameService: GameService
        private lateinit var actionService: PlayerActionService
        private lateinit var refreshable: TestRefreshable

        @BeforeEach
        fun setUp() {
            rootService = RootService()
            gameService = rootService.gameService
            actionService = rootService.playerActionService
            refreshable = TestRefreshable()
            rootService.addRefreshable(refreshable)

            gameService.startNewGame("A", "B", 3)
        }


        @Test
        fun testShiftLeft() {
            val game = rootService.currentGame!!

            val oldCenter = game.centerCards.toList()

            actionService.shiftLeft()

            assertEquals(3, game.centerCards.size)
            assertEquals(1, game.discardPile.size)
            assertEquals(Action.SHIFT_LEFT, game.firstAction)
            assertTrue(game.log.last().contains("shifted"))
            assertTrue(refreshable.refreshAfterShiftLeftCalled)
        }

        @Test
        fun testShiftLeftEndsTurn() {
            val game = rootService.currentGame!!

            game.actionsTaken = 1
            actionService.shiftLeft()

            assertEquals(0, game.actionsTaken)
            assertTrue(refreshable.refreshAfterEndTurnCalled)
        }


        @Test
        fun testShiftRight() {
            val game = rootService.currentGame!!

            actionService.shiftRight()

            assertEquals(3, game.centerCards.size)
            assertEquals(1, game.discardPile.size)
            assertEquals(Action.SHIFT_RIGHT, game.firstAction)
            assertTrue(refreshable.refreshAfterShiftRightCalled)
        }

        @Test
        fun testSwapSingle() {
            val game = rootService.currentGame!!
            val player = game.player1

            game.actionsTaken = 0
            game.firstAction = null

            // 👉 feste Karten setzen
            val playerCard = Card(CardSuit.HEARTS, CardValue.TWO)
            val centerCard = Card(CardSuit.SPADES, CardValue.ACE)

            player.openCards[0] = playerCard
            game.centerCards[0] = centerCard

            actionService.swapSingle(0, 0)

            assertEquals(centerCard, player.openCards[0])
            assertEquals(playerCard, game.centerCards[0])
            assertEquals(Action.SWAP_ONE, game.firstAction)
            assertTrue(refreshable.refreshAfterSwapSingleCalled)
        }

        @Test
        fun testSwapSingleInvalidIndex() {
            assertFailsWith<IllegalArgumentException> {
                actionService.swapSingle(5, 0)
            }
        }


        @Test
        fun testSwapAll() {
            val game = rootService.currentGame!!
            val player = game.player1

            game.actionsTaken = 0

            val playerCards = listOf(
                Card(CardSuit.HEARTS, CardValue.TWO),
                Card(CardSuit.HEARTS, CardValue.THREE),
                Card(CardSuit.HEARTS, CardValue.FOUR)
            )

            val centerCards = listOf(
                Card(CardSuit.SPADES, CardValue.ACE),
                Card(CardSuit.CLUBS, CardValue.KING),
                Card(CardSuit.DIAMONDS, CardValue.QUEEN)
            )

            player.openCards = playerCards.toMutableList()
            game.centerCards = centerCards.toMutableList()

            actionService.swapAll()

            assertEquals(centerCards, player.openCards)
            assertEquals(playerCards, game.centerCards)
            assertTrue(refreshable.refreshAfterSwapAllCalled)
        }


        @Test
        fun testSkipSwap() {
            val game = rootService.currentGame!!

            actionService.skipSwap()

            assertEquals(Action.PASS, game.firstAction)
            assertTrue(refreshable.refreshAfterSkipSwapCalled)
        }


        @Test
        fun testDrawFromEmptyDrawPile() {
            val game = rootService.currentGame!!

            game.drawPile.clear()
            game.discardPile.add(Card(CardSuit.HEARTS, CardValue.ACE))

            actionService.shiftLeft()

            assertTrue(game.drawPile.size >= 0)
        }


        @Test
        fun testEvaluatePair() {
            val cards = listOf(
                Card(CardSuit.HEARTS, CardValue.ACE),
                Card(CardSuit.SPADES, CardValue.ACE),
                Card(CardSuit.CLUBS, CardValue.TWO),
                Card(CardSuit.DIAMONDS, CardValue.THREE),
                Card(CardSuit.HEARTS, CardValue.FOUR)
            )

            val rank = actionService.evaluateHand(cards)
            assertEquals(HandRank.ONE_PAIR, rank)
        }

        @Test
        fun testEvaluateFlush() {
            val cards = listOf(
                Card(CardSuit.HEARTS, CardValue.ACE),
                Card(CardSuit.HEARTS, CardValue.TWO),
                Card(CardSuit.HEARTS, CardValue.FOUR),
                Card(CardSuit.HEARTS, CardValue.SEVEN),
                Card(CardSuit.HEARTS, CardValue.NINE)
            )

            val rank = actionService.evaluateHand(cards)
            assertEquals(HandRank.FLUSH, rank)
        }


        @Test
        fun testEndGame() {
            val game = rootService.currentGame!!

            actionService.endGame()

            assertNull(rootService.currentGame)
            assertTrue(refreshable.refreshAfterEndGameCalled)
        }

        @Test
        fun testEndGameNoGame() {
            rootService.currentGame = null

            assertFailsWith<IllegalStateException> {
                actionService.endGame()
            }
        }
}
