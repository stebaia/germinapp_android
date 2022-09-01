package it.bsdsoftware.germinapp_new.data.datasource

import it.bsdsoftware.germinapp_new.data.api.NetworkModule
import it.bsdsoftware.germinapp_new.data.api.sendFileUnirest
import it.bsdsoftware.germinapp_new.domain.entities.*
import java.io.File

class GerminaRemoteDataSourceImpl(
    private val networkModule: NetworkModule,
    private val basicUrl: String
) : GerminaRemoteDataSource {
    override fun login(
        user: String,
        password: String,
        success: (User) -> Unit,
        error: (String) -> Unit
    ) {
        networkModule.login("$basicUrl/Login", user, password, success, error)
    }

    override fun contracts(
        token: String,
        success: (List<Contract>) -> Unit,
        error: (String) -> Unit
    ) {
        networkModule.contacts("$basicUrl/Contratti", token, success, error)
    }

    override fun phytosanitary(
        token: String,
        success: (List<PhytosanitaryCategory>, List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    ) {
        networkModule.phytosanitary(
            "$basicUrl/Fitosanitari/GetFitosanitari",
            token,
            success,
            error
        )
    }

    override fun detection(
        token: String,
        phytosanitary: List<PhytosanitaryEntity>,
        contracts: List<Contract>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) {
        networkModule.detection(
            "$basicUrl/Rilevazioni/GetRilevazioni",
            token,
            phytosanitary,
            contracts,
            success,
            error
        )
    }

    override fun synchronizeDetection(
        token: String,
        detections: List<Detection>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) {
        networkModule.syncronizeDetection(
            "$basicUrl/Rilevazioni/PostRilevazioni",
            token,
            detections,

            { list ->
                val waitFor = list.map { it ->
                    sendFileUnirest(
                        "$basicUrl/Foto",
                        token,
                        it.remoteId!!,
                        it.images.filter { filter -> filter.second }
                            .map { path -> File(path.first) })
                }
                try {
                    for (future in waitFor) {
                        future.get()
                    }
                    list.forEach() {
                        it.images = it.images.map { image ->
                            Pair(image.first, false)
                        }
                    }
                    success(list)
                } catch (e: Exception) {
                    e.message?.let { error(it) }
                }
            },
            error
        )
    }
}