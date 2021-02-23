package cn.zengcanxiang.fastbarcodescan.google

import android.content.Context
import cn.zengcanxiang.fastbarcodescan.core.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning

internal class GoogleScanService(
    override val previewInterface: PreviewInterface,
    override val maskInterface: MaskInterface
) : ScanServiceInterface {
    private var client: BarcodeScanner? = null

    private val preview by lazy {
        previewInterface as? GooglePreviewView
    }

    override fun init(
        context: Context,
        supportScanType: SupportScanType
    ) {
        val options = BarcodeScannerOptions.Builder().also {
            when (supportScanType) {
                SupportScanType.QR_CODE -> {
                    it.setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE
                    )
                }
            }
        }.build()

        client = BarcodeScanning.getClient(options)
    }

    override fun startPreview() {
        preview?.startPreview()
    }

    override fun startScan(callback: ScanResultCallback) {
        GoogleImageAnalyzer(client, callback).let {
            preview?.setImageAnalyzer(it)
        }
    }

    override fun destroy() {

    }
}
