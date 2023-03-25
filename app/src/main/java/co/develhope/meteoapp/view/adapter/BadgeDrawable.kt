package co.develhope.meteoapp.view.adapter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import co.develhope.meteoapp.R

class BadgeDrawable(private val context: Context, private val text: String) : Drawable() {

    private val badgePaint: Paint = Paint()
    private val textPaint: Paint = Paint()

    init {
        badgePaint.color = Color.RED
        badgePaint.style = Paint.Style.FILL
        badgePaint.isAntiAlias = true

        textPaint.color = Color.WHITE
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.textSize = context.resources.getDimensionPixelSize(R.dimen.badge_text_size).toFloat()
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
    }
    fun setCountBounds(count: String) {
        // Add your implementation to set the bounds based on the count text
        val width = intrinsicWidth
        val height = intrinsicHeight
        setBounds(0, 0, width, height)
    }


    override fun draw(canvas: Canvas) {
        val bounds = bounds

        val circleRadius = bounds.height() / 3f
        val centerX = bounds.width() - circleRadius

        canvas.drawCircle(centerX, circleRadius, circleRadius, badgePaint)

        val textY = circleRadius - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(text, centerX, textY, textPaint)
    }

    override fun setAlpha(alpha: Int) {
        badgePaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        badgePaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
