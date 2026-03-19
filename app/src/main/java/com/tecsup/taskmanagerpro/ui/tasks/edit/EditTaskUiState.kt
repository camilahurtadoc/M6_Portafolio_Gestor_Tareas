package com.tecsup.taskmanagerpro.ui.tasks.edit

import com.google.firebase.Timestamp
import com.tecsup.taskmanagerpro.data.model.TaskPriority
import com.tecsup.taskmanagerpro.data.model.TaskStatus

/**
 * Estado de la UI de editar tarea
 * Representa todos los posibles estados de la pantalla de editar tarea
 */
data class EditTaskUiState(
    val taskId: String = "",
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val status: TaskStatus = TaskStatus.PENDING,
    val deadline: Timestamp = Timestamp.now(),
    val isLoading: Boolean = false,
    val isLoadingTask: Boolean = false,
    val errorMessage: String? = null,
    val isTaskUpdated: Boolean = false
)
