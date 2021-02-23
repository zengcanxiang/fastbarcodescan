package cn.zengcanxiang.fastbarcodescan.alibaba

import com.alipay.mobile.bqcscanservice.BQCScanCallback
import com.alipay.mobile.bqcscanservice.BQCScanError

internal abstract class SimpleCallback : BQCScanCallback {
    override fun onSurfaceAvaliable() {
    }

    override fun onCameraAutoFocus(p0: Boolean) {
    }

    override fun onPreviewFrameShow() {
    }

    override fun onCameraClose() {
    }

    override fun onOuterEnvDetected(p0: Boolean) {
    }

    override fun onParametersSetted(p0: Long) {
    }

    override fun onError(p0: BQCScanError?) {
    }

    override fun onCameraOpened() {
    }

    override fun onCameraReady() {
    }

}
