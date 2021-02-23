package cn.zengcanxiang.fastbarcodescan.core.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatImageView

/**
 * 负责绘制扫描射线
 */
class SimpleRayView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(
    context,
    attributeSet,
    defStyleAttr
) {
    private var mSimpleFinderView: SimpleFinderView? = null
    private val scanAnimation by lazy {
        ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f).also {
            it.duration = 3000L
            it.fillAfter = true
            it.repeatCount = Animation.INFINITE
            it.interpolator = AccelerateDecelerateInterpolator()
        }
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        mSimpleFinderView?.setScanWindowLocation(
            left,
            top,
            right,
            bottom
        )
    }

    fun startScanAnimation() {
        visibility = View.VISIBLE
        startAnimation(scanAnimation)
    }

    fun stopScanAnimation() {
        visibility = View.INVISIBLE
        clearAnimation()
    }

    fun setFinderView(SimpleFinderView: SimpleFinderView?) {
        mSimpleFinderView = SimpleFinderView
    }
}
