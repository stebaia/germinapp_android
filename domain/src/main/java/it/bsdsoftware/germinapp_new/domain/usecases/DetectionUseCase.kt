package it.bsdsoftware.germinapp_new.domain.usecases

import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository

class DetectionUseCase(private val repository: GerminaDomainRepository) {

    suspend fun getDetections(
        contract: Contract,
        phytosanitaries: List<PhytosanitaryEntity>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) = repository.getDetections(contract, phytosanitaries, success, error)

    suspend fun getPhytosanitaries(
        success: (List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    ) = repository.getPhytosanitaries(success, error)

    suspend fun getPhytosanitaries(
        specie: String,
        success: (List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    ) = repository.getPhytosanitaries(specie, success, error)

    suspend fun newDetection(
        detection: Detection,
        success: (Detection) -> Unit,
        error: (String) -> Unit
    ) = repository.newDetection(detection, success, error)

    suspend fun saveDetection(
        detection: Detection,
        success: (Detection) -> Unit,
        error: (String) -> Unit
    ) = repository.newDetection(detection, success, error)

    suspend fun synchronizeDetection(token: String, success: () -> Unit, error: (String) -> Unit) =
        repository.synchronize(token, success, error)

    suspend fun synchronizeDetection(
        token: String,
        detections: List<Detection>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) = repository.synchronize(token, detections, success, error)
}