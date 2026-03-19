package com.tecsup.taskmanagerpro.ui.tasks.list

import com.tecsup.taskmanagerpro.data.model.Task

/**
 * Estado de la UI de la lista de tareas
 * Representa todos los posibles estados de la pantalla de lista de tareas
 */
data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val taskToDelete: Task? = null,
    val showDeleteConfirmation: Boolean = false
)
