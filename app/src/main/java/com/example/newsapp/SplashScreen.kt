package com.example.newsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.VideoView

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private lateinit var videoView : VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        videoView = findViewById(R.id.screen)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.screen
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)
        videoView.setOnCompletionListener {
            navigateToMainActivity()
        }
        videoView.start()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMainActivity()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}