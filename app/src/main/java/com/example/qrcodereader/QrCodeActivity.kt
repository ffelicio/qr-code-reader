package com.example.qrcodereader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.qrcodereader.util.isCameraStarted
import com.example.qrcodereader.util.startCameraForAllDevices
import com.example.qrcodereader.util.stopCameraForAllDevices
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_qr_code.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class QrCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler, EasyPermissions.PermissionCallbacks {

    companion object {
        private val REQUEST_CODE_CAMERA : Int = 182
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)

        askCameraPermission()
    }

    override fun onResume() {
        super.onResume()

        z_xing_scanner.setResultHandler(this)
        restartCameraIfInactive()
    }

    override fun onPause() {
        super.onPause()
        z_xing_scanner.stopCameraForAllDevices()
    }

    override fun handleResult(result: Result?) {
        result?.let {
            processBarcodeResult(it)
        }
    }

    private fun processBarcodeResult(result: Result) {
        val text = result.text
        val barcodeFormatName = result.barcodeFormat.name

        val intent = Intent()
        intent.putExtra("barcode", text)
        intent.putExtra("format", barcodeFormatName)
        finish(intent, Activity.RESULT_OK)
    }

    fun finish(intent: Intent, resultAction: Int) {
        setResult( resultAction, intent )
        finish()
    }

    private fun restartCameraIfInactive() {
        if( !z_xing_scanner.isCameraStarted() ) {
            startCamera()
        }
    }

    private fun askCameraPermission() {
        EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)
                        .setRationale("Teste")
                        .setPositiveButtonText("OK")
                        .setNegativeButtonText("Cancelar")
                        .build())
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        askCameraPermission()
    }

    private fun startCamera() {
        z_xing_scanner.startCameraForAllDevices(this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startCamera()
    }
}