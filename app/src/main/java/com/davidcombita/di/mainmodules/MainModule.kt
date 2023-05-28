package com.davidcombita.di.mainmodules

import com.davidcombita.data.api.ApiMaterialService
import com.davidcombita.data.api.ApiServiceTatto
import com.davidcombita.domain.GetMaterialUseCase
import com.davidcombita.domain.GetTattosUseCase
import com.davidcombita.repository.InventaryRepository
import com.davidcombita.repository.TattoRepository
import com.davidcombita.viewmodels.AddMateriaViewModel
import com.davidcombita.viewmodels.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideApiMainService(@Named("RetrofitTATTO")retrofit: Retrofit): ApiServiceTatto {
        return retrofit.create(ApiServiceTatto::class.java)
    }

    @Provides
    @Singleton
    fun provideMainRepository(api: ApiServiceTatto): TattoRepository = TattoRepository(api)

    @Provides
    @Singleton
    fun provideMainUseCase(repository: TattoRepository): GetTattosUseCase = GetTattosUseCase(repository)

    @Provides
    @Singleton
    fun provideAddViewModel(getTattoUseCase: GetTattosUseCase): MainViewModel = MainViewModel(getTattoUseCase)
}