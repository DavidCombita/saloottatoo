package com.davidcombita.data.models

import java.io.Serializable

data class MaterialsTatto (
    val idCategory: Long,
    val idMaterial: Long,
    val nameProduct: String,
    val unitValue: Long): Serializable