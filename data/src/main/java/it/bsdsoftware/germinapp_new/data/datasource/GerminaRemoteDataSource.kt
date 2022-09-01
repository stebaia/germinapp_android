package it.bsdsoftware.germinapp_new.data.datasource

import it.bsdsoftware.germinapp_new.domain.entities.*


interface GerminaRemoteDataSource {
    fun login(user: String, password: String, success: (User) -> Unit, error: (String) -> Unit)

    fun contracts(token: String, success: (List<Contract>) -> Unit, error: (String) -> Unit)

    fun phytosanitary(
        token: String,
        success: (List<PhytosanitaryCategory>, List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    )

    fun detection(
        token: String,
        phytosanitary: List<PhytosanitaryEntity>,
        contracts: List<Contract>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    )

    fun synchronizeDetection(
        token: String,
        detections: List<Detection>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    )
}