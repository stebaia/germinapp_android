package it.bsdsoftware.germinapp_new.login

import android.util.Log
import androidx.lifecycle.*
import it.bsdsoftware.germinapp_new.domain.entities.User
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loggedUser = MutableLiveData(Optional.empty<User>())

    val isLoggedIn: LiveData<Boolean> = Transformations.map(_loggedUser) { user ->
        user.isPresent
    }
    val loggedUser: LiveData<Optional<User>> = _loggedUser

    fun loadLogin(){
        viewModelScope.launch {
            loginUseCase(
                {
                    _loggedUser.postValue(Optional.of(it))
                },
                {
                    Log.d("Test", it)
                })
        }
    }


    fun login(user: String, password: String, error: (String)-> Unit) {
        viewModelScope.launch {
            loginUseCase(user.replace("\n",""), password.replace("\n",""),
                {
                    _loggedUser.postValue(Optional.of(it))
                },
                {
                    Log.d("Test", it)
                    error("Login errato")
                })
        }
    }

    fun logout() {
        _loggedUser.postValue(Optional.empty())
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.logout()
        }

    }

    class LoginViewModelFactory(
        private val loginUseCase: LoginUseCase
    ) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                LoginViewModel(loginUseCase) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}