package com.udeldev.storyapp.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityLoginBinding
import com.udeldev.storyapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityLoginBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityLoginBinding.textButtonLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun initComponent(){
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
    }


}