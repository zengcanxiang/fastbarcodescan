package cn.zengcanxiang.fastbarcodescan.google

import android.graphics.Bitmap
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

class DebugAnalyzer(
    val listener: ((Bitmap) -> Unit)? = null
) : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        val bitmap = image.toBitmap()
        listener?.invoke(bitmap)
        image.close()
    }

    /**
     * Helper extension function used to extract a byte array from an image plane buffer
     */
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    private fun ImageProxy.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val rgba = IntArray(width * height)
        decodeYUV420(rgba, nv21, width, height)
        return Bitmap.createBitmap(rgba, width, height, Bitmap.Config.ARGB_8888)

    }

    private fun decodeYUV420(rgba: IntArray, yuv420: ByteArray, width: Int, height: Int) {
        val frameSize = width * height
        var r: Int
        var g: Int
        var b: Int
        var y: Int
        var uvp: Int
        var u: Int
        var v: Int
        var j = 0
        var yp = 0
        while (j < height) {
            uvp = frameSize + (j shr 1) * width
            u = 0
            v = 0
            var i = 0
            while (i < width) {
                y = 0xff and yuv420[yp].toInt()
//                if (i and 1 == 0) {
//                    v = (0xff and yuv420[uvp++].toInt()) - 128
//                    u = (0xff and yuv420[uvp++].toInt()) - 128
//                }
                r = y + (1.370705f * v).toInt()
                g = y - (0.698001f * v - 0.337633f * u).toInt()
                b = y + (1.732446f * u).toInt()
                r = Math.max(0, Math.min(r, 255))
                g = Math.max(0, Math.min(g, 255))
                b = Math.max(0, Math.min(b, 255))
                rgba[yp] = Color.argb(255, r, g, b)
                i++
                yp++
            }
            j++
        }
    }
}
