package entity

data class Game (val player1: Player,
                 val player2: Player,
                 var currentPlayer : Int )
{
    var drawPile: MutableList<Card> = mutableListOf()
    var centerCards: MutableList<Card> = mutableListOf()
    var discardPile: MutableList<Card> = mutableListOf()

    var currentRound: Int = 1
    var maxRounds: Int = 0

    var log: MutableList<String> = mutableListOf()
    var actionsTaken: Int = 0
    var firstAction: Action? = null
    var startingPlayer:Int = 0
}


