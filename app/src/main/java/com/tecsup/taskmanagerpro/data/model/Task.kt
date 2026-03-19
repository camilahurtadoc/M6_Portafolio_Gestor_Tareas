package com.tecsup.taskmanagerpro.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

/**
 * Modelo de datos para una Tarea
 * Se usa @DocumentId para que Firestore maneje automáticamente el ID del documento
 */
data class Task(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priority: String = TaskPriority.MEDIUM.name,
    val status: String = TaskStatus.PENDING.name,
    val deadline: Timestamp = Timestamp.now(),
    val userId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
) {
    /**
     * Convierte el Task a un Map para guardarlo en Firestore
     */
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            "title" to title,
            "description" to description,
            "priority" to priority,
            "status" to status,
            "deadline" to deadline,
            "userId" to userId,
            "createdAt" to createdAt,
            "updatedAt" to Timestamp.now()
        )
    }

    /**
     * Obtiene la prioridad como enum
     */
    fun getPriorityEnum(): TaskPriority {
        return TaskPriority.fromString(priority)
    }

    /**
     * Obtiene el estado como enum
     */
    fun getStatusEnum(): TaskStatus {
        return TaskStatus.fromString(status)
    }

    /**
     * Verifica si la tarea está completada
     */
    fun isCompleted(): Boolean {
        return status == TaskStatus.COMPLETED.name
    }

    companion object {
        /**
         * Crea un Task desde un Map de Firestore
         */
        fun fromMap(id: String, map: Map<String, Any>): Task {
            return Task(
                id = id,
                title = map["title"] as? String ?: "",
                description = map["description"] as? String ?: "",
                priority = map["priority"] as? String ?: TaskPriority.MEDIUM.name,
                status = map["status"] as? String ?: TaskStatus.PENDING.name,
                deadline = map["deadline"] as? Timestamp ?: Timestamp.now(),
                userId = map["userId"] as? String ?: "",
                createdAt = map["createdAt"] as? Timestamp ?: Timestamp.now(),
                updatedAt = map["updatedAt"] as? Timestamp ?: Timestamp.now()
            )
        }
    }
}
