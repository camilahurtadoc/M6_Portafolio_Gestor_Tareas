package com.tecsup.taskmanagerpro.data.model

/**
 * Enumeración que representa el estado de una tarea
 */
enum class TaskStatus(val displayName: String) {
    PENDING("Pendiente"),
    IN_PROGRESS("En Progreso"),
    COMPLETED("Completada");

    companion object {
        fun fromString(value: String): TaskStatus {
            return values().find { it.name == value } ?: PENDING
        }
    }
}
