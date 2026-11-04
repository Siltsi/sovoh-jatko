package com.example.rest

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class TodoScreenUIState(
    val isLoading: Boolean = false,
    val todos: List<Todo> = emptyList(),
    val errorMessage: String = ""
)

class TodoViewModel : ViewModel() {
    var uiState by mutableStateOf(TodoScreenUIState())
        private set

    fun loadTodos() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = "")

            delay(2000.milliseconds)

            try {
                val fetchedTodos = RetrofitInstance.api.getTodos()
                uiState = uiState.copy(todos = fetchedTodos, isLoading = false)
            } catch (e: Exception) {
                Log.e("TodoViewModel", "Error loading todos", e)
                uiState = uiState.copy(
                    errorMessage = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}