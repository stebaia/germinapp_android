package it.bsdsoftware.germinapp_new.domain.repository

import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.entities.User

interface GerminaDomainRepository {

    fun login(user: String, password: String, success: (User) -> Unit, error: (String) -> Unit)

    suspend fun loginLastUser(success: (User) -> Unit, error: (String) -> Unit)

    suspend fun logoutLastUser()

    suspend fun downloadContracts(
        token: String,
        success: (List<Contract>) -> Unit,
        error: (String) -> Unit
    )

    suspend fun getContractsFromDB(
        token: String,
        success: (List<Contract>) -> Unit,
        error: (String) -> Unit
    )

    suspend fun newDetection(
        detection: Detection,
        success: (Detection) -> Unit,
        error: (String) -> Unit
    )

    suspend fun getDetections(
        contract: Contract,
        phytosanitaries: List<PhytosanitaryEntity>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    )

    suspend fun getPhytosanitaries(
        success: (List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    )

    suspend fun getPhytosanitaries(
        specie: String,
        success: (List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    )

    suspend fun synchronize(token: String, success: () -> Unit, error: (String) -> Unit)

    suspend fun synchronize(
        token: String,
        detecrtions: List<Detection>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    )

}