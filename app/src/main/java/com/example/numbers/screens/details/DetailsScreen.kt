package com.example.numbers.screens.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.numbers.models.NumberItem
import com.example.numbers.ui.components.NBTopBar


@Composable
fun DetailsScreen(navController: NavController, number: NumberItem) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            NBTopBar(number.number, navController)
        },
    ) {
        Card(
            Modifier
                .padding(it)
                .fillMaxWidth()
                .padding(16.dp), elevation = 4.dp
        ) {
            Text(
                text = number.description,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}