package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in [ResultMenuScene]. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 *
 * @param rootService [RootService] instance to access the service methods and entity layer
 */

class NewGameMenuScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    // Semi-transparent dark background overlay
    private val blackBox = Label(
        width = 1920,
        height = 1080,
        visual = ColorVisual(0, 0, 0, 120)
    )

    // Semi-transparent white box in the center to contain input fields
    private val backgroundBox = Label(
        width = 600,
        height = 455,
        posX = (1920 - 600) / 2,
        posY = (1080 - 400) / 2,
        visual = ColorVisual(255, 255, 255, 160).apply {  style.borderRadius = BorderRadius(20)
        }
    )

    // Main title label
    private val headlineLabel = Label(
        width = 400,
        height = 50,
        posX = 760,
        posY = 380,
        text = "Start New Game",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    )

    // Label and input field for Player 1 name
    private val p1Label = Label(
        width = 100,
        height = 35,
        posX = 810,
        posY = 450,
        text = "Player 1:",
        font = Font(size = 20, fontWeight = Font.FontWeight.BOLD)
    )

    /** Input for Player 1's name with a random default name */

    private val p1Input: TextField = TextField(
        width = 200,
        height = 35,
        posX = 920,
        posY = 450,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random(),
        font = Font(size = 20, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        // Light grey background
        visual = ColorVisual(225, 225, 225).apply {
            style.borderRadius = BorderRadius.SMALL
        }
        // Disable the Start button  if either input is blank
        onKeyReleased = {
            updateStartButtonState()
        }
    }

    // Label and input field for Player 2 name
    private val p2Label = Label(
        width = 100,
        height = 35,
        posX = 810,
        posY = 500,
        text = "Player 2:",
        font = Font(
            size = 20,
            fontWeight = Font.FontWeight.BOLD
        )
    )

    // Player 2 name input
    private val p2Input: TextField = TextField(
        width = 200,
        height = 35,
        posX = 920,
        posY = 500,
        text = listOf("Fry", "Bender", "Leela", "Amy").random(),
        font = Font(
            size = 20,
            fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(225, 225, 225).apply {
            style.borderRadius = BorderRadius.SMALL
        }
        onKeyReleased = {
            updateStartButtonState()
        }
    }

    private val p3Label = Label(
        width = 100,
        height = 35,
        posX = 810,
        posY = 550,
        text = "Rounds:",
        font = Font(
            size = 20,
            fontWeight = Font.FontWeight.BOLD
        )
    )

    private val p3Input: TextField = TextField(
        width = 200,
        height = 35,
        posX = 920,
        posY = 550,
        text = listOf(2, 3, 4, 5, 6, 7).random().toString() ,
            font = Font(
            size = 20,
            fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(225, 225, 225).apply {
            style.borderRadius = BorderRadius.SMALL
        }
        onKeyReleased = {
            updateStartButtonState()
        }
    }

    private fun updateStartButtonState() {
        val rounds = p3Input.text.toIntOrNull()

        startButton.isDisabled =
            p1Input.text.isBlank() ||
                    p2Input.text.isBlank() ||
                    rounds == null ||
                    rounds !in 2..7
    }

    /**
     * Button to start the game using entered player names
     */
    private val startButton = Button(
        width = 140,
        height = 40,
        posX = 890,
        posY = 650,
        text = "Start",
        font = Font(
            size = 18,
            fontWeight = Font.FontWeight.BOLD)
    ).apply {


        // Default gray color
        visual = ColorVisual(150, 150, 150).apply { style.borderRadius = BorderRadius.LARGE }
        onMouseEntered = {
            // Darker gray on hover
            visual = ColorVisual(100, 100, 100).apply { style.borderRadius = BorderRadius.LARGE } }
        onMouseExited = {
            // Revert to original gray on mouse exit
            visual = ColorVisual(150, 150, 150).apply { style.borderRadius = BorderRadius.LARGE } }

        onMouseClicked = {
            // reset hover right after the click
            visual = ColorVisual(150, 150, 150).apply { style.borderRadius = BorderRadius.LARGE }
            rootService.gameService.startNewGame(
                p1Input.text.trim(),
                p2Input.text.trim(),
                maxRound = p3Input.text.trim().toInt(),
            )
        }
    }

    // Button to quit the game entirely
    private val quitButton = Button(
        width = 140,
        height = 40,
        posX = 890,
        posY = 700,
        text = "Quit",
        font = Font(size = 18, fontWeight = Font.FontWeight.BOLD)
    ).apply {

        // Red button
        visual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE }
        onMouseEntered = {
            // Darker red on hover
            visual = ColorVisual(178, 34, 34).apply { style.borderRadius = BorderRadius.LARGE } }
        onMouseExited = {
            // Revert to normal red on mouse exit
            visual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE } }

        onMouseClicked = {
            visual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE }
            rootService.gameService.quitGame()
        }
    }

    // Initialization of scene elements
    init {
        opacity = 0.8
        background = ImageVisual("poker1.jpeg")
        addComponents(
            blackBox,
            backgroundBox,
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            p3Label, p3Input,
            startButton, quitButton
        )
    }
}
