package cn.zengcanxiang.fastbarcodescan.google

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import cn.zengcanxiang.fastbarcodescan.core.ScanResultCallback
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage

internal class GoogleImageAnalyzer(
    private val client: BarcodeScanner?,
    private val callback: ScanResultCallback
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )
            client?.process(
                image
            )?.addOnSuccessListener {
                for (barcode in it) {
                    when (barcode.valueType) {
                        Barcode.TYPE_TEXT -> {
                            barcode.displayValue?.let { value ->
                                callback.onSuccess(value)
                            }
                        }
                    }
                }
                mediaImage.close()
                imageProxy.close()
            }?.addOnFailureListener {
                callback.onFail()
                mediaImage.close()
                imageProxy.close()
            }
        }
    }
}
