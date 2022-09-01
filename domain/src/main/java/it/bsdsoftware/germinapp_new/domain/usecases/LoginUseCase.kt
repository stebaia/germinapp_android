package it.bsdsoftware.germinapp_new.domain.usecases

import it.bsdsoftware.germinapp_new.domain.entities.User
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository

class LoginUseCase(private val repository: GerminaDomainRepository) {
    operator fun invoke(user: String, password: String, success : (User)-> Unit, error:(String)->Unit) : Unit = repository.login(user, password, success, error)

    suspend operator fun invoke(success : (User) -> Unit, error:(String)->Unit): Unit = repository.loginLastUser(success, error)

    suspend fun logout() = repository.logoutLastUser()
}