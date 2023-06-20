package com.twoB.hr.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.SphericalUtil
import com.twoB.hr.viewmodel.HomeViewModel
import com.twoB.hr.R
import com.twoB.hr.databinding.BottomSheetErrorLocBinding
import com.twoB.hr.databinding.FragmentHomeBinding
import com.twoB.hr.di.LocationTrack
import com.twob.domain.entity.FingerPrint
import com.twob.domain.entity.UserBranchesItem
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment(), OnLocationUpdatedListener {
    lateinit var locationTrack: LocationTrack
    var PERMISSION_ID = 44
    var latitude:Double?=0.0
    var longitude:Double?=0.0
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var location: Location
    private val homeViewModel: HomeViewModel by viewModels()
    var emp_id:Int? = null
    lateinit var location_name:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel.get_branches("29212011604753")
        homeViewModel.branches_live_data.observe(viewLifecycleOwner){
            set_branches(it)
        }
        homeViewModel.finger_print_live_data.observe(viewLifecycleOwner){
            set_finger_print(it)
        }

       return fragmentHomeBinding.root
    }

    private fun set_finger_print(it: FingerPrint?) {
        val bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        binding.txtDesc.text = it?.get(0).toString()
        if(it?.get(0).toString() == "تم التوقيع بنجاح"){
        binding.locationImg.setImageResource(R.drawable.success)
        }else{
            binding.locationImg.setImageResource(R.drawable.wrong)
        }
        bottomSheetDialog.show()

    }

    private fun set_branches(it: List<UserBranchesItem>?) {
        emp_id = it?.get(5)?.EmpID
        location_name = it?.get(5)!!.locName
        fragmentHomeBinding.txtWelcomeUser.text = it[5].EmpName
        location  = Location("B")
        location.latitude = it[5].LocationLatitude
        location.longitude = it[5].LocationLongitude
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            locationTrack = LocationTrack(requireContext())
            if (isLocationEnabled()) {
                    val params = LocationParams.Builder()
                        .setAccuracy(LocationAccuracy.HIGH)
                        .setDistance(1f)
                        .setInterval((5 * 1000).toLong())
                        .build()
                    val smart = SmartLocation.Builder(requireContext()).logging(true).build()
                    smart.location().config(params).start(this)

            } else {
                Toast.makeText(context, "الرجاء فتح تحديد الموقع الخاص بك", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context!!, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onLocationUpdated(currentlocation: Location?) {
        latitude = currentlocation?.latitude
        longitude = currentlocation?.longitude
        val addresses: List<Address>?
        val geocoder  = Geocoder(context!!, Locale.getDefault())
        addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)


        val address = addresses!![0].getAddressLine(0)

        val point1 = LatLng(latitude!!, longitude!!)

        val point2 = LatLng(location.latitude, location.longitude)

        val distance = SphericalUtil.computeDistanceBetween(point1, point2)

        add_finger_print(emp_id,address,location_name,distance,
                             latitude!!, longitude!!)


    }

    private fun add_finger_print(empId: Int?, address: String?, locationName: String, distance: Double, latitude: Double, longitude: Double) {
        fragmentHomeBinding.fingerPrintImg.setOnClickListener{

            homeViewModel.add_finger_print(empId,address,locationName,distance,latitude, longitude)

        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onStop() {
        super.onStop()
        SmartLocation.with(context).location().stop();
    }
}