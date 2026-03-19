package com.tecsup.taskmanagerpro.ui.tasks.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.taskmanagerpro.data.model.Task
import com.tecsup.taskmanagerpro.domain.repository.AuthRepository
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import com.tecsup.taskmanagerpro.domain.usecase.DeleteTaskUseCase
import com.tecsup.taskmanagerpro.domain.usecase.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de lista de tareas
 * Parte de la capa de presentación (MVVM)
 * Gestiona el estado y la lógica de la lista de tareas
 */
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(TaskListUiState())
    
    // Estado público inmutable para la UI
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    /**
     * Carga las tareas del usuario en tiempo real
     */
    private fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getTasksUseCase().collect { result ->
                result.fold(
                    onSuccess = { tasks ->
                        _uiState.update {
                            it.copy(
                                tasks = tasks,
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = exception.message ?: "Error al cargar las tareas"
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Refresca la lista de tareas
     */
    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        // El Flow se actualiza automáticamente
    }

    /**
     * Muestra el diálogo de confirmación de eliminación
     */
    fun showDeleteConfirmation(task: Task) {
        _uiState.update {
            it.copy(
                taskToDelete = task,
                showDeleteConfirmation = true
            )
        }
    }

    /**
     * Oculta el diálogo de confirmación de eliminación
     */
    fun hideDeleteConfirmation() {
        _uiState.update {
            it.copy(
                taskToDelete = null,
                showDeleteConfirmation = false
            )
        }
    }

    /**
     * Elimina una tarea
     */
    fun deleteTask() {
        val taskToDelete = _uiState.value.taskToDelete ?: return

        viewModelScope.launch {
            hideDeleteConfirmation()
            
            val result = deleteTaskUseCase(taskToDelete.id)
            
            result.fold(
                onSuccess = {
                    // La lista se actualiza automáticamente gracias al Flow
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            errorMessage = exception.message ?: "Error al eliminar la tarea"
                        )
                    }
                }
            )
        }
    }

    /**
     * Cambia el estado de completado de una tarea
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val result = taskRepository.toggleTaskStatus(
                taskId = task.id,
                isCompleted = !task.isCompleted()
            )
            
            result.fold(
                onSuccess = {
                    // La lista se actualiza automáticamente gracias al Flow
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            errorMessage = exception.message ?: "Error al actualizar la tarea"
                        )
                    }
                }
            )
        }
    }

    /**
     * Cierra sesión del usuario
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
