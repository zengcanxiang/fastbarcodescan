package cn.zengcanxiang.fastbarcodescan.google

import android.content.Context
import android.os.Debug
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import cn.zengcanxiang.fastbarcodescan.core.PreviewInterface
import kotlinx.android.synthetic.main.view_camera_preview.view.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

internal class GooglePreviewView @JvmOverloads constructor(
    private val mContext: Context,
    private val lifecycleOwner: LifecycleOwner? = mContext as? LifecycleOwner,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(
    mContext,
    attributeSet,
    defStyleAttr
), PreviewInterface {

    private var cameraProvider: ProcessCameraProvider? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    private val cameraExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

    init {
        LayoutInflater.from(
            mContext
        ).inflate(
            R.layout.view_camera_preview, this
        )
        layoutParams = ViewGroup.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    fun startPreview(
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(
            mContext
        )
        cameraProviderFuture.addListener(
            Runnable {
                cameraProvider = cameraProviderFuture.get()
                lensFacing = when {
                    hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                    hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                    else -> throw IllegalStateException("Back and front camera are unavailable")
                }
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(mContext)
        )
    }

    fun setImageAnalyzer(
        analyzer: ImageAnalysis.Analyzer
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(
            mContext
        )
        cameraProviderFuture.addListener(
            Runnable {
                cameraProvider = cameraProviderFuture.get()
                lensFacing = when {
                    hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                    hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                    else -> throw IllegalStateException("Back and front camera are unavailable")
                }
                bindCameraUseCases(analyzer)
            },
            ContextCompat.getMainExecutor(mContext)
        )
    }

    private fun bindCameraUseCases(
        analyzer: ImageAnalysis.Analyzer? = null
    ) {
        val metrics = DisplayMetrics().also {
            cameraPreviewView.display.getRealMetrics(it)
        }
        val screenAspectRatio = aspectRatio(
            metrics.widthPixels,
            metrics.heightPixels
        )
        val rotation = cameraPreviewView.display.rotation
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(
                lensFacing
            )
            .build()

        val preview = Preview.Builder()
            .setTargetAspectRatio(
                screenAspectRatio
            )
            .setTargetRotation(
                rotation
            )
            .build()

        val imageAnalysis = analyzer?.let {
            ImageAnalysis.Builder().setTargetAspectRatio(
                screenAspectRatio
            ).setTargetRotation(
                rotation
            ).setBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            ).build().also { analysis ->
                analysis.setAnalyzer(cameraExecutor, it)
            }
        }
        cameraProvider.unbindAll()
        try {
            val lifecycleOwner = lifecycleOwner ?: return
            val useCases = mutableListOf<UseCase>()
            useCases.add(preview)
            if (imageAnalysis != null) {
                useCases.add(imageAnalysis)
            }
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, *useCases.toTypedArray()
            )
            preview.setSurfaceProvider(
                cameraPreviewView.surfaceProvider
            )
        } catch (exc: Exception) {
        }
    }

    private fun aspectRatio(
        width: Int,
        height: Int
    ): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    override fun build(): View {
        return this
    }
}
