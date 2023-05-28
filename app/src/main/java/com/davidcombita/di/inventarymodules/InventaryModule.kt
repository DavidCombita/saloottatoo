package com.davidcombita.di.inventarymodules

import com.davidcombita.data.api.ApiMaterialService
import com.davidcombita.domain.GetMaterialUseCase
import com.davidcombita.repository.InventaryRepository
import com.davidcombita.viewmodels.AddMateriaViewModel
import com.davidcombita.viewmodels.InventaryViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
class InventaryModule {

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiMaterialService = retrofit.create(
        ApiMaterialService::class.java)

    @Provides
    fun provideInventaryRepository(apiMaterialService: ApiMaterialService): InventaryRepository = InventaryRepository(apiMaterialService)

    @Provides
    fun provideInventaryUseCase(inventaryRepository: InventaryRepository): GetMaterialUseCase = GetMaterialUseCase(inventaryRepository)

    @Provides
    fun provideInventaryViewModel(getInventaryUseCase: GetMaterialUseCase): InventaryViewModel = InventaryViewModel(getInventaryUseCase)

    @Provides
    fun provideAddViewModel(getInventaryUseCase: GetMaterialUseCase): AddMateriaViewModel = AddMateriaViewModel(getInventaryUseCase)
}