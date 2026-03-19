package com.tecsup.taskmanagerpro.data.model

/**
 * Enumeración que representa los niveles de prioridad de una tarea
 */
enum class TaskPriority(val displayName: String) {
    HIGH("Alta"),
    MEDIUM("Media"),
    LOW("Baja");

    companion object {
        fun fromString(value: String): TaskPriority {
            return values().find { it.name == value } ?: MEDIUM
        }
    }
}
