package it.bsdsoftware.germinapp_new.domain.entities

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val lastName: String,
    val token: String?
)
