package com.davidcombita.data.api

import com.davidcombita.data.models.TattoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceTatto {

    @GET("Tatto/getHomeTattos")
    suspend fun getTattos(): Response<List<TattoResponse>>

}