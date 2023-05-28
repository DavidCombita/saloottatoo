package com.davidcombita.repository

import com.davidcombita.data.api.ApiMaterialService
import com.davidcombita.data.models.Categories
import com.davidcombita.data.models.Material
import retrofit2.Response
import javax.inject.Inject

class InventaryRepository @Inject constructor(
    private val apiMaterialService: ApiMaterialService
) {
    suspend fun getMaterialsInformation(): Response<List<Material>> = apiMaterialService.getMaterials()

    suspend fun getCategoriesInformation(): Response<List<Categories>> = apiMaterialService.getCategories()

    suspend fun getSaveInformation(material: Material): Response<Material> {
        return apiMaterialService.getSaveMaterial(material.units, material.idCategory, 0, material.nameProduct,
        material.nameBrand, material.quantity, material.unitValue)
    }
}