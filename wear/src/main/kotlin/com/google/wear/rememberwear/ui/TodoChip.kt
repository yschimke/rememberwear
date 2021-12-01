package com.google.wear.rememberwear.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import com.google.wear.rememberwear.db.Todo

@Composable
fun TodoChip(
    modifier: Modifier = Modifier,
    todo: Todo,
) {
    Text(todo.toString())
}