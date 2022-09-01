package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.*

@Entity(
    tableName = "phytosanitary_species",
    primaryKeys = ["entity_id", "specie"],
    foreignKeys = [ForeignKey(
        entity = PhytosanitaryEntityDB::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("entity_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class PhytosanitarySpeciesDB(
    @ColumnInfo(name = "entity_id") val idEntity: Int,
    @ColumnInfo(name = "specie") val specie: String
)