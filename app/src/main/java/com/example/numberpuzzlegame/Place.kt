package com.example.numberpuzzlegame

import com.example.numberpuzzlegame.Tile // Import your own Tile class

class Place (
    val x: Int,
    var y: Int,
    board: Board
) {
    var tile: Tile? = null // Use your own Tile class
    private val board: Board

    constructor(x: Int, y: Int, number: Int, board: Board) : this(x, y, board) {
        tile = Tile(number)
    }

    fun hasTile(): Boolean {
        return tile != null // Fix type mismatch by removing extra '!'
    }

    fun slidable(): Boolean {
        // Implement your slidable logic here
        return hasTile() && board.slideable(this)// Placeholder return, implement your logic
    }

    fun slide() {
        board.slide(tile!!)
    }

    init{
        y=y
        this.board =board
    }
}








//Overall, the Place class provides a representation of a position on the game board where tiles
// can be placed, along with methods to interact with those tiles.
