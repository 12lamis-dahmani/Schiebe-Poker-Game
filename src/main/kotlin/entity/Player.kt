package entity

class Player(val name: String) {

    var score: Int = 0

    var hiddenCards: MutableList<Card> = mutableListOf() // genau 2
    var openCards: MutableList<Card> = mutableListOf()   // genau 3

    override fun toString(): String =
        "$name: Score=$score Open=${openCards.size} Hidden=${hiddenCards.size}"
}