package com.galvan.ubicationtest.Data.POO

import androidx.appcompat.widget.AppCompatEditText

//// Objeto personalizado para representar una ubicación basada en datos de dos EditText
class Ubication(val name: String,private val appCompatLat: AppCompatEditText,private val appCompatLon: AppCompatEditText){

    val latitud = appCompatLat.text.toString().toDouble()
    val longitude = appCompatLon.text.toString().toDouble()
}