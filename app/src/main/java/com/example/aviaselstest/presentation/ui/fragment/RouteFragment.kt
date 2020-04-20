package com.example.aviaselstest.presentation.ui.fragment

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.graphics.Color
import android.os.Bundle
import android.util.Property
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aviaselstest.R
import com.example.aviaselstest.presentation.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.addPolyline
import com.google.maps.android.ui.IconGenerator

class RouteFragment : Fragment(R.layout.fragment_route) {

    private val viewModel: MainViewModel by activityViewModels()

    private var map: GoogleMap? = null
    private lateinit var from: LatLng
    private lateinit var to: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    private fun hideToolbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync {
            map = it
            setupMap(map)
            drawRoute(it)
        }
    }

    private fun setupMap(map: GoogleMap?) {
        map?.uiSettings?.apply {
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isRotateGesturesEnabled = false
        }
    }

    private fun drawRoute(map: GoogleMap?) {
        val fromCity = viewModel.from
        val toCity = viewModel.to
        val fromLocation = fromCity?.location
        val toLocation = toCity?.location

        if (fromLocation != null && toLocation != null) {
            val points = arrayOf(from, to)
            from = LatLng(fromLocation.lat, fromLocation.lon)
            to = LatLng(toLocation.lat, toLocation.lon)
            addRouteMarker(map, from, fromCity.countryCode)
            addRouteMarker(map, to, toCity.countryCode)
            addPolyline(map)
            setupMapCamera(map)
            addPlaneMarker(map, points)
        }
    }

    private fun addPolyline(map: GoogleMap?) {
        val pattern = arrayListOf(Dot(), Gap(GAP_OF_ROUTE))
        map?.addPolyline {
            add(from, to)
            color(Color.BLUE)
            pattern(pattern)
            geodesic(true)
        }
    }

    private fun setupMapCamera(map: GoogleMap?) {
        val latLngBounds = LatLngBounds.Builder().apply {
            include(from)
            include(to)
        }.build()

        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, LATLNG_BOUNDS_PADDING)
        map?.moveCamera(cameraUpdate)
        map?.animateCamera(cameraUpdate)
    }

    private fun addPlaneMarker(
        map: GoogleMap?,
        points: Array<LatLng>
    ) {
        map?.addMarker {
            position(from)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
        }?.apply {
            rotation = computeHeading(from, to)
            setAnchor(0.5f, 0.5f)
            animateMarker(this, points)
            // TODO rotate marker
        }
    }

    private fun addRouteMarker(map: GoogleMap?, position: LatLng, title: String) {
        val iconGen = IconGenerator(requireContext()).apply {
            setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.location_marker))
            setTextAppearance(R.style.MarkerText)
        }
        val icon = iconGen.makeIcon(title)
        map?.addMarker {
            position(position)
            icon(BitmapDescriptorFactory.fromBitmap(icon))
            anchor(0.5f, 0.5f)
        }
    }

    private fun computeHeading(from: LatLng, to: LatLng) =
        SphericalUtil.computeHeading(from, to).toFloat()

    private fun animateMarker(marker: Marker, points: Array<LatLng>) {
        val typeEvaluator = TypeEvaluator<LatLng> { fraction: Float, from: LatLng, to: LatLng ->
            SphericalUtil.interpolate(from, to, fraction.toDouble())
        }
        val property = Property.of(Marker::class.java, LatLng::class.java, "position")
        ObjectAnimator.ofObject(marker, property, typeEvaluator, *points).apply {
            duration = DURATION_MARKER_ANIMATION
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showToolbar()
    }

    private fun showToolbar() {
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    companion object {
        private const val GAP_OF_ROUTE = 20f
        private const val LATLNG_BOUNDS_PADDING = 150
        private const val DURATION_MARKER_ANIMATION = 5000L

        fun newInstance(): RouteFragment {
            return RouteFragment()
        }
    }
}
