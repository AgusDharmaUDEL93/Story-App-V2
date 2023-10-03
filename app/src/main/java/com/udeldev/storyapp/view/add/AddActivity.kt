package com.udeldev.storyapp.view.add

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityAddBinding
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.ImageUtils.Companion.getImageUri
import com.udeldev.storyapp.helper.utils.ImageUtils.Companion.reduceFileImage
import com.udeldev.storyapp.helper.utils.ImageUtils.Companion.uriToFile
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.view.detail.DetailViewModel
import com.udeldev.storyapp.view.main.MainActivity
import com.udeldev.storyapp.view.welcome.WelcomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddActivity : AppCompatActivity() {

    private lateinit var activityAddBinding: ActivityAddBinding
    private lateinit var addViewModel: AddViewModel

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityAddBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState === null){
            addViewModel.getSession()
        }

        activityAddBinding.buttonAddGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        activityAddBinding.buttonAddCamera.setOnClickListener {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }

        addViewModel.token.observe(this){token ->
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                return@observe
            }
        }

        addViewModel.response.observe(this){result ->
            when (result){
                is Result.Failure -> showDialog(resources.getString(R.string.error), result.throwable)
                is Result.Loading -> showLoading(result.state)
                is Result.Success -> showDialog(resources.getString(R.string.success), result.data.message ?: resources.getString(R.string.success_post_story))
            }
        }

        activityAddBinding.buttonAddSubmit.setOnClickListener {
            if (currentImageUri == null){
                Toast.makeText(this, resources.getString(R.string.no_image), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (activityAddBinding.editAddDesc.text.toString().isEmpty()){
                activityAddBinding.editAddDescLayout.error = resources.getString(R.string.desc_empty)
                return@setOnClickListener
            }

            currentImageUri?.let {
                val imageFile = uriToFile(it, this).reduceFileImage()
                val description = activityAddBinding.editAddDesc.text.toString()

                addViewModel.postMultiPart(
                    MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        imageFile.asRequestBody("image/jpeg".toMediaType())
                    ),
                    description.toRequestBody("text/plain".toMediaType())
                )
            }
        }
    }

    // Camera
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
            return@registerForActivityResult
        }
        Toast.makeText(this, resources.getString(R.string.cannot_get_camera), Toast.LENGTH_SHORT)
    }

    // Gallery
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
            return@registerForActivityResult
        }
        Toast.makeText(this, resources.getString(R.string.no_media_selected), Toast.LENGTH_SHORT)
    }

    // Showing Image
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            activityAddBinding.imageAddStory.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityAddBinding.progressAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
        activityAddBinding.addLayoutComponent.visibility =  if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showDialog(title : String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(context: Context): AddViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[AddViewModel::class.java]
    }

    private fun initComponent(){
        activityAddBinding = ActivityAddBinding.inflate(layoutInflater)
        addViewModel = obtainViewModel(this)
    }

}