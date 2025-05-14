package com.galvan.ubicationtest.Activity.MainActivity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import com.ade.accessControl.manager.PermissionsManager
import com.galvan.ubicationtest.Activity.SplashActivity.SplashActivity
import com.galvan.ubicationtest.Data.POO.Ubication
import com.galvan.ubicationtest.R
import com.galvan.ubicationtest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn
import kotlin.getValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // ViewModel con Hilt
    private val viewModel: MainActivityViewModel by viewModels()

    // ViewBinding para acceder a las vistas
    private lateinit var binding: ActivityMainBinding

    // Variables para guardar la ubicación actual
    private var latitud: Double? = null
    private var longitud: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Init() // Inicializa UI, listeners y permisos
        ObserveLocation() // Observa ubicación actual del ViewModel
    }

    // Observa cambios en la ubicación desde el ViewModel
    private fun ObserveLocation() {
        lifecycleScope.launchWhenStarted {
            viewModel.locationFlowViewModel.collect { location ->
                location?.let {
                    latitud = it.latitude
                    longitud = it.longitude
                    binding.currentLocation.text = "Ubicacion Actual:\n${latitud}, ${longitud}"
                }
            }
        }
    }

    // Inicializa UI, estilos, validación de permisos y botones
    private fun Init() {
        // Si no hay permisos, vuelve a la pantalla de permisos
        if (!PermissionsManager(this).arePermissionsGranted()) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
            return
        }

        // Inflar layout y mostrarlo
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar color de la barra de estado
        window.statusBarColor = Color.parseColor("#2962FF")
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        window.statusBarColor = Color.WHITE
        windowInsetsController.isAppearanceLightStatusBars = false

        validationUbication() // Validar coordenadas en campos
        ActionButtom() // Configurar listeners de botones
    }

    // Valida coordenadas ingresadas por el usuario en tiempo real
    private fun validationUbication() {
        // Observa cambios en los campos y valida
        binding.acetLatitud.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarCoordenadas(binding.acetLatitud, binding.acetLongitud, binding.btnMaps1)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.acetLongitud.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarCoordenadas(binding.acetLatitud, binding.acetLongitud, binding.btnMaps1)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.acetLatitud2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarCoordenadas(binding.acetLatitud, binding.acetLongitud, binding.btnMaps1)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.acetLongitud2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarCoordenadas(binding.acetLatitud2, binding.acetLongitud2, binding.btnMaps2)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Valida si ambas coordenadas son válidas y muestra/oculta el botón correspondiente
    private fun validarCoordenadas(
        btn1: AppCompatEditText,
        btn2: AppCompatEditText,
        btnVisible: Button
    ) {
        val latStr = btn1.text.toString()
        val lonStr = btn2.text.toString()

        val isLatValid = isLatitudValida(latStr)
        val isLonValid = isLongitudValida(lonStr)

        btnVisible.visibility = if (isLatValid && isLonValid) View.VISIBLE else View.GONE
    }

    // Comprueba si la latitud está dentro del rango válido
    private fun isLatitudValida(valor: String): Boolean {
        return try {
            val lat = valor.toDouble()
            lat in -90.0..90.0
        } catch (e: Exception) {
            false
        }
    }

    // Comprueba si la longitud está dentro del rango válido
    private fun isLongitudValida(valor: String): Boolean {
        return try {
            val lon = valor.toDouble()
            lon in -180.0..180.0
        } catch (e: Exception) {
            false
        }
    }

    // Abre Google Maps con una coordenada desde los EditText
    private fun showGoogleMaps(appCompatLat: AppCompatEditText, appCompatLon: AppCompatEditText) {
        val lat = appCompatLat.text.toString().toDouble()
        val lon = appCompatLon.text.toString().toDouble()
        val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    // Configura qué hace cada botón al hacer clic
    private fun ActionButtom() {
        binding.btnMaps1.setOnClickListener {
            showGoogleMaps(binding.acetLatitud, binding.acetLongitud)
        }

        binding.btnMaps2.setOnClickListener {
            showGoogleMaps(binding.acetLatitud2, binding.acetLongitud2)
        }

        binding.btnCalculate.setOnClickListener {
            if (checkDistance().isNotEmpty()) {
                showResult("${getString(R.string.validationDistanceresult)} ${checkDistance()}")
            }
        }
    }

    // Calcula la distancia entre la ubicación actual y otro punto
    fun distancePoints(lat2: Double, lon2: Double): Double {
        // Radio de la Tierra en metros (constante usada en la fórmula de Haversine)
        val radioTierra = 6371000.0

        // Diferencia de latitud en radianes (convierte la diferencia a radianes)
        val dLat = Math.toRadians(lat2 - latitud!!)

        // Diferencia de longitud en radianes
        val dLon = Math.toRadians(lon2 - longitud!!)

        /**
         * Fórmula de Haversine:
         * Calcula el valor intermedio 'a', que representa la distancia angular entre dos puntos
         * en una esfera (usando trigonometría esférica).
         *
         * - sin²(dLat/2): variación de latitud
         * - sin²(dLon/2): variación de longitud
         * - cos(lat1) * cos(lat2): ajuste por curvatura de la Tierra
         */
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(latitud!!)) *
                cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        /**
         * Calcula el ángulo central 'c' usando la fórmula de atan2 (más precisa que acos o asin)
         * Este ángulo se multiplica por el radio de la Tierra para obtener la distancia real
         */
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Devuelve la distancia en metros
        return radioTierra * c
    }


    // Compara las distancias a dos ubicaciones y devuelve el nombre de la más cercana
    fun checkDistance(): String {
        return try {
            // Objeto para Sacar Coordenadas
            val ubication1 = Ubication("Ubicacion 1", binding.acetLatitud, binding.acetLongitud)
            val ubication2 = Ubication("Ubicacion 2", binding.acetLatitud2, binding.acetLongitud2)

            val distance1 = distancePoints(ubication1.latitud, ubication1.longitude)
            val distance2 = distancePoints(ubication2.latitud, ubication2.longitude)

            if (distance1 < distance2) ubication1.name else ubication2.name
        } catch (e: Exception) {
            e.printStackTrace()
            showError(getString(R.string.validationDistance))
            ""
        }
    }

    // Muestra resultado en un TextView
    private fun showResult(message: String) {
        hideError()
        binding.resultView.visibility = View.VISIBLE
        binding.resultView.text = message
    }

    // Oculta el resultado
    private fun hideResult() {
        binding.resultView.visibility = View.GONE
    }

    // Muestra error en pantalla
    private fun showError(message: String) {
        hideResult()
        binding.errorView.visibility = View.VISIBLE
        binding.errorView.text = message
    }

    // Oculta el error
    private fun hideError() {
        binding.errorView.visibility = View.GONE
    }
}


