package it.bsdsoftware.germinapp_new.data.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.bsdsoftware.germinapp_new.domain.entities.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class NetworkModule(context: Context) {

    private val requestQueue = Volley.newRequestQueue(context).apply {
        start()
    }

    fun login(
        url: String,
        user: String,
        password: String,
        success: (User) -> Unit,
        error: (String) -> Unit
    ) {
        val jsonBody = JSONObject()
        jsonBody.put("username", user)
        jsonBody.put("password", password)
        val request = JsonObjectRequest(Request.Method.POST, url, jsonBody,
            { response ->
                success(
                    User(
                        id = response.getInt("PK"),
                        username = response.getString("Username"),
                        name = response.getString("Name"),
                        lastName = response.getString("LastName"),
                        token = response.getString("Token")
                    )
                )
            },
            { exception ->
                error(exception.message.orEmpty())
                // TODO: Handle error
            })
        requestQueue.add(request)
    }

    fun contacts(
        url: String,
        token: String,
        success: (List<Contract>) -> Unit,
        error: (String) -> Unit
    ) {
        val request = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                val contracts = List(response.length()) { index ->
                    val tmp = response.getJSONObject(index)
                    Contract(
                        coltivatore = Item(
                            tmp.getString("ColtivatoreId"),
                            tmp.getString("Coltivatore")
                        ),
                        podereId = tmp.getString("PodereId"),
                        indirizzo = tmp.getString("Indirizzo"),
                        localita = tmp.getString("Localita"),
                        telefono = tmp.getString("Telefono"),
                        mail = tmp.getString("Mail"),
                        zonaProduzione = tmp.getString("ZonaProduzione"),
                        latitudine = tmp.getString("Latitudine"),
                        longitudine = tmp.getString("Longitudine"),
                        latitudine2 = tmp.getString("Latitudine2"),
                        longitudine2 = tmp.getString("Longitudine2"),
                        coltura = Coltura(
                            articolo = Item(
                                tmp.getString("ArticoloId"),
                                tmp.getString("Articolo")
                            ),
                            lotto = tmp.getString("Lotto"),
                            specie = Item(
                                tmp.getString("SpecieCodice"),
                                tmp.getString("Specie")
                            ),
                            varieta = Item(
                                tmp.getString("VarietaCodice"),
                                tmp.getString("Varieta")
                            ),
                            tipoRaccolta = Item(
                                tmp.getString("TipoTracCodice"),
                                tmp.getString("TipoRaccolta")
                            ),
                            tipoLinea = Item(
                                tmp.getString("LineaCodice"),
                                tmp.getString("TipoLinea")
                            ),
                            sigla = Item(
                                tmp.getString("SiglaCodice"),
                                tmp.getString("Sigla")
                            ),
                        ),
                        prodotto = Caratteristiche(
                            tipoProdotto = tmp.getString("TipoProdotto"),
                            coltura = tmp.getString("TipoColtura"),
                            destinazione = tmp.getString("Destinazione"),
                        ),
                        coltivazione = Coltivazione(
                            contratto = tmp.getString("HaContratti"),
                            quantitaAttesa = tmp.getString("QuantitaAttesa"),
                            seminati = tmp.getString("HaSeminati"),
                            dataSeminaFOP = tmp.getString("DataSeminaA"),
                            dataSemina = tmp.getString("DataSeminaB"),
                            nettiDopoDistruzione = tmp.getString("HaDistrutti"),
                        ),
                        note = tmp.getString("Note")
                    )
                }
                success(contracts)
            },
            { exception ->
                error(exception.message.orEmpty())
                // TODO: Handle error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authentication"] = token
                return headers
            }
        }
        requestQueue.add(request)
    }

    fun phytosanitary(
        url: String,
        token: String,
        success: (List<PhytosanitaryCategory>, List<PhytosanitaryEntity>) -> Unit,
        error: (String) -> Unit
    ) {
        val request = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                val phytosanitaryCategoties =
                    List(response.length()) { index ->
                        val tmp = response.getJSONObject(index)
                        PhytosanitaryCategory(tmp.getInt("CategoriaPK"), tmp.getString("Categoria"))
                    }.distinct()
                val phytosanitaryEntity =
                    List(response.length()) { index ->
                        val tmp = response.getJSONObject(index)
                        PhytosanitaryEntity(
                            tmp.getInt("PK"),
                            tmp.getString("Nome"),
                            phytosanitaryCategoties.find { c -> c.id == tmp.getInt("CategoriaPK") }!!,
                            getSpecies(tmp.getJSONArray("Species"))
                        )
                    }
                success(phytosanitaryCategoties, phytosanitaryEntity)
            },
            { exception ->
                error(exception.message.orEmpty())
                // TODO: Handle error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authentication"] = token
                return headers
            }
        }
        requestQueue.add(request)
    }

    fun getSpecies(array: JSONArray): List<String> =
        List(array.length()) { index ->
            val tmp = array.getJSONObject(index)
            tmp.getString("Codice")
        }

    fun detection(
        url: String,
        token: String,
        phytosanitary: List<PhytosanitaryEntity>,
        contracts: List<Contract>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) {
        val request = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                val detections =
                    List(response.length()) { index ->
                        val tmp = response.getJSONObject(index)
                        val test = contracts
                            .find {
                                it.coltura.articolo.id == tmp.getString("ArticoloIID")
                                    .replace(" ", "") &&
                                        it.coltura.lotto == tmp.getString("LottoID")
                                    .replace(" ", "") &&
                                        it.coltivatore.id == tmp.getString("ColtivatoreID")
                                    .replace(" ", "") &&
                                        it.podereId == tmp.getString("PodereID")
                                    .replace(" ", "")
                            }
                        if (test != null) {
                            Detection(
                                contract = test,
                                user = createUser(tmp),
                                remoteId = tmp.getLong("ID"),
                                localId = null,
                                data = tmp.getString("Data"),
                                endTime = tmp.getString("OraFine"),
                                startTime = tmp.getString("OraInizio"),
                                phytosanitaries = phytosanitary(
                                    tmp.getJSONArray("Fitosanitari"),
                                    phytosanitary
                                ),
                                images = listOf(),
                                note = tmp.getString("Note")
                            ).apply {
                                val temp = this
                                val c = 0
                            }
                        } else {
                            null
                        }
                    }
                success(detections.filterNotNull())
            },
            { exception ->
                error(exception.message.orEmpty())
                // TODO: Handle error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authentication"] = token
                return headers
            }
        }
        requestQueue.add(request)
    }

    private fun phytosanitary(
        jsonArray: JSONArray,
        phytosanitary: List<PhytosanitaryEntity>
    ): List<Phytosanitary> {
        return List(jsonArray.length()) { index ->
            val tmp = jsonArray.getJSONObject(index)
            val test = phytosanitary.find { it.id == tmp.getInt("PK") }
            if (test != null) {
                Phytosanitary(
                    type = phytosanitary.find { it.id == tmp.getInt("PK") }!!,
                    presence = tmp.getBoolean("Esito")
                ).apply {
                    val t = this
                    val c = 0
                }
            } else {
                null
            }
        }.filterNotNull()
    }

    private fun createUser(obj: JSONObject): User {
        return if (obj.getString("UtentePK") == "null") {
            User(-1, "", "", "", null)
        } else {
            User(obj.getInt("UtentePK"), obj.getString("Username"), "", "", null)
        }
    }

    fun syncronizeDetection(
        url: String,
        token: String,
        detections: List<Detection>,
        success: (List<Detection>) -> Unit,
        error: (String) -> Unit
    ) {
        val array = JSONArray()
        detections.forEach { detection ->
            val obj = JSONObject()
            try {
                obj.put("ID", detection.remoteId)
                obj.put("AppID", detection.localId)
                obj.put("PodereID", detection.contract.podereId)
                obj.put("ColtivatoreID", detection.contract.coltivatore.id)
                obj.put("ArticoloIID", detection.contract.coltura.articolo.id)
                obj.put("LottoID", detection.contract.coltura.lotto)
                obj.put("Data", detection.data)
                obj.put("OraInizio", detection.startTime)
                obj.put("OraFine", detection.endTime)
                obj.put("Note", detection.note)
                obj.put("UtentePK", detection.user.id)
                obj.put("Utente", detection.user.name)
                obj.put("Username", detection.user.username)
                val inner = JSONArray()
                detection.phytosanitaries.forEach {
                    val objInner = JSONObject()
                    objInner.put("PK", it.type.id)
                    objInner.put("Esito", it.presence)
                }
                obj.put("Fitosanitari", inner)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            array.put(obj)
        }
        val request = object : JsonArrayRequest(
            Method.POST, url, array,
            { response ->
                val mapper =
                    List(response.length()) { index ->
                        val tmp = response.getJSONObject(index)
                        Pair(tmp.getLong("AppID"), tmp.getLong("Id"))
                    }
                        .toMap()
                success(detections.map { detection ->
                    Detection(
                        contract = detection.contract,
                        remoteId = mapper[detection.localId]!!,
                        localId = detection.localId,
                        user = detection.user,
                        data = detection.data,
                        startTime = detection.startTime,
                        endTime = detection.endTime,
                        images = detection.images,
                        note = detection.note,
                        phytosanitaries = detection.phytosanitaries,
                        new = false,
                        changed = false
                    )
                }
                )
            },
            { exception ->
                error(exception.message.orEmpty())
                // TODO: Handle error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authentication"] = token
                return headers
            }
        }
        requestQueue.add(request)
    }
}
