package com.example.qrcodereader

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openQrCodeActivity()
    }

    private fun openQrCodeActivity() {
        buttonScanner.setOnClickListener {
            val intent = Intent(this, QrCodeActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            processBarcodeResult(it.getStringExtra("barcode"), it.getStringExtra("format"))
        }
    }

    private fun processBarcodeResult(text: String, barcodeFormatName: String) {
        val result = Result(text, text.toByteArray(), arrayOf(), BarcodeFormat.valueOf(barcodeFormatName))
        Toast.makeText(this, "${result.text} - ${result.barcodeFormat}", Toast.LENGTH_SHORT).show()
    }
}
