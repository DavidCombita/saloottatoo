package com.davidcombita.repository

import com.davidcombita.data.api.ApiMaterialService
import com.davidcombita.data.models.Categories
import com.davidcombita.data.models.Material
import com.davidcombita.data.models.MaterialsTatto
import retrofit2.Response
import retrofit2.http.Query
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

    suspend fun  getMaterialByTatto(tatto: Int): Response<List<MaterialsTatto>> = apiMaterialService.getMaterialByTatto(tatto)

    suspend fun sendReserva(toNumber: String, fecha: String,
                             style: String, size: String, name:String,
                            email:String, idTatto:Int){
        apiMaterialService.getSendReserva(toNumber, fecha, style, size, name, email, idTatto)
    }

    suspend fun updateMaterial(material: Material): Response<Material> {
        return apiMaterialService.updateMaterial(material.units, material.idCategory, 0, material.nameProduct,
            material.nameBrand, material.quantity, material.unitValue)
    }

    suspend fun updateUnitsById(unitis: Long, idMaterial:Long): Response<Boolean>{
        return apiMaterialService.updateUnitsById(unitis, idMaterial)
    }
}