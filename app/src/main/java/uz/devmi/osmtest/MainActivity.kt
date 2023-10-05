package uz.devmi.osmtest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
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
//        Configuration.getInstance().cacheMapTileCount = 12.toShort()
//        Configuration.getInstance().cacheMapTileOvershoot = 12.toShort()
        // Create a custom tile source
        // Create a custom tile source
        map.setTileSource(object : OnlineTileSourceBase("", 1, 30, 512, ".png", arrayOf("https://a.tile.openstreetmap.org/")) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return (baseUrl
                        + MapTileIndex.getZoom(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + "/" + MapTileIndex.getY(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        })

        map.setMultiTouchControls(true)
        val mapController: IMapController = map.controller
        val startPoint = GeoPoint(41.29, 69.24)
        mapController.setZoom(12.0)
        mapController.setCenter(startPoint)
        map.invalidate()
        createmarker()
    }

    private fun createmarker() {

        val originMarker = Marker(map)
        originMarker.position = GeoPoint(41.29,69.24)
        originMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        originMarker.setPanToView(true)
        originMarker.title = "Test"
        map.overlays.add(originMarker)

//        if (map == null) {
//            return
//        }
//        val my_marker = Marker(map)
//        my_marker.position = GeoPoint(4.1, 51.1)
//        my_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        my_marker.title = "Give it a title"
//        my_marker.setPanToView(true)
//        map.overlays.add(my_marker)
        map.invalidate()
    }
}