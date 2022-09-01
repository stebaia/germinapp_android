package com.google.maps.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.bsdsoftware.germinapp_new.detail_section.DetectionDetailsViewModel
import it.bsdsoftware.germinapp_new.detail_section.DetectionNewViewModel
import it.bsdsoftware.germinapp_new.detail_section.SelectViewModelContract
import it.bsdsoftware.germinapp_new.detail_section.SelectViewModelDetection
import com.google.maps.android.compose.detection.DetectionViewModel
import com.google.maps.android.compose.homepage.HomePage
import com.google.maps.android.compose.homepage.HomePageViewModel
import com.google.maps.android.compose.login.Login
import it.bsdsoftware.germinapp_new.login.LoginViewModel
import com.google.maps.android.compose.splash.Splash
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationScreen
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginViewModel: LoginViewModel by viewModels {
            LoginViewModel.LoginViewModelFactory(
                ((application) as App).loginUseCase
            )
        }
        val selectViewModelDetection: SelectViewModelContract by viewModels {
            SelectViewModelContract.SelectViewModelContractFactory()
        }

        val detectionSelectViewModelDetection: SelectViewModelDetection by viewModels {
            SelectViewModelDetection.SelectViewModelDetectionFactory()
        }

        val detectionViewModel: DetectionViewModel by viewModels {
            DetectionViewModel.DetectionViewModelFactory(
                detectionSelectViewModelDetection,
                ((application) as App).detectionUseCase
            )
        }

        val detectionNewViewModel: DetectionNewViewModel by viewModels {
            DetectionNewViewModel.DetectionNewViewModelFactory(
                loginViewModel,
                selectViewModelDetection,
                detectionViewModel,
                ((application) as App).detectionUseCase,
                (application) as App
            )
        }

        val detectionDetailsViewModel: DetectionDetailsViewModel by viewModels {
            DetectionDetailsViewModel.DetectionDetailsViewModelFactory(
                detectionSelectViewModelDetection,
                detectionViewModel,
                ((application) as App).detectionUseCase,
                (application) as App
            )
        }

        val homepageViewModel: HomePageViewModel by viewModels {
            HomePageViewModel.HomePageViewModelFactory(
                loginViewModel,
                selectViewModelDetection,
                detectionViewModel,
                detectionNewViewModel,
                detectionDetailsViewModel,
                ((application) as App).loadContractUseCase,
                ((application) as App).detectionUseCase
            )
        }
        setContent {
            val navController = rememberNavController()
            Germinapp_newTheme {
                NavHost(navController, startDestination = NavigationScreen.SPLASH.name) {
                    composable(NavigationScreen.SPLASH.name) {
                        Splash(navController, loginViewModel)
                    }
                    composable(NavigationScreen.LOGIN.name) {
                        Login(navController = navController, loginViewModel)
                    }
                    composable(NavigationScreen.HOME.name) {
                        HomePage(navController = navController, homepageViewModel, loginViewModel)
                    }
                }
                // A surface container using the 'background' color from the theme
            }
        }
    }

}
