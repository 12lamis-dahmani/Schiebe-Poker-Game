package gui

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.MenuScene
import service.RootService
import service.Refreshable
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
//import tools.aqua.bgw.visual.ImageVisual

/**
 * This scene is shown between player turns to inform the next player and allow them to start their turn.
 *
 * @param rootService Reference to the [RootService] that manages game state and logic
 */

class ChangePlayerScene(private val rootService: RootService) : MenuScene(800, 500) , Refreshable {

    // Retrieve the current game instance or throw an error if none is active
    val game = requireNotNull(rootService.currentGame) { "No game is currently active." }

    // Identify the current player based on the game state's player index
    val currPlayer = when (game.currentPlayer) {
        1 -> game.player1
        2 -> game.player2
        else -> throw IllegalStateException(
            "Invalid current player index: ${game.currentPlayer}")
    }


    /**
     * Button to start the player's turn and continue the game
    */
    private val startTurnButton = Button(
        width = 200,
        height = 80,
        posX = 300,
        posY = 300,
        text = " Start Turn ",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {

        visual = ColorVisual(255, 215, 0).apply { style.borderRadius = BorderRadius.LARGE }
        onMouseClicked = { rootService.gameService.startTurn() }
    }


    /**
     * Label that greets the current player by name
     */

    private val playerNameLabel = Label(
        width = 800,
        height = 80,
        posX = 0,
        posY = 120,
        text = " Hi ${currPlayer.name}! It`s your Turn in Round ${game.currentRound} ",
        font = Font(size = 36, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(225, 225, 225)
    }

    /**
     * Initializes the scene
     */

    init {
         opacity = 0.8
        background = ColorVisual(64, 64, 64)
            addComponents(playerNameLabel, startTurnButton)
    }
}

