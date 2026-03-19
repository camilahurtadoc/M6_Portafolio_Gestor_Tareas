package com.tecsup.taskmanagerpro.domain.usecase

import com.google.firebase.Timestamp
import com.tecsup.taskmanagerpro.data.model.Task
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Caso de uso para actualizar una tarea existente
 * Parte de la capa de dominio (MVVM)
 * Encapsula la lógica de negocio de la actualización de tareas
 */
class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Ejecuta el caso de uso para actualizar una tarea
     * Valida los datos antes de llamar al repositorio
     */
    suspend operator fun invoke(
        taskId: String,
        title: String,
        description: String,
        priority: String,
        status: String,
        deadline: Timestamp
    ): Result<Unit> {
        // Validaciones
        if (taskId.isBlank()) {
            return Result.failure(Exception("ID de tarea inválido"))
        }

        if (title.isBlank()) {
            return Result.failure(Exception("El título es requerido"))
        }

        if (title.length < 3) {
            return Result.failure(Exception("El título debe tener al menos 3 caracteres"))
        }

        if (title.length > 100) {
            return Result.failure(Exception("El título no puede tener más de 100 caracteres"))
        }

        if (description.length > 500) {
            return Result.failure(Exception("La descripción no puede tener más de 500 caracteres"))
        }

        // Obtener la tarea actual para preservar algunos campos
        val currentTaskResult = taskRepository.getTaskById(taskId)
        if (currentTaskResult.isFailure) {
            return Result.failure(currentTaskResult.exceptionOrNull() ?: Exception("Error al obtener la tarea"))
        }

        val currentTask = currentTaskResult.getOrNull() ?: return Result.failure(Exception("Tarea no encontrada"))

        // Crear la tarea actualizada
        val updatedTask = Task(
            id = taskId,
            title = title.trim(),
            description = description.trim(),
            priority = priority,
            status = status,
            deadline = deadline,
            userId = currentTask.userId,
            createdAt = currentTask.createdAt,
            updatedAt = Timestamp.now()
        )

        return taskRepository.updateTask(updatedTask)
    }
}
