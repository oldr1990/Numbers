package com.example.numbers.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.numbers.navigation.Routes
import com.example.numbers.ui.components.NBProgressBar
import com.example.numbers.ui.components.NBTopBar


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.screenEvent.collect {
            when (it) {
                is HomeScreenViewModel.ScreenEvent.Navigate -> {
                    navController.navigate(it.destination)
                }
                is HomeScreenViewModel.ScreenEvent.Snackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    Icons.Default.ArrowForward,
                    "Nav to top",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(0.dp)
                        .size(48.dp)
                )
            }
        },
        topBar = {
            NBTopBar("Numbers", navController, false)
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(elevation = 4.dp, modifier = Modifier.padding(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(1f),
                                value = state.value.search,
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.NumberPassword
                                ),
                                keyboardActions = KeyboardActions(onDone = { viewModel.startSearch() }),
                                label = {
                                    Text(text = "Enter your number")
                                },
                                onValueChange = { search -> viewModel.searchChanged(search.take(10)) },
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            OutlinedButton(
                                elevation = ButtonDefaults.elevation(),
                                onClick = { viewModel.startSearch() }
                            ) {
                                Text(text = "Get fact")
                            }
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = ButtonDefaults.elevation(),
                            onClick = { viewModel.startSearch() }
                        ) {
                            Text(text = "Get fact about random number")
                        }

                    }
                }
            }

            if (state.value.list.isEmpty()) item {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Search history is empty",
                    style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.primaryVariant)
                )
            } else items(state.value.list.size) { index ->
                Text(
                    text = state.value.list[index].number.toString(),
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.DETAILS + state.value.list[index].toJson)

                    })
            }
        }
        if (state.value.isLoading) NBProgressBar()
    }
}