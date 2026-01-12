package com.example.gymknight.presentation.addSet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddSetScreen() {
    AddSetScreenContent()
}

@Composable
fun AddSetScreenContent(){

    Scaffold {innerPaddings ->
        Box(
            modifier = Modifier
                .padding(innerPaddings)
                .fillMaxSize()
                .background(Color.Blue)
        )
    }
}

@Composable
@Preview
fun AddSetScreenPreview(){
    AddSetScreenContent()
}