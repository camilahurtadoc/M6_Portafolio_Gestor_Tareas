package com.tecsup.taskmanagerpro.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tecsup.taskmanagerpro.data.model.Task
import com.tecsup.taskmanagerpro.data.model.TaskStatus
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de tareas usando Firestore
 * Esta clase es parte de la capa de datos (MVVM)
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : TaskRepository {

    companion object {
        private const val TASKS_COLLECTION = "tasks"
    }

    /**
     * Obtiene el ID del usuario actual
     */
    private fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
    }

    /**
     * Obtiene todas las tareas del usuario actual en tiempo real
     * Usa un Flow para emitir actualizaciones cada vez que cambian los datos en Firestore
     */
    override fun getTasks(): Flow<Result<List<Task>>> = callbackFlow {
        val userId = try {
            getCurrentUserId()
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
            return@callbackFlow
        }

        val subscription = firestore.collection(TASKS_COLLECTION)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(Exception("Error al obtener tareas: ${error.message}")))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { document ->
                        try {
                            document.toObject(Task::class.java)?.copy(id = document.id)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(Result.success(tasks))
                }
            }

        awaitClose { subscription.remove() }
    }

    /**
     * Obtiene una tarea específica por su ID
     */
    override suspend fun getTaskById(taskId: String): Result<Task> {
        return try {
            val userId = getCurrentUserId()
            val document = firestore.collection(TASKS_COLLECTION)
                .document(taskId)
                .get()
                .await()

            if (document.exists()) {
                val task = document.toObject(Task::class.java)?.copy(id = document.id)
                if (task != null && task.userId == userId) {
                    Result.success(task)
                } else {
                    Result.failure(Exception("No tienes permiso para ver esta tarea"))
                }
            } else {
                Result.failure(Exception("Tarea no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener la tarea: ${e.message}"))
        }
    }

    /**
     * Crea una nueva tarea en Firestore
     */
    override suspend fun createTask(task: Task): Result<String> {
        return try {
            val userId = getCurrentUserId()
            val taskWithUser = task.copy(
                userId = userId,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )

            val documentRef = firestore.collection(TASKS_COLLECTION)
                .add(taskWithUser.toMap())
                .await()

            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(Exception("Error al crear la tarea: ${e.message}"))
        }
    }

    /**
     * Actualiza una tarea existente
     */
    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            
            // Verificar que la tarea pertenece al usuario
            val existingTask = firestore.collection(TASKS_COLLECTION)
                .document(task.id)
                .get()
                .await()

            if (!existingTask.exists()) {
                return Result.failure(Exception("Tarea no encontrada"))
            }

            val existingUserId = existingTask.getString("userId")
            if (existingUserId != userId) {
                return Result.failure(Exception("No tienes permiso para editar esta tarea"))
            }

            // Actualizar la tarea
            val updatedTask = task.toMap().toMutableMap()
            updatedTask["updatedAt"] = Timestamp.now()

            firestore.collection(TASKS_COLLECTION)
                .document(task.id)
                .update(updatedTask)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar la tarea: ${e.message}"))
        }
    }

    /**
     * Elimina una tarea
     */
    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            
            // Verificar que la tarea pertenece al usuario
            val existingTask = firestore.collection(TASKS_COLLECTION)
                .document(taskId)
                .get()
                .await()

            if (!existingTask.exists()) {
                return Result.failure(Exception("Tarea no encontrada"))
            }

            val existingUserId = existingTask.getString("userId")
            if (existingUserId != userId) {
                return Result.failure(Exception("No tienes permiso para eliminar esta tarea"))
            }

            // Eliminar la tarea
            firestore.collection(TASKS_COLLECTION)
                .document(taskId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al eliminar la tarea: ${e.message}"))
        }
    }

    /**
     * Marca una tarea como completada o pendiente
     */
    override suspend fun toggleTaskStatus(taskId: String, isCompleted: Boolean): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            
            // Verificar que la tarea pertenece al usuario
            val existingTask = firestore.collection(TASKS_COLLECTION)
                .document(taskId)
                .get()
                .await()

            if (!existingTask.exists()) {
                return Result.failure(Exception("Tarea no encontrada"))
            }

            val existingUserId = existingTask.getString("userId")
            if (existingUserId != userId) {
                return Result.failure(Exception("No tienes permiso para modificar esta tarea"))
            }

            // Actualizar el estado
            val newStatus = if (isCompleted) TaskStatus.COMPLETED.name else TaskStatus.PENDING.name
            
            firestore.collection(TASKS_COLLECTION)
                .document(taskId)
                .update(
                    mapOf(
                        "status" to newStatus,
                        "updatedAt" to Timestamp.now()
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al cambiar el estado: ${e.message}"))
        }
    }
}
