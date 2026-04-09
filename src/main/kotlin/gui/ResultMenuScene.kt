package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * This scene is shown when the game has ended
 *
 * There are also two buttons:
 *"New Game": Starts a new game by calling the restartGame() function.
 *"Quit": Exits the game via quitGame().
 *
 * @param rootService The central [RootService] instance used to trigger game actions like restarting or quitting.
 * @param message The result message to show under the "Game Over" title.
 */

class ResultMenuScene(private val rootService: RootService, private val message: String) :
    MenuScene(1920, 1080), Refreshable {


    // A semi-transparent black overlay that dims the background
    private val blackBox = Label(
        width = 1920,
        height = 1080,
        visual = ColorVisual(0, 0, 0, 120)
    )

    // A white box in the center for the game over content
    private val backgroundBox = Label(
        width = 600,
        height = 350,
        posX = (1920 - 600) / 2,
        posY = (1080 - 400) / 2,
        visual = ColorVisual(255, 255, 255, 160).apply {
            style.borderRadius = BorderRadius(20)
        }
    )

    // Title label that says "Game Over"
    private val headlineLabel = Label( width = 400,
        height = 50,
        posX = 760,
        posY = 380,
        text = "Game Over",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    )

    // Label that shows the custom game result message
    private val gameResult = Label( width = 400,
        height = 35,
        posX = 760,
        posY = 450,
        text = message,
        font = Font(size = 20)
    )


    // "New Game" button: starts a new game when clicked
    private val restartButton = Button(
        width = 140,
        height = 40,
        posX = 890,
        posY = 530,
        text = "New Game",
        font = Font(
            size = 18,
            fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(0, 204, 0).apply { style.borderRadius = BorderRadius.LARGE }

        // Change color slightly on hover
        onMouseEntered = {
            visual = ColorVisual(51, 94, 28).apply { style.borderRadius = BorderRadius.LARGE } }
        onMouseExited = {
            visual = ColorVisual(0, 204, 0).apply { style.borderRadius = BorderRadius.LARGE } }
        // Start a new game when clicked
        onMouseClicked = {

            visual = ColorVisual(0, 204, 0).apply { style.borderRadius = BorderRadius.LARGE }
            rootService.gameService.restartGame()
        }
    }

    // "Quit" button: exits the game when clicked
    private val quitButton = Button( width = 140,
        height = 40,
        posX = 890,
        posY = 590,
        text = "Quit",
        font = Font(
            size = 18,
            fontWeight = Font.FontWeight.BOLD)
    ).apply {

        visual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE }
        // Hover effect with darker red
        onMouseEntered = {
            visual = ColorVisual(178, 34, 34).apply { style.borderRadius = BorderRadius.LARGE } }
        onMouseExited = {
            visual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE } }
        // Quit the game when clicked
        onMouseClicked = {

            visual = ColorVisual(220, 20, 60).apply { style.borderRadius = BorderRadius.LARGE }
            rootService.gameService.quitGame()
        }
    }

    // Initialization block: sets up the whole scene
    init {
        opacity = 0.8
        background = ImageVisual("main2.PNG")
        addComponents(
            blackBox,
            backgroundBox,
            headlineLabel,
            gameResult,
            restartButton,
            quitButton
        )
    }
}

