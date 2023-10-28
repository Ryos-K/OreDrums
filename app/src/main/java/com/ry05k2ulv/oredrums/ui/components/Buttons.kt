package com.ry05k2ulv.oredrums.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = ""
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings Button: $contentDescription"
        )
    }
}

@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = ""
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Button: $contentDescription"
        )
    }
}

@Composable
fun RemoveFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = ""
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove Button: $contentDescription"
        )
    }
}

@Composable
fun EditToggleIconButton(
    editMode: Boolean,
    onEditClick: () -> Unit,
    onEditOffClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = ""
) {
    IconButton(
        onClick = if (editMode) onEditOffClick else onEditClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (editMode) Icons.Default.EditOff else Icons.Default.Edit,
            contentDescription = "Edit Button: $contentDescription"
        )
    }
}

@Composable
fun ArrowBackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = ""
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Arrow Back Button: $contentDescription"
        )
    }
}

@Composable
fun OpenTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = "Open",
            style = MaterialTheme.typography.labelLarge
        )
    }
}