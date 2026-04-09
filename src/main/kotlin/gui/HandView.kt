package gui

import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView

/**
 * This view is used for both players and shows the cards they currently hold.
 * It arranges the cards in a row with consistent spacing
 *
 * @param posX The horizontal position of the hand on the screen
 * @param posY The vertical position of the hand on the screen
 */

class HandView(posX: Number, posY: Number):
    LinearLayout<CardView>(spacing = 4.0, posX = posX, posY = posY)
{
    init {
        /* width = 1000.0
         height = 400.0
         */
        width = 800.0
        height = 200.0
    }
}