package de.chiarapolice.unserprojekt20

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import de.chiarapolice.unserprojekt20.Inventar.AddProductActivity
import de.chiarapolice.unserprojekt20.R.layout.activity_barcode_scanner
import kotlinx.android.synthetic.main.activity_barcode_scanner.*

//Code ist aus diesem Video https://youtu.be/DFwSBUb6wA8
class BarcodeScanner : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_barcode_scanner)

        findViewById<TextView>(R.id.textScanResult)
        findViewById<Button>(R.id.add_btn)

        add_btn.setOnClickListener {

            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("Code", textScanResult.text)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(
                this@BarcodeScanner, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(this@BarcodeScanner).build()
        cameraSource =
            CameraSource.Builder(this@BarcodeScanner, detector).setAutoFocusEnabled(true).build()
        cameraSurfaceView.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@BarcodeScanner, arrayOf(Manifest.permission.CAMERA), requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Berechtigungen verweigert", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback {

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource.stop()
        }

        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        this@BarcodeScanner,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cameraSource.start(surfaceHolder)
            } catch (exception: Exception) {
                Toast.makeText(applicationContext, "Etwas ist schiefgelaufen", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val processor = object : Detector.Processor<Barcode> {

        override fun release() {

        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if (detections != null && detections.detectedItems.isNotEmpty()) {
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                textScanResult.text = code.displayValue
            } else {
                textScanResult.text = ""
            }
        }
    }
}