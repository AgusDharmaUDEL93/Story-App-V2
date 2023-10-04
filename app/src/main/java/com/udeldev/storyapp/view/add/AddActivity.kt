package com.udeldev.storyapp.view.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private var myLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityAddBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState === null) {
            addViewModel.getSession()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        activityAddBinding.buttonAddGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        activityAddBinding.buttonAddCamera.setOnClickListener {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }

        addViewModel.token.observe(this) { token ->
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                return@observe
            }
        }

        activityAddBinding.switchAddLocation.setOnCheckedChangeListener(){ _, isChecked ->
            if (isChecked){
                getMyLastLocation()
                Log.i("AddActivity", "Lat : ${myLocation?.latitude}, Long : ${myLocation?.longitude}")
                return@setOnCheckedChangeListener
            }
        }

        addViewModel.response.observe(this) { result ->
            when (result) {
                is Result.Failure -> showDialog(resources.getString(R.string.error), result.throwable)
                is Result.Loading -> showLoading(result.state)
                is Result.Success -> showDialog(
                    resources.getString(R.string.success),
                    result.data.message ?: resources.getString(R.string.success_post_story)
                )
            }
        }

        activityAddBinding.buttonAddSubmit.setOnClickListener {
            if (currentImageUri == null) {
                Toast.makeText(this, resources.getString(R.string.no_image), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (activityAddBinding.editAddDesc.text.toString().isEmpty()) {
                activityAddBinding.editAddDescLayout.error = resources.getString(R.string.desc_empty)
                return@setOnClickListener
            }

            Log.i("AddActivity", "Lat : ${myLocation?.latitude} Lon : ${myLocation?.longitude}")

            currentImageUri?.let {
                val imageFile = uriToFile(it, this).reduceFileImage()
                val description = activityAddBinding.editAddDesc.text.toString()
                val lat = myLocation?.latitude
                val lon = myLocation?.longitude

                addViewModel.postMultiPart(
                    MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        imageFile.asRequestBody("image/jpeg".toMediaType())
                    ),
                    description.toRequestBody("text/plain".toMediaType()),
                    lat,
                    lon
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
        if (uri != null) {
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
            }
        }

    private fun getMyLastLocation()  {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    myLocation = location
                    Toast.makeText(this, "Location found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                Toast.makeText(
                    this,
                    "Location is not found. Try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean) {
        activityAddBinding.progressAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
        activityAddBinding.addLayoutComponent.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showDialog(title: String, message: String) {
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
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(context: Context): AddViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[AddViewModel::class.java]
    }

    private fun initComponent() {
        activityAddBinding = ActivityAddBinding.inflate(layoutInflater)
        addViewModel = obtainViewModel(this)
    }

}