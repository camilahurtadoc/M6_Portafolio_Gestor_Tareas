package com.tecsup.taskmanagerpro.ui.tasks.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.tecsup.taskmanagerpro.data.model.TaskPriority
import com.tecsup.taskmanagerpro.data.model.TaskStatus
import com.tecsup.taskmanagerpro.ui.components.CustomTextField
import com.tecsup.taskmanagerpro.ui.components.LoadingDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Pantalla de editar tarea
 * @param onNavigateBack Callback para volver atrás
 * @param onTaskUpdated Callback cuando se actualiza la tarea
 * @param viewModel ViewModel de editar tarea
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    onNavigateBack: () -> Unit,
    onTaskUpdated: () -> Unit,
    viewModel: EditTaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    // Manejo de efectos secundarios
    LaunchedEffect(uiState.isTaskUpdated) {
        if (uiState.isTaskUpdated) {
            onTaskUpdated()
            viewModel.resetTaskUpdated()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tarea") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoadingTask) {
            // Estado de carga inicial
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de título
                CustomTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    label = "Título",
                    placeholder = "Título de la tarea",
                    imeAction = ImeAction.Next,
                    enabled = !uiState.isLoading
                )

                // Campo de descripción
                CustomTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = "Descripción",
                    placeholder = "Descripción de la tarea (opcional)",
                    singleLine = false,
                    maxLines = 4,
                    imeAction = ImeAction.Default,
                    enabled = !uiState.isLoading
                )

                // Selector de prioridad
                PriorityDropdown(
                    selectedPriority = uiState.priority,
                    onPrioritySelected = viewModel::onPriorityChange,
                    enabled = !uiState.isLoading
                )

                // Selector de estado
                StatusDropdown(
                    selectedStatus = uiState.status,
                    onStatusSelected = viewModel::onStatusChange,
                    enabled = !uiState.isLoading
                )

                // Selector de fecha límite
                DeadlinePicker(
                    deadline = uiState.deadline,
                    onDeadlineSelected = viewModel::onDeadlineChange,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de actualizar
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.updateTask()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = "Actualizar Tarea",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    // Diálogo de carga
    if (uiState.isLoading) {
        LoadingDialog(message = "Actualizando tarea...")
    }
}

/**
 * Dropdown para seleccionar prioridad
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityDropdown(
    selectedPriority: TaskPriority,
    onPrioritySelected: (TaskPriority) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPriority.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Prioridad") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TaskPriority.values().forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.displayName) },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Dropdown para seleccionar estado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropdown(
    selectedStatus: TaskStatus,
    onStatusSelected: (TaskStatus) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedStatus.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Estado") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TaskStatus.values().forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.displayName) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Selector de fecha límite
 */
@Composable
private fun DeadlinePicker(
    deadline: Timestamp,
    onDeadlineSelected: (Timestamp) -> Unit,
    enabled: Boolean
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val deadlineText = dateFormat.format(deadline.toDate())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) {
                // Aquí en una implementación real, se abriría un DatePicker
                // Por simplicidad, incrementamos un día
                val calendar = Calendar.getInstance()
                calendar.time = deadline.toDate()
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                onDeadlineSelected(Timestamp(calendar.time))
            }
    ) {
        OutlinedTextField(
            value = deadlineText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha Límite") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
    }
}
