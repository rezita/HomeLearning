package com.github.rezita.homelearning.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.ActivityRegisterBinding

class RegisterActivity: AppCompatActivity()  {
    private lateinit var binding: ActivityRegisterBinding
    private val _wordPattern = Regex("\\w{1,15}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.registrationBtn.setOnClickListener{registerUser()}
    }

    private fun registerUser(){
        val userName = binding.resiterNameTextLayout.editText?.text.toString()
        Log.i("USERNAME", userName)
        if (validateRegistration(userName)) {
            setUserName(userName)
            finish()
        }
    }

    private fun setUserName(userName: String){
        val prefs = this.getSharedPreferences("", Context.MODE_PRIVATE)
        prefs.edit().putString(R.string.preferences_user_name.toString(), userName).apply()
    }

    private fun validateRegistration(name: String): Boolean{
        val nameLayout = binding.resiterNameTextLayout
        if(name.length <= 3){
            Log.i("username", "short")
            nameLayout.error = getString(R.string.registration_error_short)
            nameLayout.setErrorIconDrawable(0)
            return false
        }

        else if (!isValidUserName(name)) {
            Log.i("username", name)
            nameLayout.error = getString(R.string.registration_error_wrong)
            nameLayout.setErrorIconDrawable(0)
            return false
        }
        return true
    }

    private fun isValidUserName(text: String): Boolean {
        return _wordPattern.matches(text)
    }

}