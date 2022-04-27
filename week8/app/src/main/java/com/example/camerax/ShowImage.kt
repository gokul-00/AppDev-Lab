package com.example.camerax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ShowImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        val image : ImageView = findViewById(R.id.selectedImage)

        val bundle: Bundle?= intent.extras

        val path = bundle!!.getString("path")

        image.setImageBitmap(BitmapFactory.decodeFile(path))

        val btnRotate: Button= findViewById(R.id.btnRotate)
        btnRotate.setOnClickListener { image.rotation += 90f }

    }
}