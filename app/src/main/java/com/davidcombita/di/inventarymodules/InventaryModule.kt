package com.davidcombita.di.inventarymodules

import com.davidcombita.data.api.ApiMaterialService
import com.davidcombita.domain.GetMaterialUseCase
import com.davidcombita.repository.InventaryRepository
import com.davidcombita.viewmodels.AddMateriaViewModel
import com.davidcombita.viewmodels.InventaryViewModel
import com.davidcombita.viewmodels.ReservaViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class InventaryModule {

    @Provides
    fun provideApiService(@Named("RetrofitINVEN")retrofit: Retrofit): ApiMaterialService = retrofit.create(
        ApiMaterialService::class.java)

    @Provides
    fun provideInventaryRepository(apiMaterialService: ApiMaterialService): InventaryRepository = InventaryRepository(apiMaterialService)

    @Provides
    fun provideInventaryUseCase(inventaryRepository: InventaryRepository): GetMaterialUseCase = GetMaterialUseCase(inventaryRepository)

    @Provides
    fun provideInventaryViewModel(getInventaryUseCase: GetMaterialUseCase): InventaryViewModel = InventaryViewModel(getInventaryUseCase)

    @Provides
    fun provideAddViewModel(getInventaryUseCase: GetMaterialUseCase): AddMateriaViewModel = AddMateriaViewModel(getInventaryUseCase)

    @Provides
    fun provideReservaViewModel(getInventaryUseCase: GetMaterialUseCase): ReservaViewModel = ReservaViewModel(getInventaryUseCase)


}