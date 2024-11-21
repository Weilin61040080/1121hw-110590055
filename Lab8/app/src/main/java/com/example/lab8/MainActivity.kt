package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val contacts = mutableListOf<Contact>()
    private lateinit var myAdapter: MyAdapter

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val name = intent.getStringExtra("name").orEmpty()
                val phone = intent.getStringExtra("phone").orEmpty()
                contacts.add(Contact(name, phone))
                myAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<android.view.View>(R.id.main).applyInsets()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val btnAdd: Button = findViewById(R.id.btnAdd)

        myAdapter = MyAdapter(contacts)
        recyclerView.setupWithAdapter(myAdapter)

        btnAdd.setOnClickListener {
            Intent(this, SecActivity::class.java).also { startForResult.launch(it) }
        }
    }

    private fun RecyclerView.setupWithAdapter(adapter: MyAdapter) {
        layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
        this.adapter = adapter
    }

    private fun android.view.View.applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
