package cn.zengcanxiang.fastbarcodescan.huawei

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import cn.zengcanxiang.fastbarcodescan.core.PreviewInterface
import cn.zengcanxiang.fastbarcodescan.core.ScanResultCallback
import cn.zengcanxiang.fastbarcodescan.core.ScanServiceInterface
import cn.zengcanxiang.fastbarcodescan.core.SupportScanType
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan


internal class HuaweiPreview(
    private val mContext: Activity
) : LinearLayout(
    mContext
), PreviewInterface {

    private var scanRect: Rect? = null

    private val remoteView by lazy {
        RemoteView.Builder()
            .setContext(
                mContext
            ).also {
                it?.setBoundingBox(
                    scanRect
                )
            }.setFormat(
                HmsScan.ALL_SCAN_TYPE
            ).build()
    }

    override fun build(): View {
        return this
    }

    fun startPreview() {
        addView(remoteView)
        remoteView?.onCreate(null)
        remoteView?.onResume()
        remoteView?.onStart()
    }

    fun onDestroy() {
        remoteView?.onStop()
        remoteView?.onDestroy()
    }

    fun setupScanRect(
        rect: Rect
    ) {
        scanRect = rect
    }

    fun startScan(
        supportScanType: SupportScanType,
        callback: ScanResultCallback
    ) {
        remoteView?.setOnResultCallback {
            if (it.isEmpty()) {
                callback.onFail()
                return@setOnResultCallback
            }
            val supportType = when (supportScanType) {
                SupportScanType.QR_CODE -> {
                    HmsScan.QRCODE_SCAN_TYPE
                }
                else -> {
                    -1
                }
            }
            val item = it[0]
            if (item.scanType == supportType) {
                callback.onSuccess(item.originalValue)
            }
        }
    }

}
