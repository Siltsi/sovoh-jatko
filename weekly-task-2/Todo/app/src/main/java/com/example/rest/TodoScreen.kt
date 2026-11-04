package com.example.rest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview(showBackground = true)
@Composable
fun TodoScreen( viewModel: TodoViewModel = viewModel() ) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Todo Viewer",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
        when {
            viewModel.uiState.isLoading -> {
                CircularProgressIndicator()
            }
            viewModel.uiState.errorMessage.isNotEmpty() -> {
                Button(
                    onClick = {
                        viewModel.loadTodos()
                    }
                ) {
                    Text("Reload")
                }

                Text(
                    text = viewModel.uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
            viewModel.uiState.todos.isNotEmpty() -> {
                Button(
                    onClick = {
                        viewModel.loadTodos()
                    }
                ) {
                    Text("Reload")
                }

                Text("Tasks: ${viewModel.uiState.todos.size}")

                Spacer(modifier = Modifier.height(8.dp))

                TodoList(viewModel.uiState.todos)
            }
            else -> {
                Button(
                    onClick = {
                        viewModel.loadTodos()
                    }
                ) {
                    Text("Load todo")
                }
            }
        }
    }
}