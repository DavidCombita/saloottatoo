package com.davidcombita.repository

import com.davidcombita.data.api.ApiServiceTatto
import com.davidcombita.data.models.TattoResponse
import retrofit2.Response
import javax.inject.Inject

class TattoRepository @Inject constructor(
    private val apiService: ApiServiceTatto
)  {
    suspend fun getTattoHome(): Response<List<TattoResponse>> = apiService.getTattos()
}