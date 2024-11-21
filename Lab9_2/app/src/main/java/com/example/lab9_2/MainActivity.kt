package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private val inputs by lazy {
        listOf(edHeight to "請輸入身高", edWeight to "請輸入體重", edAge to "請輸入年齡")
    }
    private val edHeight by lazy { findViewById<EditText>(R.id.edHeight) }
    private val edWeight by lazy { findViewById<EditText>(R.id.edWeight) }
    private val edAge by lazy { findViewById<EditText>(R.id.edAge) }
    private val tvWeightResult by lazy { findViewById<TextView>(R.id.tvWeightResult) }
    private val tvFatResult by lazy { findViewById<TextView>(R.id.tvFatResult) }
    private val tvBmiResult by lazy { findViewById<TextView>(R.id.tvBmiResult) }
    private val tvProgress by lazy { findViewById<TextView>(R.id.tvProgress) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }
    private val llProgress by lazy { findViewById<LinearLayout>(R.id.llProgress) }
    private val btnBoy by lazy { findViewById<RadioButton>(R.id.btnBoy) }
    private val btnCalculate by lazy { findViewById<Button>(R.id.btnCalculate) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.main).applyInsets()

        btnCalculate.setOnClickListener {
            validateInputs() ?: runThread()
        }
    }

    private fun validateInputs(): Unit? =
        inputs.firstOrNull { it.first.text.isEmpty() }?.let {
            showToast(it.second)
        }

    private fun runThread() {
        resetResults()
        showProgress()

        Thread {
            (1..100).forEach { progress ->
                Thread.sleep(50)
                runOnUiThread {
                    progressBar.progress = progress
                    tvProgress.text = "$progress%"
                }
            }

            val (height, weight, age) = listOf(edHeight, edWeight, edAge).map {
                it.text.toString().toDouble()
            }
            val bmi = weight / (height / 100).pow(2)
            val (standWeight, bodyFat) = calculateMetrics(height, bmi, age)

            runOnUiThread {
                hideProgress()
                updateResults(standWeight, bodyFat, bmi)
            }
        }.start()
    }

    private fun calculateMetrics(height: Double, bmi: Double, age: Double) =
        if (btnBoy.isChecked)
            (height - 80) * 0.7 to 1.39 * bmi + 0.16 * age - 19.34
        else
            (height - 70) * 0.6 to 1.39 * bmi + 0.16 * age - 9

    private fun resetResults() {
        tvWeightResult.text = "標準體重\n無"
        tvFatResult.text = "體脂肪\n無"
        tvBmiResult.text = "BMI\n無"
    }

    private fun updateResults(weight: Double, fat: Double, bmi: Double) {
        tvWeightResult.text = "標準體重 \n${weight.format(2)}"
        tvFatResult.text = "體脂肪 \n${fat.format(2)}"
        tvBmiResult.text = "BMI \n${bmi.format(2)}"
    }

    private fun showProgress() {
        progressBar.progress = 0
        tvProgress.text = "0%"
        llProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        llProgress.visibility = View.GONE
    }

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun View.applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun Double.format(digits: Int) = String.format("%.${digits}f", this)
}
