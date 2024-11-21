package com.example.lab9_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var progressRabbit = 0
    private var progressTurtle = 0
    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> updateProgress(sbRabbit, progressRabbit, "兔子勝利")
            2 -> updateProgress(sbTurtle, progressTurtle, "烏龜勝利")
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<android.view.View>(R.id.main).applyInsets()

        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)

        btnStart.setOnClickListener {
            startRace()
        }
    }

    private fun startRace() {
        btnStart.isEnabled = false
        resetProgress()
        thread { runRabbit() }
        thread { runTurtle() }
    }

    private fun resetProgress() {
        progressRabbit = 0
        progressTurtle = 0
        sbRabbit.progress = 0
        sbTurtle.progress = 0
    }

    private fun updateProgress(seekBar: SeekBar, progress: Int, victoryMessage: String) {
        seekBar.progress = progress
        if (progress >= 100 && (progressRabbit < 100 || progressTurtle < 100)) {
            showToast(victoryMessage)
            btnStart.isEnabled = true
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun runRabbit() {
        val sleepProbability = arrayOf(true, true, false)
        while (progressRabbit < 100 && progressTurtle < 100) {
            Thread.sleep(100)
            if (sleepProbability.random()) Thread.sleep(300)
            progressRabbit += 3
            handler.sendMessage(handler.obtainMessage(1))
        }
    }

    private fun runTurtle() {
        while (progressTurtle < 100 && progressRabbit < 100) {
            Thread.sleep(100)
            progressTurtle += 1
            handler.sendMessage(handler.obtainMessage(2))
        }
    }

    private fun android.view.View.applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
