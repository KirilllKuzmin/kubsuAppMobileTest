package com.kubsu.kubsuappmobile.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.kubsuappmobile.databinding.ContentBaseBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ContentBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}