package gui

import service.Refreshable
import entity.*
import service.RootService
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.ScaleAnimation
import tools.aqua.bgw.components.ComponentView
import DiscardView


/**
 * This is the main scene for the card game UI. It represents the complete game board where
 * all player actions, hands, the draw stack, and the exchange area are visualized and managed.
 *
 * Player 1 is positioned in the upper half, Player 2 in the lower half of the screen.
 * Each player has their own hand area and score label. In addition, there is a shared
 * draw pile and exchange area in the center.
 *
 *communicate with the [RootService] and its logic layer
 *
 * This class also implements [Refreshable] to react to state changes in the game.
 *
 * @param rootService The [RootService] instance that provides access to game logic services
 */

class GameScene( private val rootService: RootService) : BoardGameScene(1920, 1080),  Refreshable {

    /**
     * Selected hand and exchange cards for swap/play actions
     */
    private var selectedHandCard: Card? = null
    private var selectedExchangeCard: Card? = null
    private var selectedExchangeIndex: Int? = null

    /**
     * References to clicked cards for animation reset
     */
    private var clickedHandCard: CardView? = null
    private var clickedExchangeCard: CardView? = null

    // set Buttons ------------------------------------------------------------------------------------

    /**
     * Button to draw a card from the draw stack.
     * Resets selection and animation states before drawing.
     */
    private val shiftLeftButton = Button(
       //  posX = 1550,
        // posY = 620,
        posX = 700,
        posY = 620,
        width = 150,
        height = 70,
        text = "Shift Left",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {

// set colors
        val normalVisual = ColorVisual(50, 193, 93).apply { style.borderRadius = BorderRadius.LARGE }
        val hoverVisual = ColorVisual(51, 94, 28).apply { style.borderRadius = BorderRadius.LARGE }
        visual = normalVisual
        onMouseEntered = { visual = hoverVisual }
        onMouseExited = { visual = normalVisual }
        onMouseClicked = {

            //Reset animations right after the click -----------------------------
            clickedHandCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedExchangeCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedHandCard = null
            clickedExchangeCard = null

            //------------------------------------------

            // reset selected Cards
            selectedHandCard = null
            selectedExchangeCard = null
            selectedExchangeIndex = null

            //------------------------------------

            // reset hover for Buttons
            visual = normalVisual

            rootService.playerActionService.shiftLeft()
        }
    }


    /**
     * Button to swap a card between hand and exchange area.
     * Requires both a hand and exchange card to be selected.
     */

    private val shiftRightButton = Button(
        //posX = 800,
        // posY = 620,
        posX = 920,
        posY = 620,
        width = 150,
        height = 70,
        text = "Shift Right",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {

        val normalVisual = ColorVisual(50, 193, 93).apply { style.borderRadius = BorderRadius.LARGE }
        val hoverVisual = ColorVisual(51, 94, 28).apply { style.borderRadius = BorderRadius.LARGE }
        visual = normalVisual
        onMouseEntered = { visual = hoverVisual }
        onMouseExited = { visual = normalVisual }
        onMouseClicked = {

            //Reset animations right after the click -----------------------------
            clickedHandCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedExchangeCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedHandCard = null
            clickedExchangeCard = null

            //------------------------------------------

            // reset selected Cards
            selectedHandCard = null
            selectedExchangeCard = null
            selectedExchangeIndex = null

            //------------------------------------

            // reset hover for Buttons
            visual = normalVisual

            rootService.playerActionService.shiftRight()
        }
    }


    private val swapOneButton = Button(
       // posX = 70,
       // posY = 450,
        posX = 30,
        posY = 450,
        width = 190,
        height = 70,
        text = "Swap One",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {

        val normalVisual = ColorVisual(50, 193, 93).apply { style.borderRadius = BorderRadius.LARGE }
        val hoverVisual = ColorVisual(51, 94, 28).apply { style.borderRadius = BorderRadius.LARGE }
        visual = normalVisual
        onMouseEntered = { visual = hoverVisual }
        onMouseExited = { visual = normalVisual }

        onMouseClicked = {

            //---------------------------------------
            // Reset the Animation
            clickedHandCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedExchangeCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedHandCard = null
            clickedExchangeCard = null
            //-------------------------------------------

            // reset hover for Buttons
            visual = normalVisual


            // if both cards not selected throw illegalException and Reset
            if (selectedHandCard == null || selectedExchangeCard == null) {

                selectedExchangeCard = null
                selectedExchangeIndex = null
                selectedHandCard = null

                throw IllegalArgumentException("Please select both cards before Swap!")
            } else {

                selectedHandCard?.let { handCard ->
                    selectedExchangeCard?.let { exchangeCard ->
                        rootService.playerActionService.swapSingle(handCard, exchangeCard)

                        selectedHandCard = null
                        selectedExchangeCard = null
                        selectedExchangeIndex = null
                    }
                }

            }
        }
    }


    private val swapAllButton = Button(
        //posX = 70,
        //posY = 550,
        posX = 30,
        posY = 540,
        width = 190,
        height = 70,
        text = "Swap All",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {

        val normalVisual = ColorVisual(50, 193, 93).apply { style.borderRadius = BorderRadius.LARGE }
        val hoverVisual = ColorVisual(51, 94, 28).apply { style.borderRadius = BorderRadius.LARGE }

        visual = normalVisual

        onMouseEntered = { visual = hoverVisual }
        onMouseExited = { visual = normalVisual }

        onMouseClicked = {

            //---------------------------------------
            // Reset Animation
            clickedHandCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedExchangeCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedHandCard = null
            clickedExchangeCard = null
            //---------------------------------------

            visual = normalVisual

            // 👉 DIREKT SwapAll (keine Auswahl nötig)
            rootService.playerActionService.swapAll()

            // Reset Auswahl
            selectedHandCard = null
            selectedExchangeCard = null
            selectedExchangeIndex = null
        }
    }

    /**
     * Button to Pass
     */
    private val passButton = Button(
        posX = 1740,
        posY = 990,
        width = 150,
        height = 70,
        text = "Pass",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {

        val normalVisual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE }
        val hoverVisual = ColorVisual(178, 34, 34).apply { style.borderRadius = BorderRadius.LARGE }

        visual = normalVisual

        onMouseEntered = { visual = hoverVisual }
        onMouseExited = { visual = normalVisual }

        onMouseClicked = {

            //---------------------------------------
            clickedHandCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedExchangeCard?.let {
                resetCardPosition(it)
                resetCardSize(it)
            }
            clickedHandCard = null
            clickedExchangeCard = null
            //---------------------------------------

            visual = normalVisual

            // Aktion
            rootService.playerActionService.skipSwap()

            // Auswahl zurücksetzen
            selectedHandCard = null
            selectedExchangeCard = null
            selectedExchangeIndex = null
        }
    }

    // labels -----------


    // NameLabels
    private val playerName1 = Label(
        posX = 800,
        posY = 20,
        width = 170,
        height = 50,
        alignment = Alignment.CENTER
    ).apply {
        visual = ColorVisual(240, 200, 8).apply { style.borderRadius = BorderRadius.SMALL }
    }

    val playerName2 = Label(
        posX = 800,
        posY = 1010,
        width = 170,
        height = 50,
        alignment = Alignment.CENTER
    ).apply {
        visual = ColorVisual(255, 215, 0).apply { style.borderRadius = BorderRadius.SMALL }
    }

    // Score Labels
    private val player1ScoreLabel = Label(
        posX = 70,
        posY = 20,
        width = 190,
        height = 70,
        text = "Score: 0",
        alignment = Alignment.CENTER
    ).apply {
        font = Font(size = 30, fontWeight = Font.FontWeight.BOLD)
        visual = ColorVisual(130, 130, 130).apply { style.borderRadius = BorderRadius.SMALL }
    }


    private val player2ScoreLabel = Label(
        posX = 70,
        posY = 990,
        width = 190,
        height = 70,
        text = "Score: 0",
        alignment = Alignment.CENTER
    ).apply {
        font = Font(size = 30, fontWeight = Font.FontWeight.BOLD)
        visual = ColorVisual(130, 130, 130).apply { style.borderRadius = BorderRadius.SMALL }

    }
    // BackViews ---------------------

    private val player1Hand = HandView(posX = 555, posY = 100)
    private val player2Hand = HandView(posX = 555, posY = 780)

    private val exchangeArea = ExchangeView(posX = 725, posY = 410)

    private val drawStackView = LabeledStackView(
        //posX = 1560,
        //posY = 410,
        posX = 1100,
        posY = 410,
        label = "Draw Stack",
        rotate = false
    ).apply {
        visual = ColorVisual(11, 94, 28)
    }

    private val discardView = DiscardView(
        //posX = 1700, posY = 410
        posX = 550,
        posY = 400
    ).apply {         visual = ColorVisual(200, 200, 200, 200).apply { style.borderRadius = BorderRadius.SMALL }
    }


    private val logLabel = Label(
        posX = 1500,
        posY = 100,
        width = 400,
        height = 850,
        text = "\n",
        alignment = Alignment.TOP_LEFT
    ).apply {
        font = Font(size = 18, fontWeight = Font.FontWeight.NORMAL)
        isWrapText = true
        visual = ColorVisual(200, 200, 200, 200).apply { style.borderRadius = BorderRadius.SMALL }
    }

    private val LoggerLabel = Label(
        posX = 1600,
        posY = 20,
        width = 190,
        height = 70,
        text = "Game Log",
        alignment = Alignment.CENTER
    ).apply {
        font = Font(size = 30, fontWeight = Font.FontWeight.BOLD)
        visual = ColorVisual(130, 130, 130).apply { style.borderRadius = BorderRadius.SMALL }
    }


    // set the Map ------------------------------------------------------------------------------------------


    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */

    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {
        background = ImageVisual("Poker.jpeg")

        addComponents(
            shiftLeftButton,
            shiftRightButton,
            swapAllButton,
            swapOneButton,
            passButton,
            playerName1,
            playerName2,
            player1Hand,
            player2Hand,
            drawStackView,
            exchangeArea,
            discardView,
            player1ScoreLabel,
            player2ScoreLabel,
            logLabel,
            LoggerLabel
        )
    }

    // refreshAfter.. ----------------------------------------------------------------------------------------

    /**
     * Refreshes the entire game scene after a new game has been started.
     *
     **/


    override fun refreshAfterStartNewGame() {
        // Get the current game instance and ensure it is not null
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        // Clear the card mapping
        cardMap.clear()

        val cardImageLoader = CardImageLoader()

        // Initialize the draw stack with cards from the game state
        initialStackView(game.drawPile, drawStackView, cardImageLoader)

        // Initialize the exchange area with current exchange cards
        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)

        initialDiscardView(game.discardPile, discardView, cardImageLoader)

        // playerHands and name labels
        initialPlayerNameLabel(playerName1, game.player1.name)
        initialPlayerNameLabel(playerName2, game.player2.name)
        initialHandView(game.player1.openCards, game.player1.hiddenCards, player1Hand, cardImageLoader)
        initialHandView(game.player2.openCards, game.player2.hiddenCards, player2Hand, cardImageLoader)

        updateLog()

        // Update Score-Labels
        player1ScoreLabel.text = "Score: ${game.player1.score}"
        player2ScoreLabel.text = "Score: ${game.player2.score}"

    }


    /**
     * Refreshes the game scene after the player has drawn a card this turn.
     *
     * @param card The card that was drawn by the current player
     */

    override fun refreshAfterShiftLeft() {

        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val cardImageLoader = CardImageLoader()

        // Remove the top card view from the draw stack
        val drawnCardView = drawStackView.pop().apply { this.showFront() }
        initialStackView(game.drawPile, drawStackView, cardImageLoader)

        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)

        initialDiscardView(game.discardPile, discardView, cardImageLoader)

        updateLog()

        // Wait to allow animations update
        Thread.sleep(700)

    }

    override fun refreshAfterShiftRight() {

        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val cardImageLoader = CardImageLoader()

        // Remove the top card view from the draw stack
        val drawnCardView = drawStackView.pop().apply { this.showFront() }
        initialStackView(game.drawPile, drawStackView, cardImageLoader)

        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)

        initialDiscardView(game.discardPile, discardView, cardImageLoader)

        updateLog()

        // Wait to allow animations update
        Thread.sleep(700)

    }

    /**
     * Refreshes the game scene after the player has swapped cards this turn.
     *
     */

    override fun refreshAfterSwapAll(){

        // Get the current game instance from the root service
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val cardImageLoader = CardImageLoader()

        // Update the hand view for the current player
        if (game.currentPlayer == 1) {
            initialHandView(game.player1.openCards, game.player1.hiddenCards, player1Hand , cardImageLoader)
        } else {
            initialHandView(game.player2.openCards, game.player2.hiddenCards, player2Hand , cardImageLoader)
        }
        // Update the exchange area view to reflect any swapped cards
        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)

        updateLog()

        // Reset all selection states since the swap is completed
        selectedHandCard = null
        selectedExchangeCard = null
        selectedExchangeIndex = null

        // Wait to allow animations update
        Thread.sleep(700)

    }

