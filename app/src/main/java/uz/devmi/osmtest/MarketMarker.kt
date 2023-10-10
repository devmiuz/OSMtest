package uz.devmi.osmtest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.abs
import kotlin.math.max

class MarketMarker(
    private val mapView: MapView,
    private val pointColor: String = Points.GREY,
    private val identifier: String,
    private val title: String,
    private val titleColor: String = "#000000",
    private val descriptionList: List<Pair<String, String>>,
    private val onClick: (String) -> Unit
) : Marker(mapView, mapView.context) {

    init {
        setOnMarkerClickListener { marker, mapView ->
            onClick(identifier)
            false
        }
        generateImage()
    }

    fun generateImage() {
        val bitmap = generateBitmap(mapView.context, title, titleColor, descriptionList, pointColor)
        val bitmapDrawable = BitmapDrawable(mResources, bitmap)
        super.setIcon(bitmapDrawable)
    }

    private fun generateBitmap(
        context: Context,
        title: String,
        titleColor: String,
        descriptionList: List<Pair<String, String>>,
        pointColor: String = Points.GREY
    ): Bitmap {
        val displayDensity = context.resources.displayMetrics.density
        val textPaint = Paint().apply {
            textSize = 11 * displayDensity
            textAlign = Paint.Align.LEFT
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }
        val backgroundPaint = Paint().apply {
            isAntiAlias = true
            this.color = Color.argb(180, 255, 255, 255)
        }
        val descriptionText = descriptionList.joinToString(separator = "") { it.first }
        val titleWidth = textPaint.measureText(title ?: "")
        val titleHeight = abs(textPaint.fontMetrics.bottom) + abs(textPaint.fontMetrics.top)

        val currencyWidth = if (descriptionText.isEmpty()) 0f else textPaint.measureText(descriptionText)
        val currencyHeight = if (descriptionText.isEmpty()) 0f else
            abs(textPaint.fontMetrics.bottom) + abs(textPaint.fontMetrics.top)

        val maxWidth = max(titleWidth, currencyWidth)
        val center = maxWidth / 2
        val bitmap = Bitmap.createBitmap(
            (maxWidth + center + 100).toInt(),
            (2 * (titleHeight) + currencyHeight).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawRoundRect(
            RectF(center, 0f, maxWidth + center + 100, 2f * (titleHeight) + currencyHeight),
            center, center, backgroundPaint
        )
        val centerHeight = (2f * titleHeight + currencyHeight) / 2
        backgroundPaint.color = Color.WHITE
        canvas.drawCircle(center, centerHeight, titleHeight, backgroundPaint)
        backgroundPaint.color = Color.parseColor(pointColor)
        canvas.drawCircle(center, centerHeight, titleHeight * 0.7f, backgroundPaint)
        var startWidth = center + 50
        textPaint.color = Color.parseColor(titleColor)
        canvas.drawText(title, startWidth, 1.2f * titleHeight, textPaint)
        val height = 2.2f * titleHeight
        descriptionList.forEach {
            val text = it.first
            textPaint.color = Color.parseColor(it.second)
            canvas.drawText(text, startWidth, height, textPaint)
            startWidth += textPaint.measureText(text) + 5
        }
        return bitmap
    }
}
