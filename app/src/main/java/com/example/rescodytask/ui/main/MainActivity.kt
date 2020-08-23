package com.example.rescodytask.ui.main

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.rescodytask.R
import com.example.rescodytask.databinding.ActivityMainBinding
import com.example.rescodytask.ui.adapter.MessagesAdapter
import com.example.rescodytask.ui.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() , OnMapReadyCallback {
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: MessagesAdapter
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        adapter = MessagesAdapter(this, MessagesAdapter.MessageClickListener {
            if(it.location != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.location,5f))
        } )
        binding.recyclerView.adapter = adapter
    }
    override fun onMapReady(googleMap: GoogleMap) {
        handleFeeds()

        mMap = googleMap


    }

    private fun handleFeeds() {
        mainViewModel.getFeeds()
        mainViewModel.isLoading.observe(this, androidx.lifecycle.Observer {
            Log.e("Test", "Loading " + it)
        })
        mainViewModel.errorMessage.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        mainViewModel.feedResponse.observe(this, androidx.lifecycle.Observer {
            Log.e("Test", "Reponse:  " + it)
            it.forEach {
                val latLng = getGeo(it.title)
                it.location  = latLng
                if(latLng != null)
                AddMarker(latLng,it.title)
            }
            adapter.submitList(it)
        })
    }

    private fun AddMarker(Location: LatLng, title :String) {
        mMap.addMarker(MarkerOptions().position(Location).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Location))
    }

    fun getGeo(strAddress :String): LatLng? {
        val geocoder = Geocoder(this, Locale.getDefault())
        var p1: LatLng? = null
        try {
            val addresses = geocoder.getFromLocationName(strAddress, 5) ?: return null;
            if(!addresses.isEmpty()){
                val location: Address = addresses.get(0)
                p1 = LatLng(location.getLatitude(), location.getLongitude())
                return p1
            }else{
                return null
            }

        } catch (e: IOException) {
            return null;
        }

        return null
    }

}