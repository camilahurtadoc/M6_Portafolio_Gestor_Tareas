package com.tecsup.taskmanagerpro.domain.repository

import com.tecsup.taskmanagerpro.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de tareas
 * Define el contrato para las operaciones CRUD de tareas
 * Esta interfaz es parte de la capa de dominio (MVVM)
 */
interface TaskRepository {
    /**
     * Obtiene todas las tareas del usuario actual en tiempo real
     * @return Flow que emite la lista de tareas cada vez que hay cambios
     */
    fun getTasks(): Flow<Result<List<Task>>>

    /**
     * Obtiene una tarea específica por su ID
     * @param taskId ID de la tarea
     * @return Result con la Task si existe o un error
     */
    suspend fun getTaskById(taskId: String): Result<Task>

    /**
     * Crea una nueva tarea
     * @param task Tarea a crear
     * @return Result con el ID de la tarea creada o un error
     */
    suspend fun createTask(task: Task): Result<String>

    /**
     * Actualiza una tarea existente
     * @param task Tarea con los datos actualizados
     * @return Result con Unit si es exitoso o un error
     */
    suspend fun updateTask(task: Task): Result<Unit>

    /**
     * Elimina una tarea
     * @param taskId ID de la tarea a eliminar
     * @return Result con Unit si es exitoso o un error
     */
    suspend fun deleteTask(taskId: String): Result<Unit>

    /**
     * Marca una tarea como completada o pendiente
     * @param taskId ID de la tarea
     * @param isCompleted Estado de completado
     * @return Result con Unit si es exitoso o un error
     */
    suspend fun toggleTaskStatus(taskId: String, isCompleted: Boolean): Result<Unit>
}
