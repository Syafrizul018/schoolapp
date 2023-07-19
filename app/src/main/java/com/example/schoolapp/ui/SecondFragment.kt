@file:Suppress("UNUSED_EXPRESSION")

package com.example.schoolapp.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.schoolapp.R
import com.example.schoolapp.application.SchoolApp
import com.example.schoolapp.databinding.FragmentSecondBinding
import com.example.schoolapp.model.School
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener{

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val schoolViewModel: SchoolViewModel by viewModels {
        SchoolViewModelFactory((applicationContext as SchoolApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var school: School? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var  fusedLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        school = args.school
        if (school != null) {
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Ubah"
            binding.nameEditText.setText(school?.name)
            binding.addressEditText.setText(school?.address)
            binding.streetEditText.setText(school?.street)
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()


        val name = binding.nameEditText.text
        val address = binding.addressEditText.text
        val street = binding.streetEditText.text
        binding.saveButton.setOnClickListener {
            if (name.isEmpty()) {
                Toast.makeText(context, "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else if (address.isEmpty()) {
                Toast.makeText(context, "Alamat Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else if (street.isEmpty()) {
                Toast.makeText(context, "Jalan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else{

                if (school == null) {
                    val school = School(0, name.toString(), address.toString(), street.toString(), currentLatLang?.latitude, currentLatLang?.longitude)
                    schoolViewModel.insert(school)
                } else {
                    val school = School(school?.id!!, name.toString(), address.toString(), street.toString(),currentLatLang?.latitude, currentLatLang?.longitude)
                    schoolViewModel.update(school)
                }
            findNavController().popBackStack()
        }
    }
        binding.deleteButton.setOnClickListener {
            school?.let { schoolViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    private fun checkPermission(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            getCurrentLocation()
        }else{
            Toast.makeText(applicationContext, "Akses Lokasi Ditolal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null){
                    var latLang = LatLng(location.latitude, location.longitude)
                    currentLatLang = latLang
                    var title = "Marker"

                    if (school != null){
                        title = school?.name.toString()
                        val newCurrentLocation = LatLng(school?.latitude!!, school?.longitude!!)
                        latLang = newCurrentLocation
                    }
                    val markerOptions = MarkerOptions()
                        .position(latLang)
                        .title(title)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconn))
                    mMap.addMarker (markerOptions)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15f))
                }
            }
    }
}
