package com.ingencode.reciclaia.ui.screens.imagevisor

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ingencode.reciclaia.databinding.ActivityImagevisorBinding
import com.ingencode.reciclaia.databinding.ActivityMainBinding
import com.ingencode.reciclaia.utils.applyTheme
import java.net.URI

class ImageVisor : AppCompatActivity() {
    private lateinit var binding: ActivityImagevisorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagevisorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val uri = intent.getParcelableExtra("uri", Uri::class.java)
        uri?.let { binding.visor.setImageUri(it) }
    }
}