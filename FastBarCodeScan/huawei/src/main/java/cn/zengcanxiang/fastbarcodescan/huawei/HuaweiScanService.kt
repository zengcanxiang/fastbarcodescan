package cn.zengcanxiang.fastbarcodescan.huawei

import android.content.Context
import cn.zengcanxiang.fastbarcodescan.core.*
import com.huawei.hms.ml.scan.HmsScan

class HuaweiScanService(
    override val previewInterface: PreviewInterface,
    override val maskInterface: MaskInterface
) : ScanServiceInterface {
    private val remoteView by lazy {
        previewInterface as? HuaweiPreview
    }

    private var supportScanType: SupportScanType? = null

    override fun init(
        context: Context,
        supportScanType: SupportScanType
    ) {
        this.supportScanType = supportScanType
    }

    override fun startPreview() {
        maskInterface.scanRect()?.let {
            remoteView?.setupScanRect(it)
        }
        remoteView?.startPreview()
    }

    override fun startScan(callback: ScanResultCallback) {
        supportScanType?.let {
            remoteView?.startScan(it, callback)
        }
    }

    override fun destroy() {
        remoteView?.onDestroy()
    }

}
