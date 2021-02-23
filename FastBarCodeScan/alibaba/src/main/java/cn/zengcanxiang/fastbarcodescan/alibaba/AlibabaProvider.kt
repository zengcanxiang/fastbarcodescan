package cn.zengcanxiang.fastbarcodescan.alibaba

import android.content.Context
import cn.zengcanxiang.fastbarcodescan.core.*
import cn.zengcanxiang.fastbarcodescan.core.helper.SimpleMaskLayout
import com.alipay.mobile.common.logging.api.LoggerFactory
import java.lang.Exception

class AlibabaProvider(
    private val context: Context
) : IScanProvider {
    init {
        try {
            LoggerFactory.init(
                context.applicationContext
            )
        } catch (e: Exception) {
        }
    }

    private val preview by lazy {
        AlibabaPreview(
            context
        )
    }

    private val maskView by lazy {
        SimpleMaskLayout(
            context
        )
    }

    private val alibabaScanService by lazy {
        // 这里调用方法不用变量，是因为，方法可以重写。
        AlibabaScanService(
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
