package com.davidcombita.data.models

data class TattoResponse (
    val idTattoo: Int,
    val size: String,
    val style: String,
    val stateTatto: String,
    val spectrum: String,
    val descriptionTatto: String,
    val price: Double,
    val resource: List<Resource>
)

data class Resource (
    val idTattoo: Long,
    val idResource: Long,
    val descResource: String,
    val urlImage: String,
    val typeResource: String
)