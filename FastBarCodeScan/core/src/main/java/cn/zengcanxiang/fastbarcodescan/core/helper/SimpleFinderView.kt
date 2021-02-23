package cn.zengcanxiang.fastbarcodescan.core.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import cn.zengcanxiang.fastbarcodescan.core.R
import cn.zengcanxiang.fastbarcodescan.core.changeBitmapColor

/**
 * 负责绘制扫码窗口、边角及周边阴影
 */
class SimpleFinderView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attributeSet,
    defStyleAttr
) {
    private var scanWindowLeft = 0
    private var scanWindowTop = 0
    private var scanWindowRight = 0
    private var scanWindowBottom = 0
    private var leftTopCorner: Bitmap? = null
    private var rightTopCorner: Bitmap? = null
    private var leftBottomCorner: Bitmap? = null
    private var rightBottomCorner: Bitmap? = null
    private var paint = Paint()
    private var shadowColor = 0

    init {
        applyConfig(context, attributeSet)
        visibility = INVISIBLE
        initCornerBitmap(context)
        paint.isAntiAlias = true
    }

    private fun applyConfig(
        context: Context,
        attrs: AttributeSet?
    ) {
        shadowColor =
            DEFAULT_SHADOW_COLOR
    }

    private fun initCornerBitmap(context: Context) {
        val res = context.resources
        leftTopCorner = BitmapFactory.decodeResource(
            res,
            R.drawable.scan_window_corner_left_top
        )
        rightTopCorner = BitmapFactory.decodeResource(
            res,
            R.drawable.scan_window_corner_right_top
        )
        leftBottomCorner = BitmapFactory.decodeResource(
            res,
            R.drawable.scan_window_corner_left_bottom
        )
        rightBottomCorner = BitmapFactory.decodeResource(
            res,
            R.drawable.scan_window_corner_right_bottom
        )
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        drawShadow(canvas)
        drawCorner(canvas)
    }

    private fun drawCorner(canvas: Canvas) {
        // 画4个角
        paint.alpha = 255
        leftTopCorner?.let {
            canvas.drawBitmap(
                it,
                scanWindowLeft.toFloat(),
                scanWindowTop.toFloat(),
                paint
            )
        }
        rightTopCorner?.let {
            canvas.drawBitmap(
                it,
                scanWindowRight - it.width.toFloat(),
                scanWindowTop.toFloat(),
                paint
            )
        }
        leftBottomCorner?.let {
            canvas.drawBitmap(
                it,
                scanWindowLeft.toFloat(),
                scanWindowBottom - it.height.toFloat(),
                paint
            )
        }
        rightBottomCorner?.let {
            canvas.drawBitmap(
                it,
                scanWindowRight - it.width.toFloat(),
                scanWindowBottom - it.height.toFloat(),
                paint
            )
        }
    }

    private fun drawShadow(canvas: Canvas) {
        // 画带透明的背景
        paint.color = shadowColor
        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            scanWindowTop.toFloat(),
            paint
        )
        canvas.drawRect(
            0f,
            scanWindowTop.toFloat(),
            scanWindowLeft.toFloat(),
            scanWindowBottom.toFloat(),
            paint
        )
        canvas.drawRect(
            scanWindowRight.toFloat(),
            scanWindowTop.toFloat(),
            width.toFloat(),
            scanWindowBottom.toFloat(),
            paint
        )
        canvas.drawRect(
            0f,
            scanWindowBottom.toFloat(),
            width.toFloat(),
            height.toFloat(),
            paint
        )
    }

    /**
     * 根据 RayView 的位置决定扫码窗口的位置
     */
    fun setScanWindowLocation(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        scanWindowLeft = left
        scanWindowTop = top
        scanWindowRight = right
        scanWindowBottom = bottom
        invalidate()
        visibility = VISIBLE
    }

    fun setShadowColor(shadowColor: Int) {
        this.shadowColor = shadowColor
    }

    fun setCornerColor(angleColor: Int) {
        leftTopCorner = leftBottomCorner?.changeBitmapColor(angleColor)
        rightTopCorner = rightTopCorner?.changeBitmapColor(angleColor)
        leftBottomCorner = leftBottomCorner?.changeBitmapColor(angleColor)
        rightBottomCorner = rightBottomCorner?.changeBitmapColor(angleColor)
    }

    companion object {
        private const val DEFAULT_SHADOW_COLOR = -0x6a000000
    }
}
