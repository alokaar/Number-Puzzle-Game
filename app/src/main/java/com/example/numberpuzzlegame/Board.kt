package com.example.numberpuzzlegame

import java.util.Random

class Board(size: Int) {

    private var size: Int = 0
    private var numOfMoves: Int = 0
    private val places: MutableList<Place>
    private val listeners: MutableList<BoardChangeListener>

    fun rearrange() {
        numOfMoves = 0
        for (i in 0 until size * size) {
            swapTiles()
        }
        do {
            swapTiles()
        } while (!solvable() || isSolved()) // Use isSolved instead of solved
    }

    private fun swapTiles() {
        val random = Random()
        val p1: Place? = at(rendoms.nextInt(size) + 1, rendoms.nextInt(size) + 1)
        val p2: Place? = at(rendoms.nextInt(size) + 1, rendoms.nextInt(size) + 1)

        if (p1 != p2) {
            val t: Tile? = p1?.tile
            p1!!.tile = p2?.tile
            p2!!.tile = t
        }
    }

    private fun solvable(): Boolean {
        var inversion = 0
        for (p: Place in places) {
            val pt: Tile? = p.tile
            for (q: Place in places) {
                var qt: Tile? = q.tile
                if (p != q && pt != null && qt != null
                    && indexOf(p) < indexOf(q)  // Use custom indexOf for position
                    && pt.number() > qt.number()) {
                    inversion++
                }
            }
        }
        var isEvenSize = size % 2 == 0
        var isEvenInversion = inversion % 2 == 0
        var isBlankOnOddRow = blank()!!.y % 2 === 1

        //from the bottom
        isBlankOnOddRow = if (isEvenSize) !isBlankOnOddRow else isBlankOnOddRow

        return !isEvenSize && isEvenInversion ||
                isEvenSize && isBlankOnOddRow == isEvenInversion
    }

    fun indexOf(p: Place): Int {
        return (p.y - 1) * size + p.x
    }

    fun isSolved(): Boolean {  // Renamed method from solved
        var result = true
        for (p: Place in places) {
            result = result &&
                    (p.x === size && p.y === size
                            || p.tile != null &&
                            p.tile!!.number() === indexOf(p))

        }
        return result
    }

    fun slide(tile: Tile) {
        for (p: Place in places) {
            if (p.tile == tile) {
                val to: Place? = blank()
                to!!.tile = tile
                p.tile = null
                numOfMoves++
                notifyTileSliding(p, to, numOfMoves)
                if (isSolved()) {
                    notifyPuzzleSolved(numOfMoves)  // Use notifyPuzzleSolved
                }
                return
            }
        }
    }

    fun slideable(place: Place): Boolean {
        val x: Int = place.x
        val y: Int = place.y
        return (isBlank(x - 1, y) || isBlank(x + 1, y)
                || isBlank(x, y - 1) || isBlank(x, y + 1))
    }

    private fun isBlank(x: Int, y: Int): Boolean {
        return ((0 < x && x <= size)
                && (0 < y && y <= size)
                && (at(x, y)!!.tile == null))
    }

    fun blank(): Place? {
        for (p: Place in places) {
            if (p.tile == null) {
                return p
            }
        }
        //assert false : "should never reach here !!"
        return null
    }

    fun places(): Iterable<Place> = places

    fun at(x: Int, y: Int): Place? {

        for (p: Place in places) {
            if (p.x === x && p.y === y) {
                return p
            }
        }
        //asert false : "precondition violation 11"
        return null
    }
    fun size():Int =size
    fun numOfMoves():Int = numOfMoves

    fun addBoardChangeListener(listener: BoardChangeListener){
        if(!listeners.contains(listener)){
            listeners.add(listener)
        }

    }


    fun removeBoardChangeListener(listener: BoardChangeListener){
        listeners.remove(listener)
    }

    fun notifyTileSliding(from:Place, to :Place,numOfMove:Int){
        for(listener : BoardChangeListener in listeners){
            listener.tileSlid(from,to,numOfMoves)
        }

    }
    private  fun notifyPuzzleSolved(numOfMove: Int){
        for (listener:BoardChangeListener in listeners){
            listener.solved(numOfMove)
        }
    }
    //random shuffle
    companion object{
        private val rendoms:kotlin.random.Random=kotlin.random.Random
    }
    init {
        listeners = ArrayList()
        this.size = size
        places = ArrayList<Place>(size *size)
        for(x in 1..size) {
            for(y in 1 ..size) {
                places.add(
                    if (x == size && y == size) {
                        Place(x, y, this)
                    }else{
                        Place(x, y, (y - 1) * size + x,this)
                    }
                )
            }
        }
        numOfMoves =0
    }
}







//state of the game board in a number puzzle game. It manages the placement and movement of tiles, checks for
// solvability, and notifies listeners of board changes and puzzle solving events.