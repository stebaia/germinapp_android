package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contracts", primaryKeys = ["id_coltivatore", "id_podere", "id_articolo", "lotto"])
data class ContractDB(
    @ColumnInfo(name = "id_coltivatore") val coltivatoreId: String,
    @ColumnInfo(name = "id_podere") val podereId: String,
    @ColumnInfo(name = "nome_coltivatore") val nomeColtivatore: String,
    @ColumnInfo(name = "indirizzo") val indirizzo: String,
    @ColumnInfo(name = "localita") val localita: String,
    @ColumnInfo(name = "telefono") val telefono: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "zona_di_produzione") val zonaDiProduzione: String,
    @ColumnInfo(name = "latitudine1") val latitudine1: String,
    @ColumnInfo(name = "latitudine2") val latitudine2: String,
    @ColumnInfo(name = "longitudine1") val longitudine1: String,
    @ColumnInfo(name = "longitudine2") val longitudine2: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "id_articolo") val idArticolo: String,
    @ColumnInfo(name = "articolo") val articolo: String,
    @ColumnInfo(name = "lotto") val lotto: String,
    @ColumnInfo(name = "specie") val specie: String,
    @ColumnInfo(name = "varieta") val varieta: String,
    @ColumnInfo(name = "tipo_raccolta") val tipoRaccolta: String,
    @ColumnInfo(name = "tipo_linea") val tipoLinea: String,
    @ColumnInfo(name = "tipo_prodotto") val tipoProdotto: String,
    @ColumnInfo(name = "sigla") val sigla: String,
    @ColumnInfo(name = "coltura") val coltura: String,
    @ColumnInfo(name = "destinazione") val destinazione: String,
    @ColumnInfo(name = "HA_contratto") val HAContratto: String,
    @ColumnInfo(name = "quantita_attesa") val QuantitaAttesa: String,
    @ColumnInfo(name = "HA_seminati") val HASeminati: String,
    @ColumnInfo(name = "data_semina") val DataSemina: String,
    @ColumnInfo(name = "data_semina_M") val DataSeminaM: String,
    @ColumnInfo(name = "HA_netti_dopo_distruzione") val HANettiDopoDistruzione: String,
)