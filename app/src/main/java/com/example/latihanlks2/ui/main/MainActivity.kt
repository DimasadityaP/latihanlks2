package com.example.latihanlks2.ui.main

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.latihanlks2.R
import com.example.latihanlks2.databinding.ActivityMainBinding
import com.example.latihanlks2.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private var pref : SharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.tvUsername.text = pref.getString("username", "KEY_USERNAME")
    }
}