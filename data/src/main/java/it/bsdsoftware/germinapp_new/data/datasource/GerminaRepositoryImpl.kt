package it.bsdsoftware.germinapp_new.data.datasource

import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.entities.User
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GerminaRepositoryImpl(
    private val localDataSource: GerminaLocalDataSource,
    private val remoteDataSourceImpl: GerminaRemoteDataSourceImpl
) : GerminaDomainRepository {
    @OptIn(DelicateCoroutinesApi::class)
    override fun login(
        user: String,
        password: String,
        success: (User) -> Unit,
        error: (String) -> Unit
    ) {
        remoteDataSourceImpl.login(user, password, success = {
            success(it)
            GlobalScope.launch(Dispatchers.IO) {
                localDataSource.addLastUser(it)
            }
        }, error)
    }

    override suspend fun loginLastUser(success: (User) -> Unit, error: (String) -> Unit) {
        localDataSource.lastUser(success, error)
    }

    override suspend fun logoutLastUser() {
        localDataSource.clearLastUser()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun downloadContracts(
        token: String,
        success: (List<Contract>) -> Unit,
        error: (String) -> Unit
    ) {
        remoteDataSourceImpl.contracts(token, { contracts ->
            remoteDataSourceImpl.phytosanitary(
                token,
                { categories, entities ->
                    remoteDataSourceImpl.detection(
                        token,
                        entities,
                        contracts,
                        { detections ->
                            GlobalScope.launch(Dispatchers.IO) {
                                localDataSource.insertContracts(contracts)
                                localDataSource.saveCategories(categories)
                                localDataSource.saveEntities(entities)
                                localDataSource.saveDetections(detections)
                                success(contracts)
                            }
                        }, {}
                    )


                },
                {})
        }, error)


    }

    override suspend fun getContractsFromDB(
        token: String,
        success: (List<Contract>) -> Unit,
        error: (String) -> Unit
    ) {
        try {
            success(localDataSource.getContracts())
        } catch (excpetion: Exception) {
            excpetion.message?.let { error(it) }
        }
    }

    override suspend fun newDetection(
        detection: Detection,
        success: (Detection) -> Unit,
        error: (String) -> Unit
    ) {
        try {
            val ids = localDataSource.newDetection(detection)
            success(
                Detection(
                    contract = detection.contract,
                    user = detection.user,
                    data = detection.data,
                    endTime = detection.endTime,
                    startTime = detection.startTime,
                    phytosanitaries = detection.phytosanitaries,
                    remoteId = detection.remoteId,
                    localId = detection.localId ?: ids[0],
                    images = detection.images,
                    note = detection.note,
                    new = detection.new,
                    changed = detection.changed
                )
            )
        } catch (excpetion: Exception) {
            excpetion.message?.let { error(it) }
        }
    }

    override suspend fun getDetections(
        contract: Contract,
        phytosanitaries: List<PhytosanitaryEntity>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) {
        try {
            success(localDataSource.getDetections(contract, phytosanitaries))
        } catch (excpetion: Exception) {
            excpetion.message?.let { error(it) }
        }
    }

    override suspend fun getPhytosanitaries(
        success: (List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    ) {
        try {
            success(localDataSource.getPhytosanitaries())
        } catch (excpetion: Exception) {
            excpetion.message?.let { error(it) }
        }
    }

    override suspend fun getPhytosanitaries(
        specie: String,
        success: (List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    ) {
        try {
            success(localDataSource.getPhytosanitaries(specie))
        } catch (excpetion: Exception) {
            excpetion.message?.let { error(it) }
        }
    }

    override suspend fun synchronize(token: String, success: () -> Unit, error: (String) -> Unit) {
        try {
            val detections = localDataSource.getDetectionsToSynchronize()
            if (detections.isEmpty()) {
                success()
            } else {
                remoteDataSourceImpl.synchronizeDetection(token, detections, {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            localDataSource.saveDetections(it)
                            success()
                        } catch (excpetion: Exception) {
                            excpetion.message?.let { error(it) }
                        }
                    }
                }, {

                })
            }
        } catch (excpetion: Exception) {
            excpetion.message?.let { error(it) }
        }
    }

    override suspend fun synchronize(
        token: String,
        detecrtions: List<Detection>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}