package com.udeldev.storyapp.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityMainBinding
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var activityMainBinding: ActivityMainBinding
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityMainBinding.root)

        if (savedInstanceState === null) {
            mainViewModel.getSession()
            mainViewModel.getAllStory()
        }


        mainViewModel.token.observe(this) { token ->
            if (token.isEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            Log.i("MainActivity", token)
        }


        activityMainBinding.logout.setOnClickListener {
            mainViewModel.logoutSession()
        }

        mainViewModel.story.observe(this) { resources ->
            when (resources) {
                is Result.Success -> {
                    activityMainBinding.testi.text = resources.data.listStory.toString()
                }
                is Result.Loading -> showLoading(resources.state)
                is Result.Failure -> showErrorDialog(resources.throwable)
            }


        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("Oke") { _, _ ->
                val intent = Intent(context, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        Log.i("Loading", isLoading.toString())
        activityMainBinding.progressMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun initComponent() {
        mainViewModel = obtainViewModel(this)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    }

    private fun obtainViewModel(context: Context): MainViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}