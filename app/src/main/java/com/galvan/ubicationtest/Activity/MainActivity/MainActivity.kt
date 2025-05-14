package com.galvan.ubicationtest.Activity.MainActivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.ade.accessControl.manager.PermissionsManager
import com.galvan.ubicationtest.Activity.SplashActivity.SplashActivity
import com.galvan.ubicationtest.R
import com.galvan.ubicationtest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Init()
        ObserveLocation()
    }

    private fun ObserveLocation() {
        lifecycleScope.launchWhenStarted {
            viewModel.locationFlowViewModel.collect { location ->
                location?.let {

                }
            }
        }
    }

    private fun Init(){
        if(!PermissionsManager(this).arePermissionsGranted()){
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
            return
        }
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        window.statusBarColor = Color.parseColor("#2962FF")
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        window.statusBarColor = Color.WHITE
        windowInsetsController.isAppearanceLightStatusBars = false
    }


}