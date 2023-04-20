package com.example.numbers.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.numbers.R
import com.example.numbers.models.NumberType
import com.example.numbers.navigation.Routes
import com.example.numbers.ui.components.NBProgressBar
import com.example.numbers.ui.components.NBTopBar
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    val showButton = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.screenEvent.collect {
            when (it) {
                is HomeScreenViewModel.ScreenEvent.Navigate -> {
                    keyboard?.hide()
                    navController.navigate(it.destination)
                }
                is HomeScreenViewModel.ScreenEvent.Snackbar -> {
                    keyboard?.hide()
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier,
        floatingActionButton = {
            AnimatedVisibility(visible = showButton.value, enter = fadeIn(), exit = fadeOut()) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    elevation = FloatingActionButtonDefaults.elevation(4.dp),
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        stringResource(id = R.string.scroll_to_top),
                        tint = Color.White,
                        modifier = Modifier
                            .padding(0.dp)
                            .size(48.dp)
                    )
                }
            }
        },
        topBar = {
            NBTopBar(stringResource(id = R.string.app_name), navController, false)
        },
    ) {
        LazyColumn(
            state = listState,
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
                                    Text(text = stringResource(id = R.string.enter_your_number))
                                },
                                onValueChange = { search -> viewModel.searchChanged(search.take(10)) },
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            OutlinedButton(
                                elevation = ButtonDefaults.elevation(),
                                onClick = { viewModel.startSearch() }
                            ) {
                                Text(text = stringResource(id = R.string.get_fact))
                            }
                        }
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NumberType.values().forEach { type ->
                                FilterChip(
                                    onClick = {
                                        viewModel.typeChanged(type)
                                    },
                                    selected = state.value.type == type,
                                    colors = ChipDefaults.filterChipColors(
                                        selectedBackgroundColor = MaterialTheme.colors.primary,
                                        selectedContentColor = MaterialTheme.colors.background,
                                    )
                                ) {
                                    Text(text = type.name)
                                }
                            }
                        }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = ButtonDefaults.elevation(),
                            onClick = { viewModel.startSearch(true) }
                        ) {
                            Text(text = stringResource(id = R.string.get_fact_random))
                        }

                    }
                }
            }

            if (state.value.list.isEmpty()) item {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = stringResource(id = R.string.empty_cache),
                    style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.primaryVariant)
                )
            } else items(state.value.list.size) { index ->
                Card(
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            navController.navigate(Routes.DETAILS + state.value.list[index].toJson)
                        }, elevation = 4.dp
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.value.list[index].number,
                            style = MaterialTheme.typography.h4,
                        )
                        FilterChip(
                            onClick = { navController.navigate(Routes.DETAILS + state.value.list[index].toJson) },
                            enabled = false,
                            selected = true,
                            colors = ChipDefaults.filterChipColors(
                                selectedBackgroundColor = MaterialTheme.colors.primary,
                                selectedContentColor = MaterialTheme.colors.background,
                            )
                        ) {
                            Text(text = state.value.list[index].type.name)
                        }
                    }
                }

            }
        }
        if (state.value.isLoading) {
            keyboard?.hide()
            NBProgressBar()
        }
    }
}