package com.davidcombita.data.models

import com.google.gson.annotations.SerializedName

data class Material (
    val idCategory: Long,
    val idMaterial: Long,
    val nameProduct: String,
    val nameBrand: String,
    val quantity: Long,
    val units: Long,
    val unitValue: Long
)