package it.bsdsoftware.germinapp_new.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phytosanitary_categories")
data class PhytosanitaryCategoryDB(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category") val category: String
)
