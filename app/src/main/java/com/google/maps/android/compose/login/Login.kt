package com.google.maps.android.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.R

import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.entities.User
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import it.bsdsoftware.germinapp_new.domain.usecases.LoginUseCase
import it.bsdsoftware.germinapp_new.login.LoginViewModel
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationScreen
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme

@Composable
fun LoginForm(loginViewModel: LoginViewModel, localFocusManager: FocusManager) {
    var user by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }
    OutlinedTextField(
        value = user,
        onValueChange = { user = it },
        placeholder = { TextFieldValue("Username") },
        label = { Text(text = "Username") },
        modifier = Modifier.fillMaxWidth(0.7f),
        singleLine = true,
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            localFocusManager.moveFocus(FocusDirection.Down)
        })
    )
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Add
            else Icons.Filled.ArrowBack
            val description =
                if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            loginViewModel.login(user, password) {
                error.value = it
                openDialog.value = true
            }
        }),
        modifier = Modifier.fillMaxWidth(0.7f)
    )
    Button(
        onClick = {
            loginViewModel.login(user, password) {
                error.value = it
                openDialog.value = true
            }
            localFocusManager.clearFocus()
        },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(top = 10.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Red,
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
    )
    {
        Text("Login")
    }
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Login error")
            },
            text = {
                Text(error.value)
            },
            confirmButton = {
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Ok")
                }
            }
        )
    }
}

@Composable
fun Login(
    navController: NavController,
    loginViewModel: LoginViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    loginViewModel.isLoggedIn.observe(lifecycleOwner) {
        if (it) {
            navController.navigate(NavigationScreen.HOME.name)
        }
    }
    val localFocusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            },
    ) {
        BoxWithConstraints {
            if (maxWidth < 500.dp) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.drawable.ic_splash),
                        contentDescription = "Germini App",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f)
                    )
                    LoginForm(loginViewModel, localFocusManager)
                }
            } else {
                Row(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painterResource(R.drawable.ic_splash),
                        contentDescription = "Germini App",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .fillMaxHeight()
                            .padding(50.dp)
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoginForm(loginViewModel, localFocusManager)
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 300, heightDp = 500)
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
        Login(navController, loginViewModel)
    }
}