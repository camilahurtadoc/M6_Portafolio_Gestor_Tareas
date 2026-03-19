package com.tecsup.taskmanagerpro.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.taskmanagerpro.data.model.TaskPriority
import com.tecsup.taskmanagerpro.ui.theme.PriorityHigh
import com.tecsup.taskmanagerpro.ui.theme.PriorityLow
import com.tecsup.taskmanagerpro.ui.theme.PriorityMedium

/**
 * Chip que muestra la prioridad de una tarea
 * @param priority Prioridad de la tarea
 * @param modifier Modificador de Compose
 */
@Composable
fun PriorityChip(
    priority: TaskPriority,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) = when (priority) {
        TaskPriority.HIGH -> PriorityHigh to Color.White
        TaskPriority.MEDIUM -> PriorityMedium to Color.White
        TaskPriority.LOW -> PriorityLow to Color.White
    }

    AssistChip(
        onClick = { /* No hace nada, es solo visual */ },
        label = {
            Text(
                text = priority.displayName,
                style = MaterialTheme.typography.labelSmall
            )
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor,
            labelColor = contentColor
        ),
        border = null
    )
}

/**
 * Chip que muestra el estado de una tarea
 * @param status Estado de la tarea
 * @param modifier Modificador de Compose
 */
@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor, displayText) = when (status) {
        "COMPLETED" -> Triple(
            com.tecsup.taskmanagerpro.ui.theme.StatusCompleted,
            Color.White,
            "Completada"
        )
        "IN_PROGRESS" -> Triple(
            com.tecsup.taskmanagerpro.ui.theme.StatusInProgress,
            Color.White,
            "En Progreso"
        )
        else -> Triple(
            com.tecsup.taskmanagerpro.ui.theme.StatusPending,
            Color.White,
            "Pendiente"
        )
    }

    AssistChip(
        onClick = { /* No hace nada, es solo visual */ },
        label = {
            Text(
                text = displayText,
                style = MaterialTheme.typography.labelSmall
            )
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor,
            labelColor = contentColor
        ),
        border = null
    )
}
