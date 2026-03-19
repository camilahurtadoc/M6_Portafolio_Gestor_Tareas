package com.tecsup.taskmanagerpro.ui.tasks.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.tecsup.taskmanagerpro.data.model.TaskPriority
import com.tecsup.taskmanagerpro.data.model.TaskStatus
import com.tecsup.taskmanagerpro.domain.usecase.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de crear tarea
 * Parte de la capa de presentación (MVVM)
 * Gestiona el estado y la lógica de la creación de tareas
 */
@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(CreateTaskUiState())
    
    // Estado público inmutable para la UI
    val uiState: StateFlow<CreateTaskUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el título en el estado
     */
    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    /**
     * Actualiza la descripción en el estado
     */
    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description, errorMessage = null) }
    }

    /**
     * Actualiza la prioridad en el estado
     */
    fun onPriorityChange(priority: TaskPriority) {
        _uiState.update { it.copy(priority = priority, errorMessage = null) }
    }

    /**
     * Actualiza el estado en el estado
     */
    fun onStatusChange(status: TaskStatus) {
        _uiState.update { it.copy(status = status, errorMessage = null) }
    }

    /**
     * Actualiza la fecha límite en el estado
     */
    fun onDeadlineChange(deadline: Timestamp) {
        _uiState.update { it.copy(deadline = deadline, errorMessage = null) }
    }

    /**
     * Crea una nueva tarea
     */
    fun createTask() {
        val currentState = _uiState.value
        
        // Validación básica en el ViewModel
        if (currentState.title.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "El título es requerido")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = createTaskUseCase(
                title = currentState.title.trim(),
                description = currentState.description.trim(),
                priority = currentState.priority.name,
                status = currentState.status.name,
                deadline = currentState.deadline
            )
            
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isTaskCreated = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error al crear la tarea"
                        )
                    }
                }
            )
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Reinicia el estado de creación exitosa
     */
    fun resetTaskCreated() {
        _uiState.update { it.copy(isTaskCreated = false) }
    }
}
