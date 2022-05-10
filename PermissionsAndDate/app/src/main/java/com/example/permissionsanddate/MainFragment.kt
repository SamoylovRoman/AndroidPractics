package com.example.permissionsanddate

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.permissionsanddate.adapter.TypeAdapter
import com.example.permissionsanddate.databinding.FragmentMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import kotlin.random.Random

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var isFineLocationPermissionGranted: Boolean = false

    private var locations: List<Type> = listOf()
    private var typeAdapter: TypeAdapter? = null

    private var rationaleDialog: AlertDialog? = null
    private var updateApiDialog: AlertDialog? = null

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                isFineLocationPermissionGranted = true
                checkFineLocationPermission()
            } else {
                isFineLocationPermissionGranted = false
                showToast(getString(R.string.text_permission_not_approved))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        if (savedInstanceState != null) {
            locations =
                savedInstanceState.getParcelableArrayList<Type.Location>(LOCATIONS_LIST_TAG) as List<Type.Location>
            isFineLocationPermissionGranted = savedInstanceState.getBoolean(FINE_LOC_IS_GRANTED)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonListener()
        initLocationsList()
        checkFineLocationPermission()
    }

    private fun initLocationsList() {
        typeAdapter = TypeAdapter { position -> editItem(position) }
        typeAdapter?.items = locations
        binding.locationsList.adapter = typeAdapter
        binding.locationsList.layoutManager = LinearLayoutManager(context)
        binding.locationsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun addNewLocation(newLocation: Type.Location?) {
        if (newLocation != null) {
            locations = listOf(newLocation) + locations
            typeAdapter?.items = locations
            binding.locationsList.scrollToPosition(0)
            binding.mainTextView.visibility = View.GONE
            binding.locationsList.visibility = View.VISIBLE
        }
    }

    private fun editItem(position: Int) {
        showPickerDialog(position)
    }

    private fun showPickerDialog(position: Int) {
        val selectedDateTime = locations[position].createdAt.atZone(ZoneId.systemDefault())
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hour, minute ->
                        updateLocationDateTime(position, year, month + 1, dayOfMonth, hour, minute)
                    },
                    selectedDateTime.hour,
                    selectedDateTime.minute,
                    true
                ).show()
            },
            selectedDateTime.year,
            selectedDateTime.month.value - 1,
            selectedDateTime.dayOfMonth
        )
            .show()
    }

    private fun updateLocationDateTime(
        position: Int,
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hour: Int,
        minute: Int
    ) {
        val zonedDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute)
            .atZone(ZoneId.systemDefault())
        val updatedLocation =
            (locations[position] as Type.Location).copy(createdAt = zonedDateTime.toInstant())
        locations = (locations - locations[position]).toMutableList()
            .apply {
                add(position, updatedLocation)
            }.toList()
        typeAdapter?.items = locations
    }

    private fun initButtonListener() {
        binding.allowButton.setOnClickListener {
            if (isFineLocationPermissionGranted) {
                if (checkGoogleApiAvailability()) {
                    setLocationServicesListener()
                }
            } else {
                requestFineLocationPermission()
            }
        }
    }

    private fun checkGoogleApiAvailability(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        when (val resultCode = apiAvailability.isGooglePlayServicesAvailable(requireContext())) {
            ConnectionResult.SUCCESS -> return true
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {
                showUpdateApiDialog()
            }
            else -> {
                val installApiDialog = apiAvailability.getErrorDialog(
                    this,
                    resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
                installApiDialog?.setCancelable(false)
                installApiDialog?.show()
            }
        }
        return false
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun setLocationServicesListener() {
        LocationServices.getFusedLocationProviderClient(requireContext())
            .lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val newLocation = Type.Location(
                        id = Random.nextLong(Long.MAX_VALUE),
                        createdAt = Instant.now(),
                        latitude = location.latitude,
                        longitude = location.longitude,
                        speed = location.speed,
                        imageLink = Type.Location.getRandomImageLink()
                    )
                    addNewLocation(newLocation)
                } else showToast(getString(R.string.text_no_location))
            }
            .addOnCanceledListener {
                showToast(getString(R.string.text_location_canceled))
            }
            .addOnFailureListener {
                showToast(getString(R.string.text_location_failed))
            }
    }

    private fun checkFineLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                if (locations.isEmpty()) {
                    binding.mainTextView.setText(R.string.text_empty_list)
                } else {
                    binding.locationsList.visibility = View.VISIBLE
                }
                binding.allowButton.setText(R.string.text_button_get_location)
                isFineLocationPermissionGranted = true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                binding.mainTextView.setText(R.string.text_main_text)
                binding.allowButton.setText(R.string.text_button_allow)
                showLocationRationaleDialog()
            }
            else -> {
                binding.mainTextView.setText(R.string.text_main_text)
                binding.allowButton.setText(R.string.text_button_allow)
            }
        }
    }

    private fun showLocationRationaleDialog() {
        rationaleDialog = AlertDialog.Builder(requireContext())
            .setMessage(R.string.text_rationale_dialog)
            .setPositiveButton(getString(R.string.text_ok)) { _, _ -> requestFineLocationPermission() }
            .setNegativeButton(getString(R.string.text_cancel), null)
            .show()
    }

    private fun showUpdateApiDialog() {
        updateApiDialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.text_need_to_update_google_api))
            .setPositiveButton(getString(R.string.text_update_api)) { _, _ ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_PLAY_SERVICES_LINK)
                    )
                )
            }
            .show()
    }

    private fun requestFineLocationPermission() {
        permReqLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val locationsList = locations.toCollection(ArrayList())
        outState.putParcelableArrayList(LOCATIONS_LIST_TAG, locationsList)
        outState.putBoolean(FINE_LOC_IS_GRANTED, isFineLocationPermissionGranted)
    }

    companion object {
        private const val LOCATIONS_LIST_TAG = "Locations list"
        private const val FINE_LOC_IS_GRANTED = "IsFineLocationPermissionGranted value"
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9002
        private const val GOOGLE_PLAY_SERVICES_LINK = "market://play.google.com/store/apps/details?id=com.google.android.gm&hl=en"

        fun newInstance() = MainFragment()
    }
}