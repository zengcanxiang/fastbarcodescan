package cn.zengcanxiang.fastbarcodescan.core.helper

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import cn.zengcanxiang.fastbarcodescan.core.MaskInterface
import cn.zengcanxiang.fastbarcodescan.core.R
import kotlinx.android.synthetic.main.layout_simple_mask.view.*

class SimpleMaskLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(
    context,
    attributeSet,
    defStyleAttr
), MaskInterface {

    init {
        LayoutInflater.from(
            context
        ).inflate(
            R.layout.layout_simple_mask,
            this
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rayView.setFinderView(
            finderView
        )
    }

    override fun build(): View {
        return this
    }

    override fun scanRect(): Rect? {
//        val dm = resources.displayMetrics
//        val density = dm.density
//        val mScreenWidth = resources.displayMetrics.widthPixels
//        val mScreenHeight = resources.displayMetrics.heightPixels
//        val SCAN_FRAME_SIZE = 300
//        val scanFrameSize = (SCAN_FRAME_SIZE * density).toInt()
//        val rect = Rect()
//        rect.left = mScreenWidth / 2 - scanFrameSize / 2
//        rect.right = mScreenWidth / 2 + scanFrameSize / 2
//        rect.top = mScreenHeight / 2 - scanFrameSize / 2
//        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2

        val location = IntArray(2)
        rayView.getLocationOnScreen(location)
        return Rect(
            location[0],
            location[1],
            location[0] + rayView.width,
            location[1] + rayView.height
        )
    }

    override fun startScan() {
        rayView.startScanAnimation()
    }
}
