package com.davidcombita.data.api

import com.davidcombita.data.models.Categories
import com.davidcombita.data.models.Material
import com.davidcombita.data.models.MaterialsTatto
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiMaterialService {

    @GET("Inventary/getAllInventary")
    suspend fun getMaterials(): Response<List<Material>>

    @GET("Inventary/getAllCategories")
    suspend fun getCategories(): Response<List<Categories>>

    @POST("Inventary/saveMaterial")
    suspend fun getSaveMaterial(@Query("units")units: Long, @Query("idCategory")idCategory: Long,
                                @Query("idMaterial")idMaterial:Int,
                                @Query("nameProduct") nameProduct:String, @Query("nameBrand")nameBrand:String,
                                @Query("quantity")quantity: Long,
                                @Query("unitValue")unitValue:Long): Response<Material>

    @GET("Inventary/getMaterialByTatto")
    suspend fun getMaterialByTatto(@Query("tatto")tatto: Int): Response<List<MaterialsTatto>>

    @POST("SMS/send-sms")
    suspend fun getSendReserva(@Query("to") toNumber: String, @Query("fecha")fecha: String,
                               @Query("style")style: String, @Query("size")size: String)


}