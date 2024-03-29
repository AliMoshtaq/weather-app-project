package co.develhope.meteoapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.develhope.meteoapp.R
import co.develhope.meteoapp.databinding.ActivityMainBinding
import co.develhope.meteoapp.model.ForecastModel
import co.develhope.meteoapp.utility.LOCATION_REQUEST
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.threeten.bp.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        binding.bottomNavigation.setOnItemSelectedListener {
            setBottomBarClick(it)
        }
        updateMenuItems()
        replaceFragment(HomeScrFragment())
    }

    private fun updateMenuItems() {
        val today = LocalDate.now().dayOfMonth
        val tomorrow = LocalDate.now().plusDays(1).dayOfMonth

        val todayMenuItem = binding.bottomNavigation.menu.findItem(R.id.today_fragment)
        val tomorrowMenuItem = binding.bottomNavigation.menu.findItem(R.id.tomorrow_fragment)

        val todayIcon = todayMenuItem.icon
        val tomorrowIcon = tomorrowMenuItem.icon

        val todayBadge = createBadgeDrawable(this, today.toString())
        val tomorrowBadge = createBadgeDrawable(this, tomorrow.toString())

        val todayLayerDrawable = LayerDrawable(arrayOf(todayIcon, todayBadge))
        val tomorrowLayerDrawable = LayerDrawable(arrayOf(tomorrowIcon, tomorrowBadge))

        todayMenuItem.icon = todayLayerDrawable
        tomorrowMenuItem.icon = tomorrowLayerDrawable
    }

    @SuppressLint("InflateParams")
    private fun createBadgeDrawable(context: Context, count: String): Drawable {
        val badge = LayoutInflater.from(context).inflate(R.layout.menu_badge, null)
        val text = badge.findViewById<TextView>(R.id.badge_text)
        text.text = count

        badge.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        badge.layout(0, 0, badge.measuredWidth, badge.measuredHeight)

        val bitmap = Bitmap.createBitmap(
            badge.measuredWidth, badge.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        badge.draw(canvas)

        return BitmapDrawable(context.resources, bitmap)
    }

    private fun setBottomBarClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_fragment -> replaceFragment(HomeScrFragment())
            R.id.search_fragment -> replaceFragment(SearchScrFragment())
            R.id.today_fragment -> {
                ForecastModel.getSelectedDayDetails(0)
                    ?.let { selectedDay -> ForecastModel.forecastDetails(selectedDay) }
                replaceFragment(DailyScrFragment())
            }
            R.id.tomorrow_fragment -> {
                ForecastModel.getSelectedDayDetails(1)
                    ?.let { selectedDay -> ForecastModel.forecastDetails(selectedDay) }
                replaceFragment(DailyScrFragment())
            }
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                // Final latitude and longitude code here
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }

                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(this, "Null Received", Toast.LENGTH_SHORT).show()
                    } else {
                        // Fetch weather for current location
                        fetchCurrentWeatherLocation()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun fetchCurrentWeatherLocation() {
        // Fetch weather data based on latitude and longitude
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_REQUEST
        )
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}