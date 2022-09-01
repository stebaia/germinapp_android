package com.google.maps.android.compose.splash

import com.google.maps.android.compose.R
import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.entities.User
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import it.bsdsoftware.germinapp_new.domain.usecases.LoginUseCase
import it.bsdsoftware.germinapp_new.login.LoginViewModel
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationScreen
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration


/**
 * a spash view that contains an image that, ig clicked switch to login view
 *
 * @param navController the NavController of the application which must have the element NavigationScreen.LOGIN.name
 */
@Composable
fun Splash(
    navController: NavController,
    loginViewModel: LoginViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    loginViewModel.loadLogin()
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        loginViewModel
            .isLoggedIn
            .observe(lifecycleOwner) {
                if (it) {
                    navController
                        .navigate(NavigationScreen.HOME.name)
                }else{
                    object : CountDownTimer(2500, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                        }
                        override fun onFinish() {
                            navController.navigate(NavigationScreen.LOGIN.name)
                        }
                    }.start()

                }
            }
        Box(Modifier.height(500.dp).width(500.dp), contentAlignment = Alignment.Center) {
            Image(
                painterResource(R.drawable.ic_splash),
                contentDescription = "Germinapp",

                modifier = Modifier.fillMaxSize(0.50f)

            )
        }

    }
}

fun splashFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Germinapp_newTheme {
        val navController = rememberNavController()
        val repository = object : GerminaDomainRepository {
            override fun login(
                user: String,
                password: String,
                success: (User) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun loginLastUser(success: (User) -> Unit, error: (String) -> Unit) {
                TODO("Not yet implemented")
            }

            override suspend fun logoutLastUser() {
                TODO("Not yet implemented")
            }

            override suspend fun downloadContracts(
                token: String,
                success: (List<Contract>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getContractsFromDB(
                token: String,
                success: (List<Contract>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun newDetection(
                detection: Detection,
                success: (Detection) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getDetections(
                contract: Contract,
                phytosanitaries: List<PhytosanitaryEntity>,
                success: (List<Detection>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getPhytosanitaries(
                success: (List<PhytosanitaryEntity>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun getPhytosanitaries(
                specie: String,
                success: (List<PhytosanitaryEntity>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun synchronize(
                token: String,
                success: () -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun synchronize(
                token: String,
                detecrtions: List<Detection>,
                success: (List<Detection>) -> Unit,
                error: (String) -> Unit
            ) {
                TODO("Not yet implemented")
            }

        }
        val loginViewModel = LoginViewModel(LoginUseCase(repository))
        Splash(navController, loginViewModel)
    }
}