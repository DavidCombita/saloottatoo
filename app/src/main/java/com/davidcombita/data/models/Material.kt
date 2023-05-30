package com.davidcombita.data.models

import java.io.Serializable

data class Material (
    val idCategory: Long,
    var idMaterial: Long,
    val nameProduct: String,
    val nameBrand: String,
    val quantity: Long,
    val units: Long,
    val unitValue: Long,
    val auxUnits: Long
): Serializable