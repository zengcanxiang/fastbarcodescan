package cn.zengcanxiang.fastbarcodescan

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_scan.view.*

class ScanView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(
    context,
    attributeSet,
    defStyleAttr
) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_scan, this, true)
        ray_view.setFinderView(finder_view)
    }

    fun onStartScan() {
        ray_view.startScanAnimation()
    }

    fun onStopScan() {
        ray_view.stopScanAnimation()
    }
}
