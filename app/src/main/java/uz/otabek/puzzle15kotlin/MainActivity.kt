package uz.otabek.puzzle15kotlin

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uz.otabek.puzzle15kotlin.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var items: Array<Array<Button>> = arrayOf()
    private var numbers: ArrayList<Int> = arrayListOf()
    private var emptySpace: Coordinate = Coordinate(2, 2)

    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadView()
        loadData()
        dataToView()
    }

    private fun loadView() {
        val group: ViewGroup = binding.container
        val count = group.childCount

        items = Array(3) { Array(3) { Button(this) } }

        for (i in 0..<count) {
            val view = group.getChildAt(i)
            val button = view as Button
            val y = i / 3
            val x = i % 3
            button.setOnClickListener {
                onItemClick(button, x, y)
            }
            items[y][x] = button
        }
    }

    private fun loadData() {
        for (i in 1..8) {
            numbers.add(i)
        }
    }

    private fun onItemClick(button: Button, x: Int, y: Int) {
        val dx = abs(emptySpace.x - x)
        val dy = abs(emptySpace.y - y)

        if (dx + dy == 1) {
            binding.textScore.text = (++score).toString()

            val text = button.text.toString()
            button.text = ""
            button.setBackgroundResource(R.color.color_item_empty)

            val temp = items[emptySpace.y][emptySpace.x]
            temp.text = text
            temp.setBackgroundResource(R.color.color_item)

            emptySpace.x = x
            emptySpace.y = y

            checkWin()
        }
    }

    private fun checkWin() {
        var isWin = false
        if (emptySpace.x == 2 && emptySpace.y == 2) {
            for (i in 0..<8) {
                if (items[i / 3][i % 3].text.toString() == (i + 1).toString()) {
                    isWin = true
                } else {
                    isWin = false
                    break
                }
            }
        }
        if (isWin) {
            Toast.makeText(this, "You win", Toast.LENGTH_SHORT).show()
            restart()
        }
    }

    private fun dataToView() {
        binding.textScore.text = "0"

        numbers.shuffle()
        items[emptySpace.y][emptySpace.x].setBackgroundResource(R.color.color_item)
        emptySpace.x = 2
        emptySpace.y = 2

        for (i in 0..<3) {
            for (j in 0..<3) {
                val index = 3 * i + j
                if (index < 8) {
                    val number = numbers[index]
                    items[i][j].text = number.toString()
                } else {
                    items[i][j].text = ""
                    items[i][j].setBackgroundResource(R.color.color_item_empty)
                }
            }
        }
    }

    private fun restart() {
        dataToView()
    }
}