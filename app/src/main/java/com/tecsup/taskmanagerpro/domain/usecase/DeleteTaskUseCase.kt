package com.tecsup.taskmanagerpro.domain.usecase

import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Caso de uso para eliminar una tarea
 * Parte de la capa de dominio (MVVM)
 * Encapsula la lógica de negocio de la eliminación de tareas
 */
class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Ejecuta el caso de uso para eliminar una tarea
     * Valida los datos antes de llamar al repositorio
     */
    suspend operator fun invoke(taskId: String): Result<Unit> {
        // Validación
        if (taskId.isBlank()) {
            return Result.failure(Exception("ID de tarea inválido"))
        }

        // Verificar que la tarea existe antes de eliminar
        val taskResult = taskRepository.getTaskById(taskId)
        if (taskResult.isFailure) {
            return Result.failure(taskResult.exceptionOrNull() ?: Exception("Error al verificar la tarea"))
        }

        // Eliminar la tarea
        return taskRepository.deleteTask(taskId)
    }
}
