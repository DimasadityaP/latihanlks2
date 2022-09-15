package com.example.latihanlks2.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.latihanlks2.R
import com.example.latihanlks2.databinding.ActivityLoginBinding
import com.example.latihanlks2.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    private var pref : SharedPreferences = baseContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    var username: String?
        get() {
            return pref.getString(KEY_USERNAME, null)
        }
        set(value) {
            editor.putString(KEY_USERNAME, value)
            editor.commit()
        }

    var password: String?
        get() {
            return pref.getString(KEY_PASSWORD, null)
        }
        set(value) {
            editor.putString(KEY_PASSWORD, value)
            editor.commit()
        }

    companion object{
        const val SHARED_PREFERENCES_NAME = "MySharedPreferences"
        const val KEY_USERNAME = "Key_Username"
        const val KEY_PASSWORD = "Key_Password"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setUp()
    }

    private fun setUp(){
        binding.btnLogin.setOnClickListener {
            when{
                binding.EdtUsername.text.isNullOrEmpty() ->{
                    binding.EdtUsername.error = "Username tidak boleh kosong"
                }
                binding.EdtPassword.text.isNullOrEmpty() ->{
                    binding.EdtPassword.error = "Password tidak boleh kosong"
                }
                else ->{
                    username = binding.EdtUsername.text.toString().trim()
                    password = binding.EdtPassword.text.toString().trim()
                    val intentmain = Intent(this, MainActivity::class.java)
                    startActivity(intentmain)
                }
            }
        }
    }
}