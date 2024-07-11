package com.example.numberpuzzlegame

class Tile (

    private var number:Int? = null
)
{
    fun number():Int {
        return number!!
    }
}