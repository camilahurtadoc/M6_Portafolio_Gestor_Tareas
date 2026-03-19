package com.tecsup.taskmanagerpro.domain.usecase

import com.google.firebase.Timestamp
import com.tecsup.taskmanagerpro.data.model.Task
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Caso de uso para crear una nueva tarea
 * Parte de la capa de dominio (MVVM)
 * Encapsula la lógica de negocio de la creación de tareas
 */
class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Ejecuta el caso de uso para crear una tarea
     * Valida los datos antes de llamar al repositorio
     */
    suspend operator fun invoke(
        title: String,
        description: String,
        priority: String,
        status: String,
        deadline: Timestamp
    ): Result<String> {
        // Validaciones
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

        // Validar que la fecha límite no sea en el pasado
        val now = Timestamp.now()
        if (deadline.toDate().before(now.toDate())) {
            return Result.failure(Exception("La fecha límite no puede ser en el pasado"))
        }

        // Crear la tarea
        val task = Task(
            title = title.trim(),
            description = description.trim(),
            priority = priority,
            status = status,
            deadline = deadline,
            createdAt = now,
            updatedAt = now
        )

        return taskRepository.createTask(task)
    }
}
