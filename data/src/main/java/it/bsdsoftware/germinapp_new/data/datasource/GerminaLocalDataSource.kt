package it.bsdsoftware.germinapp_new.data.datasource

import it.bsdsoftware.germinapp_new.domain.entities.*


interface GerminaLocalDataSource {
    suspend fun lastUser(success: (User) -> Unit, error: (String) -> Unit)

    suspend fun addLastUser(user: User)

    suspend fun clearLastUser()

    suspend fun saveCategories(categories: List<PhytosanitaryCategory>)

    suspend fun saveEntities(entity: List<PhytosanitaryEntity>)

    suspend fun getContracts(): List<Contract>

    suspend fun insertContracts(contracts: List<Contract>)

    suspend fun newDetection(detection: Detection): Array<Long>

    suspend fun saveDetections(detections: List<Detection>)

    suspend fun getDetections(
        contract: Contract,
        phytosanitaries: List<PhytosanitaryEntity>
    ): List<Detection>

    suspend fun getPhytosanitaries(): List<PhytosanitaryEntity>

    suspend fun getPhytosanitaries(specie: String): List<PhytosanitaryEntity>

    suspend fun getDetectionsToSynchronize(): List<Detection>
}