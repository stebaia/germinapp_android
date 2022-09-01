package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.*

@Entity(
    tableName = "detections",
    foreignKeys = [ForeignKey(
        entity = ContractDB::class,
        parentColumns = arrayOf("id_coltivatore", "id_podere", "id_articolo", "lotto"),
        childColumns = arrayOf("id_coltivatore", "id_podere", "id_articolo", "lotto"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["remote_id"], unique = true)]
)
data class RilevazioneDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0,
    @ColumnInfo(name = "remote_id") val remoteId: Long?,
    @ColumnInfo(name = "utentePK") val utentePK: Int,
    @ColumnInfo(name = "utenteUsername") val utenteUsername: String,
    @ColumnInfo(name = "utenteName") val utenteName: String,
    @ColumnInfo(name = "utenteLastName") val utenteLastName: String,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "ora_inizio") val oraInizio: String,
    @ColumnInfo(name = "ora_fine") val oraFine: String,
    @ColumnInfo(name = "modificato") val modificato: Boolean,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "id_articolo") val idArticolo: String,
    @ColumnInfo(name = "id_podere") val podereId: String,
    @ColumnInfo(name = "lotto") val lotto: String,
    @ColumnInfo(name = "id_coltivatore") val coltivatoreId: String,

)
