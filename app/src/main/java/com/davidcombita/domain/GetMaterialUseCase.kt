package com.davidcombita.domain

import com.davidcombita.data.models.Categories
import com.davidcombita.data.models.Material
import com.davidcombita.data.models.MaterialsTatto
import com.davidcombita.repository.InventaryRepository
import retrofit2.Response
import javax.inject.Inject

class GetMaterialUseCase @Inject constructor(
    private val materialRepository: InventaryRepository
){
    suspend fun invoke(): Response<List<Material>> = materialRepository.getMaterialsInformation()

    suspend fun getCategories(): Response<List<Categories>> = materialRepository.getCategoriesInformation()

    suspend fun saveMaterial(material: Material): Response<Material> = materialRepository.getSaveInformation(material)

    suspend fun getMaterialTattoByTatto(tatto: Int): Response<List<MaterialsTatto>> = materialRepository.getMaterialByTatto(tatto)

    suspend fun sendReserva(toNumber: String, fecha: String,
                            style: String, size: String, name:String,
                            email:String, idTatto:Int){
        materialRepository.sendReserva(toNumber, fecha, style, size, name, email, idTatto)
    }

    suspend fun updateMaterial(material: Material):Response<Material> = materialRepository.updateMaterial(material)

    suspend fun updateUnits(unitis: Long, idMaterial:Long):Response<Boolean> = materialRepository.updateUnitsById(unitis, idMaterial)
}