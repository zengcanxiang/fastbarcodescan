package cn.zengcanxiang.fastbarcodescan

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*

class MainActivity : AppCompatActivity() {
//    private val bqcScanCallback: BQCScanCallback = object : BQCScanCallback {
//        override fun onParametersSetted(pcode: Long) {
//            runOnUiThread {
//                service.setDisplay(textureView)
//                cameraHandler?.onSurfaceViewAvailable()
//                val maScanEngineService: MaScanEngineService = MaScanEngineServiceImpl()
//                service.regScanEngine(
//                    "MA",
//                    maScanEngineService.engineClazz,
//                    object : MaScanCallback {
//                        override fun onResultMa(p0: MultiMaScanResult?) {
//                            p0 ?: return
//                            Log.e("TAG", p0.maScanResults[0].text)
//                        }
//                    }
//                )
//                service.setScanType("MA")
//                service.isScanEnable = true
//            }
//        }
//
//        override fun onSurfaceAvaliable() {
//            cameraHandler?.onSurfaceViewAvailable()
//        }
//
//        override fun onPreviewFrameShow() {
//            val scanRect = Rect(
//                0, 0, textureView.width, textureView.height
//            )
//            Log.e("TAG", "${scanRect.width()}")
//            service.setScanRegion(scanRect)
//        }
//
//        override fun onError(bqcError: BQCScanError) {
//        }
//
//        override fun onCameraOpened() {}
//        override fun onCameraAutoFocus(success: Boolean) {}
//        override fun onOuterEnvDetected(shouldShow: Boolean) {}
//        override fun onCameraReady() {}
//        override fun onCameraClose() {}
//    }
//
//    val service by lazy {
//        MPaasScanServiceImpl().also {
//            it.serviceInit()
//            it.setEngineParameters(null)
//
//        }
//    }
//
//    private var cameraHandler: CameraHandler? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_test)
//        cameraHandler = service.cameraHandler
//        service.cameraHandler.init(this, bqcScanCallback)
//        service.cameraHandler.openCamera()
//
//    }

//    override fun onResume() {
//        super.onResume()
//        barCodeScanView.start()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        barCodeScanView.stop()
//    }

}
