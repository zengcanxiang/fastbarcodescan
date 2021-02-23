package cn.zengcanxiang.fastbarcodescan.core

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper

fun ScanServiceInterface?.start(
    context: Context,
    callback: ScanResultCallback,
    supportScanType: SupportScanType = SupportScanType.QR_CODE
) {
    this?.init(context, supportScanType)
    this?.startPreview()
    this?.maskInterface?.startScan()
    this?.startScan(callback)
}

fun ScanView.start(
    context: Context,
    callback: ScanResultCallback,
    supportScanType: SupportScanType = SupportScanType.QR_CODE
) {
    if (Thread.currentThread() != Looper.getMainLooper().thread) {
        // 不是在主线程，post到主线程执行
        Handler(Looper.getMainLooper()).post {
            scanService?.start(
                context, callback, supportScanType
            )
        }
        return
    }
    scanService?.start(
        context, callback, supportScanType
    )
}

fun Bitmap.changeBitmapColor(
    color: Int
): Bitmap {
    val arrayColor = IntArray(width * height)
    var count = 0
    for (i in 0 until height) {
        for (j in 0 until width) {
            var originColor = getPixel(j, i)
            // 非透明区域
            if (originColor != 0) {
                originColor = color
            }
            arrayColor[count] = originColor
            count++
        }
    }
    return Bitmap.createBitmap(
        arrayColor,
        width, height,
        Bitmap.Config.ARGB_8888
    )
}
