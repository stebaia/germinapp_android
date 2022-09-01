package it.bsdsoftware.germinapp_new.domain.entities

data class PhytosanitaryEntity(
    val id: Int,
    val value: String,
    val category: PhytosanitaryCategory,
    val species: List<String>
)
