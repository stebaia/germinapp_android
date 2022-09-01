package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.*

@Entity(
    tableName = "phytosanitary_entities",
    foreignKeys = [ForeignKey(
        entity = PhytosanitaryCategoryDB::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PhytosanitaryEntityDB(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "entity") val entity: String,
    @ColumnInfo(name = "category_id") val categoryId: Int
)

data class CategoryWithEntity(
    @Embedded val categoryDB: PhytosanitaryCategoryDB,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val entries: List<PhytosanitaryEntityDB>
)
