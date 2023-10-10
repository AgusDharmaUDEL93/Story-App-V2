package com.udeldev.storyapp.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityMainBinding
import com.udeldev.storyapp.helper.adapter.LoadingStateAdapter
import com.udeldev.storyapp.helper.adapter.StoryListAdapter
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.entity.ListStoryItem
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.view.add.AddActivity
import com.udeldev.storyapp.view.maps.MapsActivity
import com.udeldev.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var adapter: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityMainBinding.root)

        activityMainBinding.recyclerMainStories.layoutManager = LinearLayoutManager(this)
        adapter = StoryListAdapter()
        activityMainBinding.recyclerMainStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        if (savedInstanceState === null) {
            mainViewModel.getSession()
        }


        mainViewModel.token.observe(this) { token ->
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                return@observe
            }
            mainViewModel.getAllStoryPaging()
        }

        mainViewModel.story.observe(this){result ->
            when(result){
                is Result.Failure -> {
                    showErrorDialog(resources.getString(R.string.error))
                }
                is Result.Loading -> showLoading(result.state)
                is Result.Success -> {
                    result.data.observe(this){
                        adapter.submitData(lifecycle, it)
                    }
                }
            }

        }


        activityMainBinding.buttonMainAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_logout -> mainViewModel.logoutSession()
            R.id.menu_main_setting -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.menu_main_maps -> startActivity(Intent(this, MapsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
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