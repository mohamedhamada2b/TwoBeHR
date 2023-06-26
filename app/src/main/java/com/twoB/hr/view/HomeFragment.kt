package com.twoB.hr.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.data.constants.Utilities
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.SphericalUtil
import com.twoB.hr.R
import com.twoB.hr.databinding.BottomSheetErrorLocBinding
import com.twoB.hr.databinding.FragmentHomeBinding
import com.twoB.hr.di.LocationTrack
import com.twoB.hr.viewmodel.HomeViewModel
import com.twob.domain.entity.FingerPrint
import com.twob.domain.entity.UserBranchesItem
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment(), OnLocationUpdatedListener,CustomSpinner.OnSpinnerEventsListener {
    lateinit var locationTrack: LocationTrack
    var PERMISSION_ID = 44
    var latitude:Double?=0.0
    var longitude:Double?=0.0
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    var location = Location("B")
    lateinit var branchesAdapter: BranchesAdapter
    var branches_list:List<UserBranchesItem> = ArrayList()
    lateinit var branches_spinner:CustomSpinner
    private val homeViewModel: HomeViewModel by viewModels()
    var emp_id:Int? = null
    lateinit var location_name:String
    lateinit var emp_name:String
    lateinit var branches_title:ArrayList<String>
     lateinit var  bottomSheetDialog:BottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val national_id = arguments?.getString("national_id")
        getBranches(national_id)
        branches_spinner = fragmentHomeBinding.spinnerItem
        branches_spinner.setSpinnerEventsListener(this)
        fragmentHomeBinding.progressBar.visibility = View.GONE
        fragmentHomeBinding.errorImg.visibility = View.GONE
        branchesAdapter = BranchesAdapter()
        homeViewModel.branches_live_data.observe(viewLifecycleOwner){
            set_branches(it)
        }
        homeViewModel.finger_print_live_data.observe(viewLifecycleOwner){
            set_finger_print(it)
        }
        homeViewModel.branches_title_live_data.observe(viewLifecycleOwner){
            setSpinnerData(it)
        }
        /*homeViewModel.network_live_data.observe(viewLifecycleOwner){
            create_network_connection_bottomsheet(it)
        }*/

            // call your function here
        fragmentHomeBinding.spinnerItem.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                location_name = branches_list[position].locName
                emp_id = branches_list[position].EmpID
                emp_name = branches_list[position].EmpName
                fragmentHomeBinding.txtWelcomeUser.text = emp_name
                location = Location("B")
                location.latitude = branches_list[position].LocationLatitude
                location.longitude = branches_list[position].LocationLongitude
                fragmentHomeBinding.fingerPrintImg.isClickable = false
                fragmentHomeBinding.progressBar.visibility = View.VISIBLE
                try {
                    val textView = view as TextView
                    textView.setTextColor(resources.getColor(R.color.black))
                } catch (e: java.lang.Exception) {
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


       return fragmentHomeBinding.root
    }

    private fun getBranches(nationalId: String?) {
        if (Utilities.isNetworkAvailable(context!!)){
            homeViewModel.get_branches("29212011604753")
        }else{
            createNetworkBottomSheet()
        }
    }

    private fun createNetworkBottomSheet() {
        val bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        binding.txtDesc.text = "Something went wrong,\n" +
                "Please try again!!!!"
        binding.locationImg.setImageResource(R.drawable.wrong)
        bottomSheetDialog.show()
    }

    private fun setSpinnerData(it: List<String>) {
        branchesAdapter.submitList(it)
        branches_spinner.adapter = branchesAdapter

    }

    private fun createFailedLocationBottomsheet() {
        val bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        binding.txtDesc.text = "Wait 30s to recheck location,\n" +
                "or check in & await manager confirmation."
        binding.locationImg.setImageResource(R.drawable.wrong_location)
        bottomSheetDialog.show()

    }
    private fun createSuccessLocationBottomsheet() {
        val bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        binding.txtDesc.text = "You are here,\n" +
                "Tap now to check in!! You're good to go!"
        binding.locationImg.setImageResource(R.drawable.right_location)
        bottomSheetDialog.show()
    }

    private fun set_finger_print(it: FingerPrint?) {
        bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        binding.txtDesc.text = it?.get(0).toString()
        binding.locationImg.setImageResource(R.drawable.success)
        bottomSheetDialog.show()

    }

    private fun set_branches(it: List<UserBranchesItem>?) {
        branches_list = it!!

    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            locationTrack = LocationTrack(requireContext())
            if (isLocationEnabled()) {
                    val params = LocationParams.Builder()
                        .setAccuracy(LocationAccuracy.HIGH)
                        .setDistance(1f)
                        .setInterval(5*1000)
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
        if (Utilities.isNetworkAvailable(context!!)){
            fragmentHomeBinding.progressBar.visibility = View.GONE
            latitude = currentlocation?.latitude
            longitude = currentlocation?.longitude
            var address:String?
            try {
                val addresses: List<Address>?
                val geocoder  = Geocoder(context!!, Locale.getDefault())
                addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                Log.e("latt",location.latitude.toString())
                address = addresses!![0].getAddressLine(0)
            }catch (e:Exception){
                address = ""
            }


            val point1 = LatLng(latitude!!, longitude!!)

            val point2 = LatLng(location.latitude, location.longitude)

            val distance = SphericalUtil.computeDistanceBetween(point1, point2)
            fragmentHomeBinding.fingerPrintImg.isClickable = true
            fragmentHomeBinding.locationTxt.setOnClickListener{
                fragmentHomeBinding.errorImg.visibility = View.VISIBLE
                if (distance<=350){
                    fragmentHomeBinding.errorImg.setBackgroundResource(R.drawable.success_loc)
                    createSuccessLocationBottomsheet()
                }else{
                    fragmentHomeBinding.errorImg.setBackgroundResource(R.drawable.error_loc)
                    createFailedLocationBottomsheet()
                }
            }
            add_finger_print(emp_id,address,location_name,distance,latitude!!, longitude!!)
        }

        /*val inFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inFormat.parse(input)
        val outFormat = SimpleDateFormat("EEEE")
        val goal: String = outFormat.format(date)*/
    }

    private fun add_finger_print(empId: Int?, address: String?, locationName: String, distance: Double, latitude: Double, longitude: Double) {
        fragmentHomeBinding.fingerPrintImg.setOnClickListener{
            if (Utilities.isNetworkAvailable(context!!))
                homeViewModel.add_finger_print(empId,address,locationName,distance,latitude, longitude)
            else
                createNetworkBottomSheet()
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
        homeViewModel.branches_title_live_data.removeObservers(viewLifecycleOwner)
        homeViewModel.branches_live_data.removeObservers(viewLifecycleOwner)
        //homeViewModel.finger_print_live_data.removeObservers(viewLifecycleOwner)
        SmartLocation.with(context).location().stop()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPopupWindowOpened(spinner: Spinner?) {
        fragmentHomeBinding.spinnerItem.background = resources.getDrawable(R.drawable.spinner_item_bg_up)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPopupWindowClosed(spinner: Spinner?) {
        fragmentHomeBinding.spinnerItem.background = resources.getDrawable(R.drawable.spinner_item_bg);
    }
}