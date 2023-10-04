package com.udeldev.storyapp.view.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityDetailBinding
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.entity.ListStoryItem
import com.udeldev.storyapp.repository.story.StoryRepository
import com.udeldev.storyapp.view.login.LoginViewModel
import com.udeldev.storyapp.view.main.MainActivity
import com.udeldev.storyapp.view.welcome.WelcomeActivity

class DetailActivity() : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityDetailBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(EXTRA_ID) ?: return

        if (savedInstanceState === null) {
            detailViewModel.getSession()

        }

        detailViewModel.token.observe(this) { token ->
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                return@observe
            }
            detailViewModel.getDetailStory(id)
        }

        detailViewModel.detailStory.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    result.data.story?.let { setDetailData(it) }

                }
                is Result.Loading -> showLoading(result.state)
                is Result.Failure -> {
                    if (!isFinishing) {
                        showErrorDialog(result.throwable)
                    }
                }
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("Oke") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun setDetailData(storyItem: ListStoryItem) {
        activityDetailBinding.textDetailTitle.text = storyItem.name
        activityDetailBinding.textDetailDesc.text = storyItem.description
        activityDetailBinding.textDetailDate.text = storyItem.createdAt
        Glide.with(this)
            .load(storyItem.photoUrl ?: "https://i.stack.imgur.com/l60Hf.png")
            .into(activityDetailBinding.imageDetailStory)
        return

    }

    private fun showLoading(isLoading: Boolean) {
        activityDetailBinding.progressDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        activityDetailBinding.detailLayoutComponent.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun initComponent() {
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        detailViewModel = obtainViewModel(this)
    }

    private fun obtainViewModel(context: Context): DetailViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}