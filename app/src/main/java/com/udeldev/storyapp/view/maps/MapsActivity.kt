package com.udeldev.storyapp.view.maps

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityMapsBinding
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.view.main.MainActivity
import com.udeldev.storyapp.view.welcome.WelcomeActivity

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var activityMapsBinding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityMapsBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        if (savedInstanceState === null) {
            mapsViewModel.getSession()
        }

        mapsViewModel.token.observe(this) { token ->
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                return@observe
            }
            mapsViewModel.getAllStoryLocation()
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val denpasar = LatLng(-8.6540846, 115.212214)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(denpasar, 5f))
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style)
        )

        mapsViewModel.storyLocation.observe(this) { result ->
            when (result) {
                is Result.Failure -> {
                    if (!isFinishing) {
                        showErrorDialog(result.throwable)
                    }
                }

                is Result.Loading -> showLoading(result.state)
                is Result.Success -> {
                    result.data.listStory?.forEach { data ->
                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(data?.lat ?: 0.0, data?.lon ?: 0.0))
                                .title(data?.name)
                                .snippet(data?.description)
                        )
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.menu_maps_normal -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }

            R.id.menu_maps_satellite -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }

            R.id.menu_maps_terrain -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }

            R.id.menu_maps_hybrid -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        activityMapsBinding.progressMaps.visibility = if (isLoading) View.VISIBLE else View.GONE
        mapFragment.view?.visibility = if (isLoading) View.GONE else View.VISIBLE
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

    private fun obtainViewModel(context: Context): MapsViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[MapsViewModel::class.java]
    }

    private fun initComponent() {
        activityMapsBinding = ActivityMapsBinding.inflate(layoutInflater)
        mapsViewModel = obtainViewModel(this)
    }
}