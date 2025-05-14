package com.ade.accessControl.manager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.galvan.ubicationtest.Activity.MainActivity.MainActivity
import com.galvan.ubicationtest.R

class PermissionsManager(private val activity: Activity) {

    private val _alert = MutableLiveData<Boolean>()
    val alert: LiveData<Boolean> get() = _alert
    private val PERMISSION_REQUEST_CODE = 123
    private val REQUEST_BLUETOOTH_PERMISSIONS = 124
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )


    fun arePermissionsGranted(): Boolean {
        // Verificar permisos de almacenamiento si la versión es <= Android 9 (P)
        val storagePermissionsGranted = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).all { permission ->
                ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            true // No se requieren estos permisos en versiones más recientes
        }

        // Verificar permisos generales
        val generalPermissionsGranted = REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
        Log.d("Permisos", "Permisos generales: $generalPermissionsGranted")

//        // Verificar permisos Bluetooth si el SDK es >= S (API 31)
//        val bluetoothPermissionsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            listOf(
//                Manifest.permission.FOREGROUND_SERVICE
//            ).all { permission ->
//                ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
//            }
//        } else {
//            true // No se requieren en versiones anteriores
//        }
//        Log.d("Permisos", "Permisos Bluetooth: $bluetoothPermissionsGranted")

        // Retornar verdadero si todos los permisos están concedidos
        return generalPermissionsGranted && storagePermissionsGranted
    }


    // Solicita permisos si es necesario
    fun checkAndRequestPermissions() {


        val permissionsNeeded = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        val storageManager = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }
        } else {
            emptyList()
        }

        val bluetoothPermissionsNeeded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ).filter {
                ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
            }
        } else {
            emptyList()
        }



        if (permissionsNeeded.isNotEmpty() || bluetoothPermissionsNeeded.isNotEmpty() || storageManager.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                (permissionsNeeded + bluetoothPermissionsNeeded + storageManager).toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("NewApi")
    fun handlePermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val deniedPermissions = mutableListOf<String>()
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    println("${permissions[i]} granted")
                } else {
                    deniedPermissions.add(permissions[i])
                    println("${permissions[i]} denied")
                }
            }


            if (deniedPermissions.isNotEmpty() && !Environment.isExternalStorageManager()) {
                _alert.postValue(true)
                val alerta = AlertDialog.Builder(activity)
                alerta.setTitle("ADVERTENCIA")
                    .setMessage(activity.getString(R.string.permiss))
                    .setPositiveButton("OK") { dialog, _ -> activity.finish() }
                alerta.show()
            } else {
                val intent = Intent(activity, MainActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }
}
