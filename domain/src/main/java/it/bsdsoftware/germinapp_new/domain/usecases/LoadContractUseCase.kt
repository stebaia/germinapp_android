package it.bsdsoftware.germinapp_new.domain.usecases

import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository

class LoadContractUseCase(private val repository: GerminaDomainRepository) {
    suspend operator fun invoke(token: String, success : (List<Contract>)-> Unit, error:(String)->Unit) : Unit = repository.getContractsFromDB(token, success, error)

    suspend fun remote(token: String, success : (List<Contract>)-> Unit, error:(String)->Unit) : Unit = repository.downloadContracts(token, success, error)
}