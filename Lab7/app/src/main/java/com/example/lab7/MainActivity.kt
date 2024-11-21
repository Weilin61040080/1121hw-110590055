package com.example.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 設定 WindowInsets，避免系統欄遮蓋 UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            view.setPaddingWithInsets(insets.getInsets(WindowInsetsCompat.Type.systemBars()))
            insets
        }

        // 初始化 UI 元件
        val spinner: Spinner = findViewById(R.id.spinner)
        val listView: ListView = findViewById(R.id.listView)
        val gridView: GridView = findViewById(R.id.gridView)

        // 資料準備
        val priceRange = 10..100
        val itemData = prepareItems(priceRange)
        val countData = itemData.indices.map { "${it + 1}個" }

        // 配置 Spinner
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countData)

        // 配置 GridView
        gridView.numColumns = 3
        gridView.adapter = MyAdapter(this, itemData, R.layout.adapter_vertical)

        // 配置 ListView
        listView.adapter = MyAdapter(this, itemData, R.layout.adapter_horizontal)
    }

    /**
     * 擴充 View 的 Padding 設定
     */
    private fun android.view.View.setPaddingWithInsets(insets: WindowInsetsCompat.Insets) {
        setPadding(insets.left, insets.top, insets.right, insets.bottom)
    }

    /**
     * 準備水果資料
     */
    private fun prepareItems(priceRange: IntRange): List<Item> {
        val typedArray = resources.obtainTypedArray(R.array.image_list)
        return List(typedArray.length()) { index ->
            val photo = typedArray.getResourceId(index, 0)
            val name = "水果${index + 1}"
            val price = priceRange.random()
            Item(photo, name, price)
        }.also {
            typedArray.recycle()
        }
    }
}
