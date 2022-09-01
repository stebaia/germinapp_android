package it.bsdsoftware.germinapp_new.domain.entities

data class Coltivazione(
    val contratto: String,
    val quantitaAttesa: String,
    val seminati: String,
    val dataSeminaFOP: String,
    val dataSemina: String,
    val nettiDopoDistruzione: String
)