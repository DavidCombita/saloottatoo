package com.davidcombita.domain

import com.davidcombita.BuildConfig
import com.davidcombita.data.models.Material
import com.davidcombita.repository.InventaryRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GetCatsUseCaseTest {

    @RelaxedMockK
    private lateinit var catsRepository: InventaryRepository

    lateinit var getCatsUseCase: GetMaterialUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCatsUseCase = GetMaterialUseCase(catsRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `test if you call repository and return empty list`() = runBlocking{
        //Given
        coEvery { catsRepository.getCatsInformation(BuildConfig.APIKEY) } returns Response.success(emptyList())
        //When
        getCatsUseCase(BuildConfig.APIKEY)
        //Then
        coVerify(exactly = 1) { catsRepository.getCatsInformation(BuildConfig.APIKEY) }
    }

    @Test
    fun `test call repository and return list`() = runBlocking{
        val catsResponse = Response.success(emptyList<Material>())
        //Given
        coEvery { catsRepository.getCatsInformation(BuildConfig.APIKEY) } returns catsResponse
        //When
        val response = getCatsUseCase(BuildConfig.APIKEY)
        //Then
        coVerify(exactly = 1) { catsRepository.getCatsInformation(BuildConfig.APIKEY) }
        assert(response == catsResponse)
    }
}