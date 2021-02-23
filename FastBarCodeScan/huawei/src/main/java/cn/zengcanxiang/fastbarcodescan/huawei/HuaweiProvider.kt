package cn.zengcanxiang.fastbarcodescan.huawei

import android.app.Activity
import cn.zengcanxiang.fastbarcodescan.core.*
import cn.zengcanxiang.fastbarcodescan.core.helper.SimpleMaskLayout

class HuaweiProvider(
    private val activity: Activity
) : IScanProvider {
    private val preview by lazy {
        HuaweiPreview(
            activity
        )
    }

    private val maskView by lazy {
        SimpleMaskLayout(
            activity
        )
    }

    private val huaweiScanService by lazy {
        HuaweiScanService(
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
        return huaweiScanService
    }
}
