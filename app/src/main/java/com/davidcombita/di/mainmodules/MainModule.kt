package com.davidcombita.di.mainmodules

import com.davidcombita.data.api.ApiMaterialService
import com.davidcombita.domain.GetMaterialUseCase
import com.davidcombita.repository.InventaryRepository
import com.davidcombita.viewmodels.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object MainModule {}