package cn.zengcanxiang.fastbarcodescan.alibaba

import android.content.Context
import android.graphics.Rect
import android.hardware.Camera
import android.os.Handler
import android.os.Looper
import android.view.TextureView
import cn.zengcanxiang.fastbarcodescan.core.SupportScanType
import cn.zengcanxiang.fastbarcodescan.core.ScanResultCallback
import com.alipay.mobile.bqcscanservice.impl.MPaasScanServiceImpl
import com.alipay.mobile.mascanengine.MaScanCallback
import com.alipay.mobile.mascanengine.MaScanEngineService
import com.alipay.mobile.mascanengine.MultiMaScanResult
import com.alipay.mobile.mascanengine.impl.MaScanEngineServiceImpl

internal class ScanHandler {
    private val scanHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private var releaseCode = -1L
    private val scanCallback by lazy {
        object : SimpleCallback() {
            override fun onParametersSetted(p0: Long) {
                super.onParametersSetted(p0)
                releaseCode = p0
                display?.let {
                    service.setDisplay(it)
                }
            }

            override fun onPreviewFrameShow() {
                //TODO 先扫描识别全屏 不只识别框里面的内容
                val scanRect = Rect(
                    0, 0, display?.width ?: 0, display?.height ?: 0
                )
                service.setScanRegion(scanRect)
            }

            override fun onSurfaceAvaliable() {
                service.cameraHandler.onSurfaceViewAvailable()
            }
        }
    }
    private val service by lazy {
        MPaasScanServiceImpl().also {
            it.serviceInit()
            it.setEngineParameters(null)
        }
    }

    var supportScanType: SupportScanType = SupportScanType.QR_CODE

    var isScanEnable = false
        set(value) {
            field = value
            scanHandler.post {
                switchScanEnable()
            }
        }

    var display: TextureView? = null
    var callback: ScanResultCallback? = null

    fun startPreview(
        context: Context
    ) {
        service.cameraHandler.init(context, scanCallback)
        service.cameraHandler.openCamera()
    }

    fun setupScanRect(
        region: Rect,
        previewWidth: Int,
        previewHeight: Int
    ) {
        this.previewWidth = previewWidth
        this.previewHeight = previewHeight
        this.oldRegion = region
    }

    private var previewWidth = 0
    private var previewHeight = 0
    private var oldRegion: Rect? = null

    private fun processRect(
        camera: Camera,
        oldRegion: Rect
    ): Rect {

        val rayWidth = oldRegion.right - oldRegion.left
        val rayHeight = oldRegion.bottom - oldRegion.top

        val size: Camera.Size = try {
            camera.parameters.previewSize
        } catch (e: Exception) {
            return oldRegion
        }

        val rateX = size.height.toDouble() / previewWidth
        val rateY = size.width.toDouble() / previewHeight
        val expandX = (rayWidth * 0.05)
        val expandY = (rayHeight * 0.05)

        val resRect = Rect(
            ((oldRegion.top - expandY) * rateY).toInt(),
            ((oldRegion.left - expandX) * rateX).toInt(),
            ((oldRegion.bottom + expandY) * rateY).toInt(),
            ((oldRegion.right + expandX) * rateX).toInt()
        )

        val finalRect = Rect(
            if (resRect.left < 0) 0 else resRect.left,
            if (resRect.top < 0) 0 else resRect.top,
            if (resRect.width() > size.width) size.width else resRect.width(),
            if (resRect.height() > size.height) size.height else resRect.height()
        )

        val rect1 = Rect(
            finalRect.left / 4 * 4,
            finalRect.top / 4 * 4,
            finalRect.right / 4 * 4,
            finalRect.bottom / 4 * 4
        )

        val max = Math.max(rect1.right, rect1.bottom)
        val diff = Math.abs(rect1.right - rect1.bottom) / 8 * 4

        val rect2: Rect
        rect2 = if (rect1.right > rect1.bottom) {
            Rect(rect1.left, rect1.top - diff, max, max)
        } else {
            Rect(rect1.left - diff, rect1.top, max, max)
        }
        return rect2
    }

    fun destroy() {
        display = null
        service.isScanEnable = false
        service.cameraHandler.postCloseCamera()
        service.cameraHandler.release(releaseCode)
        service.cameraHandler.destroy()
    }

    private fun switchScanEnable() {
        val maScanEngineService: MaScanEngineService = MaScanEngineServiceImpl()

        service.regScanEngine(
            "MA",
            maScanEngineService.engineClazz,
            object : MaScanCallback {
                override fun onResultMa(p0: MultiMaScanResult?) {
                    p0 ?: return
                    callback?.onSuccess(p0.maScanResults[0].text)
                }
            }
        )
        service.setScanType(
            "MA"
        )

        service.isScanEnable = isScanEnable
    }
}
