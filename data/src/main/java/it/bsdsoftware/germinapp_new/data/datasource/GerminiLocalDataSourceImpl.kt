package it.bsdsoftware.germinapp_new.data.datasource

import it.bsdsoftware.germinapp_new.data.db.GerminiDatabase
import it.bsdsoftware.germinapp_new.data.db.entity.*
import it.bsdsoftware.germinapp_new.data.sharedpreferences.Preferences
import it.bsdsoftware.germinapp_new.domain.entities.*

class GerminiLocalDataSourceImpl(
    private val germinaDB: GerminiDatabase,
    private val preferences: Preferences
) : GerminaLocalDataSource {
    override suspend fun lastUser(success: (User) -> Unit, error: (String) -> Unit) {
        val user = preferences.user
        if (user.token != "") {
            success(user)
        } else {
            error("not found")
        }
    }

    override suspend fun addLastUser(user: User) {
        preferences.user = user
    }

    override suspend fun clearLastUser() {
        preferences.clear()
    }

    override suspend fun saveCategories(categories: List<PhytosanitaryCategory>) {
        germinaDB.germinaDAO()
            .insertAllCategory(
                *categories
                    .map { PhytosanitaryCategoryDB(it.id, it.value) }
                    .toTypedArray()
            )
    }

    override suspend fun saveEntities(entities: List<PhytosanitaryEntity>) {
        germinaDB.germinaDAO()
            .insertAllEntity(
                *entities
                    .map { PhytosanitaryEntityDB(it.id, it.value, it.category.id) }
                    .toTypedArray()
            )
        germinaDB.germinaDAO()
            .insertAllSpecies(
                *entities
                    .flatMap { entity -> entity.species.map { Pair(entity.id, it) } }
                    .map { PhytosanitarySpeciesDB(it.first, it.second) }
                    .toTypedArray()
            )
    }

    override suspend fun getContracts(): List<Contract> {
        val test = germinaDB
            .germinaDAO()
            .getContract()
            .map { contract ->
                Contract(
                    coltivatore = Item(
                        contract.coltivatoreId,
                        contract.nomeColtivatore
                    ),
                    podereId = contract.podereId,
                    indirizzo = contract.indirizzo,
                    localita = contract.localita,
                    telefono = contract.telefono,
                    mail = contract.email,
                    zonaProduzione = contract.zonaDiProduzione,
                    latitudine = contract.latitudine1,
                    longitudine = contract.longitudine1,
                    latitudine2 = contract.latitudine2,
                    longitudine2 = contract.longitudine2,
                    coltura = Coltura(
                        articolo = Item(
                            contract.idArticolo,
                            contract.articolo
                        ),
                        lotto = contract.lotto,
                        specie = Item.factory(contract.specie),
                        varieta = Item.factory(contract.varieta),
                        tipoRaccolta = Item.factory(contract.tipoRaccolta),
                        tipoLinea = Item.factory(contract.tipoLinea),
                        sigla = Item.factory(contract.sigla),
                    ),
                    prodotto = Caratteristiche(
                        tipoProdotto = contract.tipoProdotto,
                        coltura = contract.coltura,
                        destinazione = contract.destinazione
                    ),
                    coltivazione = Coltivazione(
                        contratto = contract.HAContratto,
                        quantitaAttesa = contract.QuantitaAttesa,
                        seminati = contract.HASeminati,
                        dataSeminaFOP = contract.DataSeminaM,
                        dataSemina = contract.DataSemina,
                        nettiDopoDistruzione = contract.HANettiDopoDistruzione,
                    ),
                    note = contract.note
                )
            }
        return test
    }

    override suspend fun insertContracts(contracts: List<Contract>) {
        germinaDB
            .germinaDAO()
            .insertContracts(
                *contracts
                    .map { contract ->
                        ContractDB(
                            contract.coltivatore.id,
                            contract.podereId,
                            contract.coltivatore.value,
                            contract.indirizzo,
                            contract.localita,
                            contract.telefono,
                            contract.mail,
                            contract.zonaProduzione,
                            contract.latitudine,
                            contract.latitudine2,
                            contract.longitudine,
                            contract.longitudine2,
                            contract.note,
                            idArticolo = contract.coltura.articolo.id,
                            articolo = contract.coltura.articolo.value,
                            lotto = contract.coltura.lotto,
                            specie = contract.coltura.specie.toString(),
                            varieta = contract.coltura.varieta.toString(),
                            tipoRaccolta = contract.coltura.tipoRaccolta.toString(),
                            tipoLinea = contract.coltura.tipoLinea.toString(),
                            sigla = contract.coltura.sigla.toString(),
                            tipoProdotto = contract.prodotto.tipoProdotto,
                            coltura = contract.prodotto.coltura,
                            destinazione = contract.prodotto.destinazione,
                            HAContratto = contract.coltivazione.contratto,
                            QuantitaAttesa = contract.coltivazione.quantitaAttesa,
                            HASeminati = contract.coltivazione.seminati,
                            DataSemina = contract.coltivazione.dataSemina,
                            DataSeminaM = contract.coltivazione.dataSeminaFOP,
                            HANettiDopoDistruzione = contract.coltivazione.nettiDopoDistruzione,
                        )
                    }
                    .toTypedArray()
            )
    }

    override suspend fun newDetection(detection: Detection): Array<Long> {
        val localId = germinaDB
            .germinaDAO()
            .insertDetections(
                RilevazioneDB(
                    localId = detection.localId ?: 0,
                    remoteId = detection.remoteId,
                    utentePK = detection.user.id,
                    utenteUsername = detection.user.username,
                    utenteName = detection.user.name,
                    utenteLastName = detection.user.lastName,
                    data = detection.data,
                    oraInizio = detection.startTime,
                    oraFine = detection.endTime,
                    modificato = detection.changed,
                    note = detection.note,
                    idArticolo = detection.contract.coltura.articolo.id,
                    podereId = detection.contract.podereId,
                    lotto = detection.contract.coltura.lotto,
                    coltivatoreId = detection.contract.coltivatore.id
                )
            )
        germinaDB
            .germinaDAO()
            .insertDetectedPhytosanitary(
                *detection
                    .phytosanitaries
                    .map {
                        DetectedPhytosanitaryDB(
                            localId = detection.localId ?: localId[0],
                            entity = it.type.id,
                            detected = it.presence
                        )
                    }
                    .toTypedArray()
            )
        germinaDB
            .germinaDAO()
            .insertMultimedias(
                *detection
                    .images
                    .filter { it.second }
                    .filter {
                        !germinaDB
                            .germinaDAO()
                            .getImages(detection.localId ?: localId[0])
                            .any { image -> image.path == it.first }
                    }
                    .map {
                        MultimediaDB(
                            path = it.first,
                            type = MultimediaType.PHOTO,
                            localId = detection.localId ?: localId[0],
                            toSync = it.second,
                        )
                    }
                    .toTypedArray()
            )
        return localId
    }

    override suspend fun saveDetections(detections: List<Detection>) {
        val newDetections = detections.filter { it.remoteId != null }
        val localId = germinaDB
            .germinaDAO()
            .insertDetections(
                *newDetections
                    .map { detection ->
                        RilevazioneDB(
                            localId = detection.localId ?: 0,
                            remoteId = detection.remoteId,
                            utentePK = detection.user.id,
                            utenteUsername = detection.user.username,
                            utenteName = detection.user.name,
                            utenteLastName = detection.user.lastName,
                            data = detection.data,
                            oraInizio = detection.startTime,
                            oraFine = detection.endTime,
                            modificato = detection.changed,
                            idArticolo = detection.contract.coltura.articolo.id,
                            note = detection.note,
                            podereId = detection.contract.podereId,
                            lotto = detection.contract.coltura.lotto,
                            coltivatoreId = detection.contract.coltivatore.id
                        )
                    }
                    .toTypedArray()
            )
        germinaDB
            .germinaDAO()
            .insertDetectedPhytosanitary(
                *newDetections.flatMap { detection ->
                    detection.phytosanitaries
                        .map {
                            DetectedPhytosanitaryDB(
                                localId = localId[0],
                                entity = it.type.id,
                                detected = it.presence
                            )
                        }
                }
                    .toTypedArray())
        val oldDetections = detections.filter { !it.new && it.remoteId != null }
        oldDetections.forEach {
            it.localId = germinaDB
                .germinaDAO()
                .getDetection(it.remoteId!!).localId
        }
        germinaDB
            .germinaDAO()
            .insertDetections(
                *oldDetections
                    .map { detection ->
                        RilevazioneDB(
                            localId = detection.localId!!,
                            remoteId = detection.remoteId,
                            utentePK = detection.user.id,
                            utenteUsername = detection.user.username,
                            utenteName = detection.user.name,
                            utenteLastName = detection.user.lastName,
                            data = detection.data,
                            oraInizio = detection.startTime,
                            oraFine = detection.endTime,
                            modificato = detection.changed,
                            idArticolo = detection.contract.coltura.articolo.id,
                            note = detection.note,
                            podereId = detection.contract.podereId,
                            lotto = detection.contract.coltura.lotto,
                            coltivatoreId = detection.contract.coltivatore.id
                        )
                    }
                    .toTypedArray()
            )
        germinaDB
            .germinaDAO()
            .insertDetectedPhytosanitary(
                *oldDetections.flatMap { detection ->
                    detection.phytosanitaries
                        .map {
                            DetectedPhytosanitaryDB(
                                localId = detection.localId!!,
                                entity = it.type.id,
                                detected = it.presence
                            )
                        }
                }
                    .toTypedArray())
        oldDetections.forEach() { detection ->
            germinaDB
                .germinaDAO()
                .insertMultimedias(
                    *detection
                        .images
                        .filter { it.second }
                        .map {
                            MultimediaDB(
                                path = it.first,
                                type = MultimediaType.PHOTO,
                                localId = detection.localId ?: localId[0],
                                toSync = it.second,
                                id = germinaDB
                                    .germinaDAO()
                                    .getImages(detection.localId ?: localId[0])
                                    .find { image -> image.path == it.first }?.id ?: 0
                            )
                        }
                        .toTypedArray()
                )
        }
    }

    override suspend fun getDetections(
        contract: Contract,
        phytosanitaries: List<PhytosanitaryEntity>
    ): List<Detection> {
        return germinaDB
            .germinaDAO()
            .getDetections(
                idArticolo = contract.coltura.articolo.id,
                lotto = contract.coltura.lotto,
                idColtivatore = contract.coltivatore.id,
                podere = contract.podereId
            ).map { detection ->
                Detection(
                    contract = contract,
                    remoteId = detection.remoteId,
                    localId = detection.localId,
                    user = User(
                        detection.utentePK,
                        detection.utenteUsername,
                        detection.utenteName,
                        detection.utenteLastName,
                        null
                    ),
                    data = detection.data,
                    startTime = detection.oraInizio,
                    endTime = detection.oraFine,
                    images = germinaDB
                        .germinaDAO().getImages(localId = detection.localId)
                        .map { Pair(it.path, it.toSync) },
                    note = detection.note,
                    phytosanitaries = germinaDB
                        .germinaDAO()
                        .getDetectedPhytosanitaries(detection.localId)
                        .map { detected ->
                            Phytosanitary(
                                phytosanitaries.find { it.id == detected.entity }!!,
                                detected.detected
                            )
                        },
                    new = detection.remoteId == null,
                    changed = detection.modificato
                )
            }
    }

    override suspend fun getPhytosanitaries(): List<PhytosanitaryEntity> =
        germinaDB
            .germinaDAO()
            .getPhytosanitaries()
            .flatMap { category ->
                category.entries.map { phytosanitary ->
                    PhytosanitaryEntity(
                        id = phytosanitary.id,
                        value = phytosanitary.entity,
                        category = PhytosanitaryCategory(
                            id = category.categoryDB.id,
                            value = category.categoryDB.category
                        ),
                        species = listOf()
                    )
                }
            }

    override suspend fun getPhytosanitaries(specie: String): List<PhytosanitaryEntity> {
        val categories = germinaDB
            .germinaDAO()
            .getPhytosanitaryCategories()
            .map { category ->
                PhytosanitaryCategory(
                    id = category.id,
                    value = category.category
                )
            }
        return germinaDB
            .germinaDAO()
            .getPhytosanitariesForSpecie(specie)
            .map { phytosanitary ->
                PhytosanitaryEntity(
                    id = phytosanitary.id,
                    value = phytosanitary.entity,
                    category = PhytosanitaryCategory(
                        id = phytosanitary.categoryId,
                        value = categories.find { it.id == phytosanitary.categoryId }!!.value
                    ),
                    species = listOf()
                )
            }
    }

    override suspend fun getDetectionsToSynchronize(): List<Detection> {
        return germinaDB
            .germinaDAO()
            .getToSynchronize().map { detection ->
                Detection(
                    contract = Contract(
                        coltivatore = Item(
                            detection.coltivatoreId,
                            ""
                        ),
                        podereId = detection.podereId,
                        indirizzo = "",
                        localita = "",
                        telefono = "",
                        mail = "",
                        zonaProduzione = "",
                        latitudine = "",
                        longitudine = "",
                        latitudine2 = "",
                        longitudine2 = "",
                        coltura = Coltura(
                            articolo = Item(
                                detection.idArticolo,
                                ""
                            ),
                            lotto = detection.lotto,
                            specie = Item(
                                "",
                                ""
                            ),
                            varieta = Item(
                                "",
                                ""
                            ),
                            tipoRaccolta = Item(
                                "",
                                ""
                            ),
                            tipoLinea = Item(
                                "",
                                ""
                            ),
                            sigla = Item(
                                "",
                                ""
                            ),
                        ),
                        prodotto = Caratteristiche(
                            tipoProdotto = "",
                            coltura = "",
                            destinazione = ""
                        ),
                        coltivazione = Coltivazione(
                            contratto = "",
                            quantitaAttesa = "",
                            seminati = "",
                            dataSeminaFOP = "",
                            dataSemina = "",
                            nettiDopoDistruzione = "",
                        ),
                        note = ""
                    ),
                    remoteId = detection.remoteId,
                    localId = detection.localId,
                    user = User(
                        detection.utentePK,
                        detection.utenteUsername,
                        detection.utenteName,
                        detection.utenteLastName,
                        null
                    ),
                    data = detection.data,
                    startTime = detection.oraInizio,
                    endTime = detection.oraFine,
                    images = germinaDB
                        .germinaDAO().getImages(localId = detection.localId)
                        .map { Pair(it.path, it.toSync) },
                    note = detection.note,
                    phytosanitaries = germinaDB
                        .germinaDAO()
                        .getDetectedPhytosanitaries(detection.localId)
                        .map { detected ->
                            Phytosanitary(
                                PhytosanitaryEntity(
                                    id = detected.entity,
                                    value = "",
                                    category = PhytosanitaryCategory(
                                        id = 0,
                                        value = ""
                                    ),
                                    species = listOf()
                                ),
                                detected.detected
                            )
                        },
                    new = false,
                    changed = false
                )
            }
    }

}