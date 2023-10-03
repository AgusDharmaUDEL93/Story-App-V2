package com.udeldev.storyapp.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityWelcomeBinding
import com.udeldev.storyapp.view.login.LoginActivity
import com.udeldev.storyapp.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var activityWelcomeBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityWelcomeBinding.root)

        activityWelcomeBinding.buttonWelcomeLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        activityWelcomeBinding.buttonWelcomeRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_welcome_languange -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_welcome, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initComponent(){
        activityWelcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
    }
}