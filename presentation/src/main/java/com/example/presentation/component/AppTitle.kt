package com.example.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppTitle(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign? = null,
    textStyle: TextStyle
) {
    Text(
        modifier = modifier,
        text = text,
        style = textStyle,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = textAlign
    )
}