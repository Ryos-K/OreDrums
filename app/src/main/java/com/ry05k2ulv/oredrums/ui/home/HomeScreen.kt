package com.ry05k2ulv.oredrums.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ry05k2ulv.oredrums.model.DrumsProperty
import com.ry05k2ulv.oredrums.ui.theme.OreDrumsTheme
import com.ry05k2ulv.oredrums.utils.flip

@Composable
internal fun HomeRoute(
    onPropertyClick: (id: Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        onPropertyClick = onPropertyClick,
        onAddProperty = viewModel::insertProperty
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onPropertyClick: (id: Int) -> Unit,
    onAddProperty: (title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        when (uiState) {
            HomeUiState.Loading -> LoadingScreen()
            is HomeUiState.Success -> SuccessScreen(
                modifier = Modifier.fillMaxSize(),
                properties = uiState.properties,
                onPropertyClick = onPropertyClick,
                onAddProperty = onAddProperty
            )
        }
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier,
    properties: List<DrumsProperty>,
    onPropertyClick: (id: Int) -> Unit,
    onAddProperty: (title: String) -> Unit
) {
    var showAddPropertyDialog by remember { mutableStateOf(false) }

    var selectedSet by remember { mutableStateOf(setOf<Int>()) }

    if (showAddPropertyDialog) {
        AddPropertyDialog(
            onDismiss = { showAddPropertyDialog = false },
            onAccept = { title ->
                onAddProperty(title)
                showAddPropertyDialog = false
            })
    }

    Box(modifier) {
        PropertyLazyList(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            selectMode = selectedSet.isNotEmpty(),
            selectedSet = selectedSet,
            onSelectChange = { selectedSet = it },
            onPropertyClick = onPropertyClick
        )

        FloatingActionButton(
            onClick = { showAddPropertyDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add property")
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
    modifier: Modifier,
    properties: List<DrumsProperty>,
    selectMode: Boolean,
    selectedSet: Set<Int>,
    onSelectChange: (Set<Int>) -> Unit,
    onPropertyClick: (id: Int) -> Unit
) {
    LazyColumn(modifier) {
        items(properties, { it.id }) {
            PropertyLazyListItem(
                modifier = Modifier
                    .padding(8.dp, 4.dp)
                    .fillMaxWidth(),
                property = it,
                onClick = {
                    if (selectMode) onSelectChange(selectedSet.flip(it.id))
                    else onPropertyClick(it.id)
                },
                onLongClick = { onSelectChange(selectedSet.flip(it.id)) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PropertyLazyListItem(
    modifier: Modifier,
    property: DrumsProperty,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Text(text = property.title)
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