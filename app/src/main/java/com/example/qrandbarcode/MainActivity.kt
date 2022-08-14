package com.example.qrandbarcode

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.qrandbarcode.databinding.ActivityMainBinding

private const val CAMERA_REQUEST_CODE= 101

class MainActivity : AppCompatActivity() {




    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityMainBinding

   // val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
   // val tv_scan=findViewById<TextView>(R.id.tvscan)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermission()
        Scanner()

    }

    private fun Scanner(){
        codeScanner= CodeScanner(this,binding.scannerView)
        codeScanner.apply {
            camera=CodeScanner.CAMERA_BACK
            formats=CodeScanner.ALL_FORMATS
            autoFocusMode=AutoFocusMode.SAFE
            scanMode=ScanMode.CONTINUOUS
            isAutoFocusEnabled=true
            isFlashEnabled=false

            decodeCallback= DecodeCallback {
                runOnUiThread{
                    binding.tvscan.text =it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread{
                    Log.d("Main","Camera Initialition Error:  ${it.message}")
                }

            }

        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission(){
        val permission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
             if(permission != PackageManager.PERMISSION_GRANTED){
                 makerequest()
             }
    }

    private fun makerequest(){

        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != CAMERA_REQUEST_CODE) {
            return
        }
        if (grantResults.isEmpty() ||
            grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Please Enable Camera Permission",Toast.LENGTH_LONG).show()
        } else{
            Scanner()
        }
    }


}