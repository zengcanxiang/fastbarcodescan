package cn.zengcanxiang.fastbarcodescan.core

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout


class ScanView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(
    context,
    attributeSet,
    defStyleAttr
) {

    private var provider: IScanProvider? = null

    var scanService: ScanServiceInterface? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val previewInterface = provider?.buildPreView() ?: return
        addView(previewInterface.build())

        val maskViewInterface = provider?.buildMaskLayout() ?: return
        addView(maskViewInterface.build())

        scanService = provider?.buildScanService(
            previewInterface, maskViewInterface
        )
    }

    fun setProvider(
        iProvider: IScanProvider
    ) {
        this.provider = iProvider
    }

}
