package cn.zengcanxiang.fastbarcodescan.core

import android.content.Context
import android.graphics.Rect
import android.view.View

interface IScanProvider {
    fun buildPreView(): PreviewInterface
    fun buildMaskLayout(): MaskInterface

    fun buildScanService(
        previewInterface: PreviewInterface,
        maskInterface: MaskInterface
    ): ScanServiceInterface
}

interface PreviewInterface {
    fun build(): View
}

interface MaskInterface {
    fun build(): View
    fun scanRect(): Rect?
    fun startScan()
}

interface ScanServiceInterface {
    val previewInterface: PreviewInterface
    val maskInterface: MaskInterface

    fun init(
        context: Context,
        supportScanType: SupportScanType
    )


    fun startPreview()

    fun startScan(
        callback: ScanResultCallback
    )

    fun destroy()
}

interface ScanResultCallback {
    fun onSuccess(
        value: String
    )

    fun onFail()
}