    override fun refreshAfterSwapSingle() {

        // Get the current game instance from the root service
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val cardImageLoader = CardImageLoader()

        // Update the hand view for the current player
        if (game.currentPlayer == 1) {
            initialHandView(game.player1.openCards, game.player1.hiddenCards, player1Hand , cardImageLoader)
        } else {
            initialHandView(game.player2.openCards, game.player2.hiddenCards, player2Hand , cardImageLoader)
        }
        // Update the exchange area view to reflect any swapped cards
        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)

        updateLog()

        // Reset all selection states since the swap is completed
        selectedHandCard = null
        selectedExchangeCard = null
        selectedExchangeIndex = null

        // Wait to allow animations update
        Thread.sleep(700)

    }


    /**
     * Refreshes the game scene after start turn
     *
     */

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        val cardImageLoader = CardImageLoader()

        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)
        initialStackView(game.drawPile, drawStackView, cardImageLoader)
        initialDiscardView(game.discardPile, discardView, cardImageLoader)
        initialPlayerNameLabel(playerName1, game.player1.name)
        initialHandView(game.player1.openCards, game.player1.hiddenCards, player1Hand , cardImageLoader)
        initialPlayerNameLabel(playerName2, game.player2.name)
        initialHandView(game.player2.openCards, game.player2.hiddenCards, player2Hand , cardImageLoader)

        updateLog()

    }

    override fun refreshAfterSkipSwap() {
        updateLog()
    }


    /**
     * Refreshes the game scene after End turn
     *
     */

    override fun refreshAfterEndTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        val cardImageLoader = CardImageLoader()
        cardMap.clear()

        initialExchangeView(game.centerCards, exchangeArea, cardImageLoader)
        initialStackView(game.drawPile, drawStackView, cardImageLoader)
        initialDiscardView(game.discardPile, discardView, cardImageLoader)
        initialPlayerNameLabel(playerName1, game.player1.name)
        initialHandView(game.player1.openCards, game.player1.hiddenCards, player1Hand , cardImageLoader)
        initialPlayerNameLabel(playerName2, game.player2.name)
        initialHandView(game.player2.openCards, game.player2.hiddenCards, player2Hand , cardImageLoader)

        updateLog()

        // Score-Labels
        player1ScoreLabel.text = "Score: ${game.player1.score}"
        player2ScoreLabel.text = "Score: ${game.player2.score}"

         // reset cards
        selectedHandCard = null
        selectedExchangeCard = null
        selectedExchangeIndex = null

        //reset the Animation

        clickedHandCard?.let {
            resetCardPosition(it)
            resetCardSize(it)
        }
        clickedExchangeCard?.let {
            resetCardPosition(it)
            resetCardSize(it)
        }

        clickedHandCard = null
        clickedExchangeCard= null

    }

    private fun updateLog() {
        val game = rootService.currentGame ?: return

        val lines = mutableListOf("\n")
        game.log.takeLast(20).forEach {
            lines.add("• $it")
            lines.add("")
        }

        logLabel.text = lines.joinToString("\n")
    }



    // Help Method -----------------------------------------------------------------------------------------------


    /*
    private fun onHandCardClicked(card: Card, cardView: CardView) {

        println("Hand card clicked: $card")
        if (card in currentCombo) {
            currentCombo.remove(card)
        } else {
            currentCombo.add(card)
            selectedHandCard = card
        }
    }

 */
    /**
     * Triggered when a hand card is clicked.
     * Handles animations, selection toggling and updates current combo
     *
     * @param card The logical card that was clicked in the hand
     * @param cardView The visual component representing the clicked card
     */


    private fun onHandCardClicked(card: Card, cardView: CardView) {
        println("Hand card clicked: $card")

        // Toggle: If already selected, deselect
        if (selectedHandCard == card) {
            selectedHandCard = null
            clickedHandCard = null
            resetCardPosition(cardView)
            resetCardSize(cardView)
            return
        }

        // Reset the previously clicked card
        clickedHandCard?.let { previousCard ->
            resetCardPosition(previousCard)
            resetCardSize(previousCard)
        }

        // Select and animate
        selectedHandCard = card
        clickedHandCard = cardView


        // Change the position of a card
        playAnimation(
            MovementAnimation(
                componentView = cardView,
                byX = 0,
                byY = -30,
                duration = 300
            )
        )

        // Change the size of a card
        playAnimation(
            ScaleAnimation(
                componentView = cardView,
                toScaleX = 1.1,
                toScaleY = 1.1,
                duration = 300
            )
        )
    }

    /**
     * Handles the logic and animations when a card in the exchange area is clicked.
     *
     * @param card The exchange card that was clicked
     * @param cardView The visual representation of the clicked card
     * @param index The index of the card in the exchange area
     */

    private fun onExchangeCardClicked(card: Card, cardView: CardView, index: Int) {
        println("Exchange card clicked: $card")

        // Reset the visual state of all other exchange cards
        exchangeArea.components.forEach { otherCardView ->
            if (otherCardView != cardView) {
                resetCardPosition(otherCardView)
                resetCardSize(otherCardView)
            }
        }

        // If the clicked card is already selected:
        if (selectedExchangeCard == card) {

            //deselect the card
            selectedExchangeCard = null
            selectedExchangeIndex = null
            clickedExchangeCard = null
            // remove the animations
            resetCardPosition(cardView)
            resetCardSize(cardView)
        }
        // Select this card and animate it to show it's active
        else {
            selectedExchangeCard = card
            selectedExchangeIndex = index
            clickedExchangeCard = cardView

            playAnimation(
                MovementAnimation(
                    componentView = cardView,
                    byX = 0,
                    byY = -30,
                    duration = 300
                )
            )
            playAnimation(
                ScaleAnimation(
                    componentView = cardView,
                    toScaleX = 1.1,
                    toScaleY = 1.1,
                    duration = 300
                )
            )
        }
    }


    /**
     * Resets the position of a card component to its original location.
     * @param component The card component to reset.
     */

    private fun resetCardPosition(component: ComponentView?) {
        component?.apply {
            playAnimation(
                MovementAnimation(
                    componentView = this,
                    byX = 0,
                    byY = 0,
                    duration = 0
                )
            )
        }
    }

    /**
     * Resets the size of a card component to its original scale .
     *
     * @param component The card component to reset.
     */

    private fun resetCardSize(component: ComponentView?) {
        // Apply scale animation only if the component is not null
        component?.apply {
            playAnimation(
                ScaleAnimation(
                    componentView = this,
                    toScaleX = 1.0,
                    toScaleY = 1.0,
                    duration = 0
                )
            )
        }
    }


    // initial Views ----------------------------------------------------------------------

    /**
     * Initializes the visual stack of cards
     *
     * @param stack The list of cards to be displayed in the stack.
     * @param stackView The UI component that visually represents the stack.
     * @param cardImageLoader Responsible for loading front and back images for cards.
     */

    private fun initialStackView(
        stack: MutableList<Card>,
        stackView: LabeledStackView,
        cardImageLoader: CardImageLoader
    ) {
        // Clear the current visual stack
        stackView.clear()
        // Loop through the stack in reverse
        stack.asReversed().forEach { card ->
            // Create a visual representation of the card
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                showBack()
            }

            // Add the cardView to the visual stack
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    /**
     * Initializes the player's hand view by loading card images and setting up click actions.
     *
     * @param hand The list of cards in the player's hand.
     * @param handDeckView The UI component that displays the player's hand.
     * @param cardImageLoader Handles loading of card front and back images.
     * @param cardsShown If true, cards are shown face-up and interactive
     */

    private fun initialHandView(
        openCards: MutableList<Card>,
        hiddenCards: MutableList<Card>,
        handDeckView: HandView,
        cardImageLoader: CardImageLoader
    ) {
        handDeckView.clear()

        // Add open cards (top 3)
        openCards.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                showFront()
                onMouseClicked = { onHandCardClicked(card, this) }
            }
            handDeckView.add(cardView)
            cardMap.add(card to cardView)
        }

        // Add hidden cards (bottom 2)
        hiddenCards.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                showBack()
                isDisabled = true
            }
            handDeckView.add(cardView)
            cardMap.add(card to cardView)
        }
    }


    /**
     * Initializes the exchange area by displaying the given exchange cards.
     * Cards are shown face-up and are interactive (clickable).
     *
     * @param exchangeCards The list of cards currently in the exchange area.
     * @param exchangeArea The UI component where the cards will be displayed.
     * @param cardImageLoader Handles loading of card front and back images.
     */


    private fun initialExchangeView(
        exchangeCards: MutableList<Card>,
        exchangeArea: LinearLayout<CardView>,
        cardImageLoader: CardImageLoader
    ) {
        exchangeArea.clear()

        exchangeCards.forEachIndexed { index, card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                showFront()  // Always show the front of exchange cards
                onMouseClicked = {
                    onExchangeCardClicked(card, this, index)
                }
            }
            // Add the card view to the exchange UI area
            exchangeArea.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun initialDiscardView(
        discardPile: MutableList<Card>,
        discardView: LinearLayout<CardView>,
        cardImageLoader: CardImageLoader
    ) {
        discardView.clear()

        discardPile.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                showFront()
            }
            discardView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    /**
     * Initializes the player's name label
     *
     * @param label The UI label that will display the player's name.
     * @param text The text to display
     */


    private fun initialPlayerNameLabel(label: Label, text: String) {
        label.text = text
        label.font = Font(
            size = 28,
            fontWeight = Font.FontWeight.BOLD
        )
    }

}



