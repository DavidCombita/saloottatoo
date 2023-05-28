package com.davidcombita.domain

import com.davidcombita.data.models.TattoResponse
import com.davidcombita.repository.TattoRepository
import retrofit2.Response
import javax.inject.Inject

class GetTattosUseCase @Inject constructor(
    private val tattoRepository: TattoRepository
){
    suspend fun invoke(): Response<List<TattoResponse>> = tattoRepository.getTattoHome()
}