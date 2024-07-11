package com.example.numberpuzzlegame

interface BoardChangeListener {

    fun tileSlid(from : Place?,to: Place?,numOfMoves :Int)
    fun solved(numOfMoves: Int)
}