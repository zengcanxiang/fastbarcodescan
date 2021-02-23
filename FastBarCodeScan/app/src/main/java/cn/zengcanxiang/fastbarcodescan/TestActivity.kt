package cn.zengcanxiang.fastbarcodescan

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.zengcanxiang.fastbarcodescan.alibaba.AlibabaProvider
import cn.zengcanxiang.fastbarcodescan.core.ScanResultCallback
import cn.zengcanxiang.fastbarcodescan.core.start
import cn.zengcanxiang.fastbarcodescan.google.GoogleProvider
import cn.zengcanxiang.fastbarcodescan.huawei.HuaweiProvider
import kotlinx.android.synthetic.main.test.*

class TestActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val scanResultCallback by lazy {
        object : ScanResultCallback {
            override fun onSuccess(value: String) {
                Log.e("TAGzzzz", value)
                handler.post {
                    Toast.makeText(
                        this@TestActivity,
                        value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFail() {
            }
        }
    }

    private val googleProvider by lazy {
        GoogleProvider(
            this,
            this
        )
    }

    private val huaweiProvider by lazy {
        HuaweiProvider(this)
    }

    private val alibabaProvider by lazy {
        AlibabaProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        barcodeScanView.setProvider(
            GoogleProvider(
                this,
                this
            )
        )
        start.setOnClickListener {
            barcodeScanView.start(
                this,
                scanResultCallback
            )
        }

    }
}
