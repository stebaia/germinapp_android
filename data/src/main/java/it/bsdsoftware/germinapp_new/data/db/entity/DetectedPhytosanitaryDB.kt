package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.*

@Entity(
    tableName = "detected_phytosanitaries",
    primaryKeys = ["local_id", "entity"], foreignKeys = [
        ForeignKey(
            entity = RilevazioneDB::class,
            parentColumns = ["local_id"],
            childColumns = ["local_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhytosanitaryEntityDB::class,
            parentColumns = ["id"],
            childColumns = ["entity"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class DetectedPhytosanitaryDB(
    @ColumnInfo(name = "local_id") val localId: Long,
    val entity: Int,
    val detected: Boolean
)

data class DetectionWithInfo(
    @Embedded
    var rilevazione: RilevazioneDB,
    @Relation(
        parentColumn = "local_id",
        entityColumn = "entity",
        associateBy = Junction(DetectedPhytosanitaryDB::class)
    )
    val phytosanitaries: List<PhytosanitaryEntityDB>
)