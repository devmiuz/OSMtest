package uz.devmi.osmtest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


class MainActivity : AppCompatActivity() {
    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map = findViewById<View>(R.id.map) as MapView
        map.tileProvider.clearTileCache()
        Configuration.getInstance().cacheMapTileCount = 12.toShort()
        Configuration.getInstance().cacheMapTileOvershoot = 12.toShort()


        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setMultiTouchControls(true)
        val mapController: IMapController = map.controller
        val startPoint = GeoPoint(41.29, 69.24)
        mapController.setZoom(12.0)
        mapController.setCenter(startPoint)
        map.invalidate()
        createmarker()
    }

    private fun createmarker() {

        val originMarker = CustomMarker(
            mapView = map,
            identifier = "1111",
            title = "AAA",
            pointColor = Points.GREY,
            descriptionList = listOf("1 000 USD" to "#02E767", "500 000 UZS" to "#E66767")
        ) {

        }
        originMarker.position = GeoPoint(41.30, 69.20)
        originMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        originMarker.setPanToView(true)
        originMarker.generateImage()

        map.overlays.add(originMarker)

        val originMarker1 = CustomMarker(
            mapView = map,
            title = "BBB",
            identifier = "1111",
            pointColor = Points.GREEN,
            descriptionList = listOf("200 000 UZS" to "#02E767", "50 USD" to "#02E767")
        ) {

        }
        originMarker1.position = GeoPoint(41.25, 69.10)
        originMarker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        originMarker1.setPanToView(true)
        originMarker1.generateImage()
        map.overlays.add(originMarker1)

        val originMarker2 = CustomMarker(
            mapView = map,
            identifier = "2222",
            title = "CCC",
            pointColor = Points.RED,
            descriptionList = listOf("200 000 UZS" to "#E66767", "500 000 USD" to "#E66767")
        ) {
            map.controller.animateTo(GeoPoint(41.20, 69.00))
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()

        }
        originMarker2.position = GeoPoint(41.20, 69.00)
        originMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        originMarker2.setPanToView(true)
        originMarker2.generateImage()
        map.overlays.add(originMarker2)

        map.invalidate()
    }
}