package com.example.numberpuzzlegame

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mainView: ViewGroup? = null
    private var board: Board? = null
    private var boardView: BoardView? = null
    private lateinit var moves: TextView
    private var boardSize = 4

    private val maxMoves = arrayOf(
        intArrayOf(0, 0, 90),
        intArrayOf(0, 0, 200),
        intArrayOf(0, 0, 400),
        intArrayOf(0, 0, 800)
    ) // Maximum allowed moves for each size (adjust as needed)

    private var currentScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainView = findViewById(R.id.mainView)
        moves = findViewById(R.id.moves)
        moves.setTextColor(Color.RED)
        moves.textSize = 20f
        newGame()
    }

    private fun newGame() {
        board = Board(boardSize)
        calculateMaxMoves(boardSize); // Set max moves based on size
        board?.addBoardChangeListener(boardChangeListener)
        board?.rearrange()
        mainView?.removeView(boardView)
        boardView = BoardView(this, board!!)
        mainView?.addView(boardView)
        moves.text = "Number of Movements: 0"
        currentScore = calculateScore(0); // Initial score based on zero moves

    }

    fun changeSize(newSize: Int) {
        if (newSize != boardSize) {
            boardSize = newSize
            newGame()
            boardView!!.invalidate()
        }
    }

    private val boardChangeListener: BoardChangeListener = object : BoardChangeListener {
        override fun tileSlid(from: Place?, to: Place?, numOfMoves: Int) {
            moves.text = "Number of Movements: $numOfMoves"
            currentScore = calculateScore(numOfMoves) // Update score based on new moves
        }

        override fun solved(numOfMoves: Int) {
            val score = calculateScore(numOfMoves)  // Calculate score

            moves.text = if (score == 0) {
                "But Out of Moves!"
            } else {
                "Solved in $numOfMoves moves \n Your score is: $score  " +
                        "continue?"
            }

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("You Won!")
                .setMessage(moves.text.toString()) // Use the updated moves text
                .setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                    board!!.rearrange()
                    moves.text = "Number of Movements: 0"
                    currentScore = calculateScore(0) // Reset score
                    boardView!!.invalidate()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun calculateMaxMoves(size: Int) {
        if (size >= 1 && size <= maxMoves.size) {
            maxMoves[size - 1][0] = boardSize // Set board size for reference
            maxMoves[size - 1][1] = boardSize * boardSize // Total tiles
            maxMoves[size - 1][2] = Math.max(size * 20, 30);// Base max moves based on array length
        }
    }

    private fun calculateScore(moves: Int): Int {
        val baseScore = 200 // Adjust base score as needed
        return if (moves <= maxMoves[boardSize - 1][2]) {
            // Calculate score based on remaining moves (more moves, less score)
            baseScore - moves * (baseScore / maxMoves[boardSize - 1][2])
        } else {
            // Handle failure if exceeding max moves
            0 // Or display "Failed" message and reset game
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_game -> {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("New Game")
                    .setMessage("Are u sure you want to begin a new game")
                    .setPositiveButton("Yes") { dialog, _ ->
                        board!!.rearrange()
                        moves.text = "Number of Movements : 0"
                        boardView!!.invalidate()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
                true
            }

            R.id.action_settings -> {
                val settings = SettingsDialogFragment(boardSize)
                settings.show(supportFragmentManager, "fragment_settings")
                true
            }

            R.id.action_help -> {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Instruction")
                    .setMessage(
                        "Arrange tiles (1-up) with fewest moves!\n" +
                                "Each move -5 pts, plan strategically!\n" +
                                "Higher score = fewer moves! Aim for the top!" +
                                "Stuck? Tap a number tile to make movements! ✨"
                    )
                    .setPositiveButton("Unsertood!! Play") { dialog, _ ->
                        dialog.dismiss()

                    }
                    .create()
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}