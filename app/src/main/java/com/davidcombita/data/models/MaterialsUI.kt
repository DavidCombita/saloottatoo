package com.davidcombita.data.models

data class MaterialsUI (
    val loading: Boolean = false,
    val materialInfo: List<Material> = emptyList(),
    val error: Boolean = false
)

data class CategoriesUI (
    val loading: Boolean = false,
    val materialInfo: List<Categories> = emptyList(),
    val error: Boolean = false
)

data class TattosUI (
    val loading: Boolean = false,
    val tattoInfo: List<TattoResponse> = emptyList(),
    val error: Boolean = false
)
data class TattosMaterialUI (
    val loading: Boolean = false,
    val tattoMaterialInfo: List<MaterialsTatto> = emptyList(),
    val error: Boolean = false
)