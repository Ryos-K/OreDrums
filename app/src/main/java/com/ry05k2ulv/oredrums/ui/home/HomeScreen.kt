package com.ry05k2ulv.oredrums.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ry05k2ulv.oredrums.model.DrumsProperty
import com.ry05k2ulv.oredrums.utils.flip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun HomeRoute(
    onPropertyClick: (id: Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        onPropertyClick = onPropertyClick,
        onAddProperty = viewModel::insertProperty,
        onRemoveProperty = viewModel::deletePropertyById
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onPropertyClick: (id: Int) -> Unit,
    onAddProperty: (title: String) -> Unit,
    onRemoveProperty: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        when (uiState) {
            HomeUiState.Loading -> LoadingScreen()
            is HomeUiState.Success -> SuccessScreen(
                modifier = Modifier
                    .fillMaxSize(),
                properties = uiState.properties,
                onPropertyClick = onPropertyClick,
                onAddProperty = onAddProperty,
                onRemoveProperty = onRemoveProperty
            )
        }
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier,
    properties: List<DrumsProperty>,
    onPropertyClick: (id: Int) -> Unit,
    onAddProperty: (title: String) -> Unit,
    onRemoveProperty: (id: Int) -> Unit
) {
    var showAddPropertyDialog by remember { mutableStateOf(false) }

    var propertyShouldShow by remember { mutableStateOf<DrumsProperty?>(null) }

    var selectedSet by remember { mutableStateOf(setOf<Int>()) }

    if (showAddPropertyDialog) {
        AddPropertyDialog(
            onDismiss = { showAddPropertyDialog = false },
            onAccept = { title ->
                onAddProperty(title)
                showAddPropertyDialog = false
            })
    }

    Row(modifier) {
        Box(Modifier.weight(1f)) {
            PropertyLazyList(
                columns = GridCells.Fixed(if (propertyShouldShow == null) 2 else 1),
                modifier = Modifier.fillMaxSize(),
                properties = properties,
                selectMode = selectedSet.isNotEmpty(),
                selectedSet = selectedSet,
                onSelectChange = { selectedSet = it },
                onDetailsClick = { propertyShouldShow = it },
                onPropertyClick = onPropertyClick
            )

            if (selectedSet.isEmpty()) AddFloatingActionButton(
                onClick = { onAddProperty("Untitled 1") },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) else RemoveFloatingActionButton(
                onClick = { selectedSet.forEach(onRemoveProperty); selectedSet = setOf() },
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
        if (propertyShouldShow != null) {
            DetailsPane(
                property = propertyShouldShow!!,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .animateContentSize(),
                onEditClick = {},
                onCloseClick = { propertyShouldShow = null },
            )
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(modifier) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}


@Composable
private fun PropertyLazyList(
    columns: GridCells,
    modifier: Modifier,
    properties: List<DrumsProperty>,
    selectMode: Boolean,
    selectedSet: Set<Int>,
    onSelectChange: (Set<Int>) -> Unit,
    onDetailsClick: (DrumsProperty) -> Unit,
    onPropertyClick: (id: Int) -> Unit,
) {
    LazyVerticalGrid(columns, modifier) {
        items(items = properties, { it.id }) {
            PropertyLazyListItem(
                modifier = Modifier
                    .padding(8.dp, 4.dp)
                    .height(64.dp)
                    .fillMaxWidth(),
                property = it,
                selected = it.id in selectedSet,
                onClick = {
                    if (selectMode) onSelectChange(selectedSet.flip(it.id))
                    else onPropertyClick(it.id)
                },
                onLongClick = { onSelectChange(selectedSet.flip(it.id)) },
                onDetailsClick = { onDetailsClick(it) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyGridItemScope.PropertyLazyListItem(
    modifier: Modifier,
    property: DrumsProperty,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    val primaryIfSelected by animateColorAsState(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
    val padding by animateDpAsState(if (selected) 1.dp else 0.dp)
    Box(modifier) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding, padding * 2)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            elevation = CardDefaults.elevatedCardElevation(),
            border = BorderStroke(4.dp, primaryIfSelected)
        ) {
            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = property.title)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDetailsClick) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
                }
            }
        }
    }
}

@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Property")
    }
}

@Composable
fun RemoveFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Property")
    }
}

@Composable
fun DetailsPane(
    property: DrumsProperty,
    modifier: Modifier,
    onEditClick: () -> Unit,
    onCloseClick: () -> Unit
) {

    Column(
        modifier
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onCloseClick) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Close")
            }
        }
        Text(
            text = property.title,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Created At ${property.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Updated At ${property.updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPropertyDialog(
    onDismiss: () -> Unit,
    onAccept: (title: String) -> Unit
) {
    val maxSize = 32

    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { if (text.length <= maxSize) onAccept(text) }) {
                Text("OK")
            }
        },
        title = { Text("Input New Title") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { if (text.length > maxSize) Text("length must be 32 or less") },
                isError = text.length > maxSize,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    )
}