import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.visual.ColorVisual

/**
 * This view displays the discard pile of cards.
 * Cards that have been discarded are shown here.
 *
 * @param posX The horizontal position of the discard area on the screen
 * @param posY The vertical position of the discard area on the screen
 */

class DiscardView(posX: Number, posY: Number) : LinearLayout<CardView>(
    spacing = 5.0,
    posX = posX,
    posY = posY
) {
    init {
        width = 120.0
        height = 190.0
        alignment = Alignment.CENTER
        visual = ColorVisual(139, 69, 19)  // Braun für Discard-Pile
    }
}