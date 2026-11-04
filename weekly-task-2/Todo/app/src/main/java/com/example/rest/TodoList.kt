package com.example.rest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TodoList(
    todos: List<Todo>
) {
    LazyColumn(
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = todos,
            key = { todo -> todo.id }
        ) { todo ->
            TodoCard(
                todo = todo
            )
        }
    }
}
