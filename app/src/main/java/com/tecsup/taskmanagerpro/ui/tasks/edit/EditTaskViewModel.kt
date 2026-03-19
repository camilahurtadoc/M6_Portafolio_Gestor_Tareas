package com.tecsup.taskmanagerpro.ui.tasks.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.tecsup.taskmanagerpro.data.model.TaskPriority
import com.tecsup.taskmanagerpro.data.model.TaskStatus
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import com.tecsup.taskmanagerpro.domain.usecase.UpdateTaskUseCase
import com.tecsup.taskmanagerpro.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de editar tarea
 * Parte de la capa de presentación (MVVM)
 * Gestiona el estado y la lógica de la edición de tareas
 */
@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(EditTaskUiState())
    
    // Estado público inmutable para la UI
    val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()

    private val taskId: String = checkNotNull(savedStateHandle[Screen.TASK_ID_ARG])

    init {
        loadTask()
    }

    /**
     * Carga la tarea a editar
     */
    private fun loadTask() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingTask = true) }
            
            val result = taskRepository.getTaskById(taskId)
            
            result.fold(
                onSuccess = { task ->
                    _uiState.update {
                        it.copy(
                            taskId = task.id,
                            title = task.title,
                            description = task.description,
                            priority = task.getPriorityEnum(),
                            status = task.getStatusEnum(),
                            deadline = task.deadline,
                            isLoadingTask = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoadingTask = false,
                            errorMessage = exception.message ?: "Error al cargar la tarea"
                        )
                    }
                }
            )
        }
    }

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
     * Actualiza la tarea
     */
    fun updateTask() {
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
            
            val result = updateTaskUseCase(
                taskId = currentState.taskId,
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
                            isTaskUpdated = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error al actualizar la tarea"
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
     * Reinicia el estado de actualización exitosa
     */
    fun resetTaskUpdated() {
        _uiState.update { it.copy(isTaskUpdated = false) }
    }
}
