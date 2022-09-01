package com.google.maps.android.compose

import android.app.Application
import com.google.maps.android.compose.di.ServiceLocator
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoadContractUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoginUseCase

class App : Application() {

    private val germinaRepository: GerminaDomainRepository
        get() = ServiceLocator.provideGerminaRepository(this)

    val loginUseCase: LoginUseCase
        get() = LoginUseCase(germinaRepository)

    val loadContractUseCase: LoadContractUseCase
        get() = LoadContractUseCase(germinaRepository)

    val detectionUseCase: DetectionUseCase
        get() = DetectionUseCase(germinaRepository)

}