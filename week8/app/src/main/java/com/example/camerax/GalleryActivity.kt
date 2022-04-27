package com.example.camerax

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val fileList = imageReaderNew(getOutputDirectory())

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = GridLayoutManager(this, 2)

        val adapter = CustomAdapter(fileList)

        recyclerview.adapter = adapter

        adapter.setOnClickListener(object : CustomAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@GalleryActivity, ShowImage::class.java)
                intent.putExtra("path", fileList[position].path)
                startActivity(intent)
            }
        })
    }

    private fun getOutputDirectory() :File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun imageReaderNew(root: File): ArrayList<ItemsViewModal> {
        val fileList = ArrayList<ItemsViewModal>()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.size > 0) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".jpg")) {
                    val myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath())
                    fileList.add(ItemsViewModal(currentFile.getAbsolutePath(), myBitmap, currentFile.name))
                }
            }
        }

        return fileList
    }

}