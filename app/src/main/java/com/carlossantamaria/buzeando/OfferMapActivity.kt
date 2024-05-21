package com.carlossantamaria.buzeando

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.carlossantamaria.buzeando.objects.Offer
import com.carlossantamaria.buzeando.objects.Waypoint
import com.carlossantamaria.buzeando.utils.LoadOffersFromDb
import com.carlossantamaria.buzeando.utils.PermissionUtils
import com.carlossantamaria.buzeando.utils.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.carlossantamaria.buzeando.utils.PermissionUtils.isPermissionGranted
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class OfferMapActivity : AppCompatActivity(),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnInfoWindowClickListener,
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var btnListaOfertas: Button
    private lateinit var btnCuenta: Button
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private val listaCoordenadas = mutableListOf<Waypoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_offer_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnListaOfertas = findViewById(R.id.btnListaOfertas)
        btnCuenta = findViewById(R.id.btnCuenta)

        btnListaOfertas.setOnClickListener { abrirListaOfertas() }
        btnCuenta.setOnClickListener { abrirCuenta() }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun abrirCuenta() {
        val intent = Intent(this, AccountActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnInfoWindowClickListener(this)
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        LoadOffersFromDb.cargarOfertas(this) { offerList ->
            if (offerList.isNotEmpty()) {
                offerList.forEach {
                    listaCoordenadas.add(Waypoint(it, it.titulo, it.descripcion, LatLng(it.coordsLat, it.coordsLong)))
                }
                // Add markers after listaCoordenadas is populated
                listaCoordenadas.forEach {
                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(it.coordenadas)
                            .title(it.titulo)
                            .snippet("${it.descripcion.substring(0, it.descripcion.length.coerceAtMost(25))}...")
                    )
                    marker?.tag = it.oferta
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onInfoWindowClick(marker: Marker) {
        val offer = marker.tag as Offer
        abrirDetallesOferta(offer)
    }

    private fun abrirDetallesOferta(offer: Offer) {
        val intent = Intent(this, OfferDetailsActivity::class.java)
        intent.putExtra("offer_object", offer)
        startActivity(intent)
    }
}