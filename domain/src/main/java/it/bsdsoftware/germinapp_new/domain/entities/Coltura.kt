package it.bsdsoftware.germinapp_new.domain.entities

data class Coltura(
    val articolo: Item,
    val lotto: String,
    val sigla: Item,
    val specie: Item,
    val varieta: Item,
    val tipoRaccolta: Item,
    val tipoLinea: Item
)