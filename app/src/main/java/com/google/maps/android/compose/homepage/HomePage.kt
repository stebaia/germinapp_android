package com.google.maps.android.compose.homepage

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.R
import com.google.maps.android.compose.ui.common.*

import it.bsdsoftware.germinapp_new.detail_section.*
import com.google.maps.android.compose.detection.DetectionPage
import com.google.maps.android.compose.detection.DetectionViewModel
import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.entities.User
import it.bsdsoftware.germinapp_new.domain.repository.GerminaDomainRepository
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoadContractUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoginUseCase
import it.bsdsoftware.germinapp_new.login.LoginViewModel
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationHome
import it.bsdsoftware.germinapp_new.ui.navigation.NavigationScreen
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    navController: NavController,
    homepageViewModel: HomePageViewModel,
    loginViewModel: LoginViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    loginViewModel.isLoggedIn.observe(lifecycleOwner) {
        if (!it) {
            if (navController.findDestination(NavigationScreen.LOGIN.name) != null) {
                navController.navigate(NavigationScreen.LOGIN.name)
            }
        } else {
            homepageViewModel.loadContract()
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        var user by remember { mutableStateOf("") }
        val dialogOpen = remember {
            mutableStateOf(false)
        }
        val drawerState = remember { mutableStateOf(DrawerValue.Open) }
        var openFilter by remember {
            mutableStateOf(false)
        }
        var selectedOption by remember {
            mutableStateOf("Lista")
        }
        loginViewModel.loggedUser.observe(lifecycleOwner) {
            if (it.isPresent) {
                user = it.get().username + "\n" + it.get().name + " " + it.get().lastName
            }
        }
        val navControllerHome = rememberNavController()
        ScaffoldRight(main = {
            NavHost(navControllerHome, startDestination = NavigationHome.CONTRATTI.name) {
                composable(NavigationHome.CONTRATTI.name) {
                    ScaffoldLeftPanelShift(
                        0.05f, 0.4f,
                        buttonContent =
                        {
                            Column( verticalArrangement = Arrangement.Center,
                                modifier = Modifier.width(150.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,) {
                                /*Button(
                                    onClick = {
                                        homepageViewModel.downloadContract()
                                    },
                                    enabled = true,
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Red,
                                        contentColor = Color.White
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .fillMaxWidth(0.32f)
                                        .padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Download,
                                        contentDescription = "download",
                                        modifier = Modifier.padding(end = 4.dp),
                                        tint = Color.White
                                    )
                                    //Text("Scarica")
                                }*/
                                val options = listOf(
                                    "Lista",
                                    "Mappa",
                                )
                                
                                val onSelectionChange = { text: String ->
                                    selectedOption = text
                                }
                                Spacer(modifier = Modifier.height(40.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(color = colorResource(id = R.color.element_ligth_gray))
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,

                                        ) {
                                        options.forEach { text ->
                                            Row(
                                                modifier = Modifier
                                                    .padding(
                                                        all = 10.dp,
                                                    ),
                                            ) {
                                                if (text == "Lista") {
                                                    Box(

                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .width(60.dp)
                                                            .height(60.dp)
                                                            .background(
                                                                color = if (text == selectedOption) colorResource(
                                                                    id = R.color.grey_filter
                                                                ) else colorResource(
                                                                    id = R.color.element_ligth_gray
                                                                )
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ){
                                                        IconButton(modifier = Modifier.then(
                                                            Modifier.size(
                                                                34.dp
                                                            )
                                                        ),
                                                            onClick = {
                                                                onSelectionChange(text)
                                                            }) {

                                                            Icon(
                                                                modifier = Modifier.size(
                                                                    34.dp

                                                                ),
                                                                imageVector = Icons.Filled.List,
                                                                contentDescription = if (!openFilter) {
                                                                    "filter"
                                                                } else {
                                                                    "close"
                                                                },
                                                                tint = if (text == selectedOption) {
                                                                    Color.White
                                                                } else {
                                                                    colorResource(
                                                                        id = R.color.grey_filter
                                                                    )
                                                                }

                                                            )


                                                        }
                                                    }

                                                } else {

                                                    Box(

                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .width(60.dp)
                                                            .height(60.dp)
                                                            .background(
                                                                color = if (text == selectedOption) colorResource(
                                                                    id = R.color.grey_filter
                                                                ) else colorResource(
                                                                    id = R.color.element_ligth_gray
                                                                )
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        IconButton(

                                                            onClick = {
                                                                onSelectionChange(text)
                                                            }) {

                                                                Icon(
                                                                    modifier = Modifier.size(
                                                                        34.dp

                                                                    ),
                                                                    imageVector = Icons.Filled.Place,
                                                                    contentDescription = if (!openFilter) {
                                                                        "filter"
                                                                    } else {
                                                                        "close"
                                                                    },

                                                                    tint = if (text == selectedOption) {
                                                                        Color.White
                                                                    } else {
                                                                        colorResource(
                                                                            id = R.color.grey_filter
                                                                        )
                                                                    }

                                                                )

                                                        }
                                                    }
                                                }
                                                /*Text(
                                                text = text,

                                                color = Color.White,
                                                modifier = Modifier
                                                    .clip(
                                                        shape = RoundedCornerShape(
                                                            size = 12.dp,
                                                        ),
                                                    )
                                                    .clickable {
                                                        onSelectionChange(text)
                                                    }
                                                    .background(
                                                        if (text == selectedOption) {
                                                            Color.Magenta
                                                        } else {
                                                            Color.LightGray
                                                        }
                                                    )
                                                    .padding(
                                                        vertical = 12.dp,
                                                        horizontal = 16.dp,
                                                    ),
                                            )*/
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(color = colorResource(id = R.color.element_ligth_gray))
                                ){
                                    IconButton(onClick = { openFilter = !openFilter
                                        Log.d("trte", openFilter.toString())
                                        it()}) {
                                        Icon(
                                            imageVector = if (!openFilter) {
                                                Icons.Filled.Tune
                                            } else {
                                                Icons.Filled.Close
                                            },
                                            contentDescription = if (!openFilter) {
                                                "filter"
                                            } else {
                                                "close"
                                            },

                                            modifier = Modifier
                                                .padding(all = 22.dp)
                                                .size(34.dp),
                                            tint = Color.Red

                                        )
                                    }
                                }


                                //Text("Filter")
                            }
                        },
                        main = {
                            if(selectedOption == "Lista") {
                                LiveDataList(
                                    homepageViewModel.contracts,
                                    lifecycleOwner
                                ) { c ->
                                    ContractPreviewItem(c,
                                        {
                                            homepageViewModel.selectViewModelContract.select(c)
                                            navControllerHome.navigate(NavigationHome.DETTAGLI.name)
                                        },
                                        {
                                            homepageViewModel.selectViewModelContract.select(c)
                                            homepageViewModel.detectionViewModel.getAllDetections(c)
                                            navControllerHome.navigate(NavigationHome.RILEVAZIONI.name)
                                        })
                                }
                            }else{
                                LiveDataMap(
                                    homepageViewModel.contracts,
                                    lifecycleOwner
                                )
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)

                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(color = colorResource(id = R.color.grey_filter))
                            ) {
                                Column(
                                    modifier = Modifier.padding(all = 10.dp)
                                ) {
                                    Box(){
                                        Row(
                                            modifier = Modifier.fillMaxWidth(0.45f),
                                            horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(text = "Filtri ricerca", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(all = 12.dp))



                                        }


                                    }
                                    /*OutlinedTextField(
                                        value = value,
                                        onValueChange = {
                                            value = it
                                            homepageViewModel.addFilter(filter, value)
                                        },
                                        label = {
                                            Text(
                                                filter.labelText,
                                                fontSize = 20.sp,
                                                color = Color.White
                                            )
                                        },
                                        singleLine = true,
                                        placeholder = {
                                            Text(
                                                filter.labelText,
                                                fontSize = 20.sp,
                                                color = Color.White
                                            )
                                        },
                                        trailingIcon = {
                                            val image = Icons.Default.Clear
                                            val description = "clear"
                                        }
                                    )*/

                                        LazyColumn(

                                        ) {

                                            items(Filters.values()) { filter ->
                                                var value by rememberSaveable {
                                                    mutableStateOf(
                                                        homepageViewModel.getValueOfFilter(filter)
                                                    )
                                                }

                                                Column(
                                                    modifier = Modifier.padding(all = 6.dp)
                                                ) {
                                                    Text(filter.labelText, color = Color.White)
                                                    BasicTextField(
                                                        value = value,
                                                        onValueChange = {
                                                            value = it
                                                            homepageViewModel.addFilter(filter, value)

                                                        },
                                                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.45f)
                                                            .border(
                                                                width = 2.dp,
                                                                shape = RoundedCornerShape(12.dp),
                                                                color = Color.Gray
                                                            )

                                                            .padding(8.dp)

                                                    )
                                                }


                                                /*OutlinedTextField(
                                                    value = value,
                                                    onValueChange = {
                                                        value = it
                                                        homepageViewModel.addFilter(filter, value)
                                                    },
                                                    label = { Text(filter.labelText, fontSize = 20.sp, color = Color.White) },
                                                    singleLine = true,
                                                    placeholder = { Text(filter.labelText, fontSize = 20.sp, color = Color.White) },
                                                    trailingIcon = {
                                                        val image = Icons.Default.Clear
                                                        val description = "clear"

                                                        IconButton(onClick = {
                                                            value = ""
                                                            homepageViewModel.addFilter(filter, value)
                                                        }) {
                                                            Icon(imageVector = image, description)
                                                        }
                                                    },
                                                    keyboardOptions = KeyboardOptions(
                                                        keyboardType = KeyboardType.Password,
                                                        imeAction = ImeAction.Done
                                                    ),
                                                    modifier = Modifier.fillMaxWidth(0.45f)
                                                )*/
                                            }
                                        }
                                    Row(
                                        modifier = Modifier.fillMaxSize(0.45f),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        /*Button(
                                            modifier = Modifier.width(130.dp),
                                            onClick = { },
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Red,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)


                                        ) {
                                            Text(text = "Applica", fontSize = 20.sp)
                                        }
                                        Spacer(modifier = Modifier.width(2.dp))
                                        */

                                        Button(
                                            modifier = Modifier.width(130.dp),
                                            onClick = { homepageViewModel.filterReset() },
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Red,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp, topStart = 12.dp, bottomStart = 12.dp)


                                        ) {
                                            Text(text = "Reset", fontSize = 20.sp)
                                        }
                                    }


                                    }
                                    }




                        }
                    }
                }
                composable(NavigationHome.DETTAGLI.name) {
                    ContractDetails(
                        homepageViewModel.selectViewModelContract,
                        navControllerHome
                    )
                }
                composable(NavigationHome.RILEVAZIONI.name) {
                    DetectionPage(homepageViewModel.detectionViewModel, navControllerHome)
                }
                composable(NavigationHome.DETTAGLI_RILEVAZIONI.name) {
                    DetectionDetails(
                        homepageViewModel.detectionDetails,
                        navControllerHome
                    )
                }
                composable(NavigationHome.NUOVA_RILEVAZIONE.name) {
                    DetectionNew(homepageViewModel.detectionNewViewModel, navControllerHome)
                }
            }
            if (dialogOpen.value) {
                CustomDialog(openDialogCustom = dialogOpen)
            }
        }, bar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        dialogOpen.value = true
                        homepageViewModel.syncronize { dialogOpen.value = false }
                    },
                    modifier = Modifier
                        .padding(top = Dp(10f)),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Gray,
                        backgroundColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    Text("Sincronizza")
                }
                TextButton(
                    onClick = { navControllerHome.navigate(NavigationHome.CONTRATTI.name) },
                    modifier = Modifier
                        .padding(top = Dp(10f)),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (navControllerHome.findDestination(NavigationHome.CONTRATTI.name) != null && navControllerHome.currentDestination?.route!! == NavigationHome.CONTRATTI.name) Color.Red else Color.Gray,
                        backgroundColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    Text("Contratti")
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.mipmap.ic_launcher_foreground),
                        contentDescription = "Germini App",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                    )
                }
            }
        }, sidePanel = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { loginViewModel.logout() },
                    modifier = Modifier
                        .padding(horizontal = Dp(10f))
                        .fillMaxWidth(),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    Text("Logout")
                }
            }
        },
            sidePanelBar = {
                Text(
                    text = user,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            })
    }
}

@Preview(widthDp = 400, heightDp = 500)
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
                success(User(1, user, "test", "test", ""))
            }

            override suspend fun loginLastUser(
                success: (User) -> Unit,
                error: (String) -> Unit
            ) {
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
                success(listOf())
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
        val detailsViewModel = SelectViewModelContract()
        val loginViewModel = LoginViewModel(LoginUseCase(repository))
        val detectionViewModel =
            DetectionViewModel(SelectViewModelDetection(), DetectionUseCase(repository))
        loginViewModel.login("test", "test") { }
        val detectionNewViewModel = DetectionNewViewModel(
            loginViewModel,
            detailsViewModel,
            detectionViewModel,
            DetectionUseCase(repository),
            Application()
        )

        val detectionDetailsViewModel = DetectionDetailsViewModel(
            SelectViewModelDetection(),
            detectionViewModel,
            DetectionUseCase(repository),
            Application()
        )
        val homepageViewModel =
            HomePageViewModel(
                loginViewModel,
                detailsViewModel,
                detectionViewModel,
                detectionNewViewModel,
                detectionDetailsViewModel,
                LoadContractUseCase(repository),
                DetectionUseCase(repository)
            )
        HomePage(navController, homepageViewModel, loginViewModel)
    }
}
