package cn.zengcanxiang.fastbarcodescan.alibaba

import android.content.Context
import cn.zengcanxiang.fastbarcodescan.core.*

internal class AlibabaScanService(
    override val previewInterface: PreviewInterface,
    override val maskInterface: MaskInterface
) : ScanServiceInterface {

    private val scanHandler by lazy {
        ScanHandler()
    }

    private var mContext: Context? = null

    override fun init(
        context: Context,
        supportScanType: SupportScanType
    ) {
        mContext = context
        scanHandler.supportScanType = supportScanType
    }

    override fun startPreview() {
        (previewInterface as? AlibabaPreview)?.let {
            scanHandler.display = it
        }

        mContext?.let {
            scanHandler.startPreview(it)
        }
    }

    override fun startScan(
        callback: ScanResultCallback
    ) {
        scanHandler.callback = callback
        maskInterface.scanRect()?.let {
            scanHandler.setupScanRect(
                it,
                previewInterface.build().width,
                previewInterface.build().height
            )
        }
        scanHandler.isScanEnable = true
    }

    override fun destroy() {
        scanHandler.destroy()
    }

}
