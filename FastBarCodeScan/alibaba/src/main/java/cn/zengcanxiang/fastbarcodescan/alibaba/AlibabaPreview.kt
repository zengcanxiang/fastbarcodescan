package cn.zengcanxiang.fastbarcodescan.alibaba

import android.content.Context
import android.view.TextureView
import android.view.View
import cn.zengcanxiang.fastbarcodescan.core.PreviewInterface

internal class AlibabaPreview(
    private val mContext: Context
) : TextureView(
    mContext
), PreviewInterface {
    override fun build(): View {
        return this
    }
}
