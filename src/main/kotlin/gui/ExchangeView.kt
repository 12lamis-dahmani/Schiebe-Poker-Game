package gui

import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.visual.ColorVisual


/**
 * This view displays a shared row of exchange cards available to both players.
 * Cards placed here can be selected for swapping with hand cards.
 *
 * @param posX The horizontal position of the exchange area on the screen
 * @param posY The vertical position of the exchange area on the screen
 */

class ExchangeView ( posX: Number, posY: Number):LinearLayout<CardView>(
    spacing = 5.0,
    posX = posX,
    posY = posY){
    init {
        width = 320.0
        height = 170.0
        alignment = Alignment.CENTER
        visual = ColorVisual(11, 94, 28)
    }
}