package cn.zengcanxiang.fastbarcodescan.google

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import cn.zengcanxiang.fastbarcodescan.core.*
import cn.zengcanxiang.fastbarcodescan.core.helper.SimpleMaskLayout

class GoogleProvider(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner? = context as? LifecycleOwner
) : IScanProvider {
    private val preview by lazy {
        GooglePreviewView(
            context,
            lifecycleOwner
        )
    }

    private val maskView by lazy {
        SimpleMaskLayout(
            context
        )
    }

    private val alibabaScanService by lazy {
        // 这里调用方法不用变量，是因为，方法可以重写。
        GoogleScanService(
            buildPreView(),
            buildMaskLayout()
        )
    }

    override fun buildPreView(): PreviewInterface {
        return preview
    }

    override fun buildMaskLayout(): MaskInterface {
        return maskView
    }

    override fun buildScanService(
        previewInterface: PreviewInterface,
        maskInterface: MaskInterface
    ): ScanServiceInterface {
        return alibabaScanService
    }


}
