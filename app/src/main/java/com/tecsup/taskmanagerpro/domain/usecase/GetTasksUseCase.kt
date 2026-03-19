package com.tecsup.taskmanagerpro.domain.usecase

import com.tecsup.taskmanagerpro.data.model.Task
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todas las tareas
 * Parte de la capa de dominio (MVVM)
 * Obtiene las tareas en tiempo real desde Firestore
 */
class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Ejecuta el caso de uso para obtener tareas
     * @return Flow que emite la lista de tareas cada vez que hay cambios
     */
    operator fun invoke(): Flow<Result<List<Task>>> {
        return taskRepository.getTasks()
    }
}
