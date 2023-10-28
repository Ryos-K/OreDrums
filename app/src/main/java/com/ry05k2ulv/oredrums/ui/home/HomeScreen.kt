package com.ry05k2ulv.oredrums.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ry05k2ulv.oredrums.model.DrumsProperty
import com.ry05k2ulv.oredrums.ui.components.AddFloatingActionButton
import com.ry05k2ulv.oredrums.ui.components.ArrowBackIconButton
import com.ry05k2ulv.oredrums.ui.components.EditToggleIconButton
import com.ry05k2ulv.oredrums.ui.components.OpenTextButton
import com.ry05k2ulv.oredrums.ui.components.RemoveFloatingActionButton
import com.ry05k2ulv.oredrums.ui.components.SettingsIconButton
import com.ry05k2ulv.oredrums.utils.flip
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun HomeRoute(
    onOpenClick: (id: Int) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        onSettingsClick = onSettingsClick,
        onOpenClick = onOpenClick,
        onAddProperty = viewModel::insertProperty,
        onRemoveProperty = viewModel::deletePropertyById,
        onEditProperty = viewModel::updateProperty
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onSettingsClick: () -> Unit,
    onOpenClick: (id: Int) -> Unit,
    onAddProperty: (title: String) -> Unit,
    onRemoveProperty: (id: Int) -> Unit,
    onEditProperty: (DrumsProperty) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        HomeTopBar(onSettingsClick = onSettingsClick, modifier = Modifier)
        when (uiState) {
            HomeUiState.Loading -> LoadingScreen()
            is HomeUiState.Success -> SuccessScreen(
                modifier = Modifier
                    .fillMaxSize(),
                properties = uiState.properties,
                onOpenClick = onOpenClick,
                onAddProperty = onAddProperty,
                onRemoveProperty = onRemoveProperty,
                onEditProperty = onEditProperty
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier,
) {
    TopAppBar(
        title = {
            Spacer(modifier = Modifier.width(32.dp))
            Text(text = "Ore Drums", style = MaterialTheme.typography.headlineLarge)
        },
        actions = {
            SettingsIconButton(onClick = onSettingsClick)
        },
        modifier = modifier,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun SuccessScreen(
    modifier: Modifier,
    properties: List<DrumsProperty>,
    onOpenClick: (id: Int) -> Unit,
    onAddProperty: (title: String) -> Unit,
    onRemoveProperty: (id: Int) -> Unit,
    onEditProperty: (DrumsProperty) -> Unit
) {
    var propertyIdShouldShow by remember { mutableStateOf<Int?>(null) }

    var selectedSet by remember { mutableStateOf(setOf<Int>()) }

    Row(modifier) {
        AnimatedVisibility(visible = propertyIdShouldShow != null) {
            DetailsPane(
                property = properties.find { it.id == propertyIdShouldShow },
                onPropertyChange = onEditProperty,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),
                onOpenClick = onOpenClick,
                onCloseClick = { propertyIdShouldShow = null }
            )
        }

        Box(Modifier.weight(1f)) {
            PropertyLazyList(
                modifier = Modifier.fillMaxSize(),
                properties = properties,
                selectMode = selectedSet.isNotEmpty(),
                selectedSet = selectedSet,
                onSelectChange = { selectedSet = it },
                onPropertyClick = { propertyIdShouldShow = it.id }
            )

            if (selectedSet.isEmpty()) AddFloatingActionButton(
                onClick = { onAddProperty("Untitled 1") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) else RemoveFloatingActionButton(
                onClick = {
                    selectedSet.forEach(onRemoveProperty)
                    propertyIdShouldShow?.let { if (it in selectedSet) propertyIdShouldShow = null }
                    selectedSet = setOf()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            )
        }
    }
}

@Composable
private fun PropertyLazyList(
    modifier: Modifier,
    properties: List<DrumsProperty>,
    selectMode: Boolean,
    selectedSet: Set<Int>,
    onSelectChange: (Set<Int>) -> Unit,
    onPropertyClick: (DrumsProperty) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(properties, { it.id }) {
            PropertyLazyListItem(
                modifier = Modifier
                    .padding(8.dp, 4.dp)
                    .height(64.dp)
                    .fillMaxWidth(),
                property = it,
                selected = it.id in selectedSet,
                onClick = {
                    if (selectMode) onSelectChange(selectedSet.flip(it.id))
                    else onPropertyClick(it)
                },
                onLongClick = { onSelectChange(selectedSet.flip(it.id)) },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.PropertyLazyListItem(
    modifier: Modifier,
    property: DrumsProperty,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
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
            }
        }
    }
}


@Composable
fun DetailsPane(
    property: DrumsProperty?,
    onPropertyChange: (DrumsProperty) -> Unit,
    modifier: Modifier,
    onOpenClick: (id: Int) -> Unit,
    onCloseClick: () -> Unit
) {
    var editMode: Boolean by remember { mutableStateOf(false) }

    property?.let {
        Column(modifier) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ArrowBackIconButton(onClick = onCloseClick)
                EditToggleIconButton(
                    editMode = editMode,
                    onEditClick = { editMode = true },
                    onEditOffClick = { editMode = false })
            }
            DetailsPaneTitleSection(
                property.title,
                onTitleChange = {
                    onPropertyChange(
                        property.copy(title = it)
                    )
                },
                editMode = editMode
            )
            DetailsPaneDateTimeSection("Created At", property.createdAt)
            DetailsPaneDateTimeSection("Updated At", property.updatedAt)

            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp), horizontalArrangement = Arrangement.End
            ) {
                OpenTextButton(onClick = { onOpenClick(property.id) })
            }
        }
    } ?: Box(modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsPaneTitleSection(
    title: String,
    onTitleChange: (String) -> Unit = {},
    editMode: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    Row(Modifier.height(56.dp), verticalAlignment = Alignment.CenterVertically) {
        if (editMode) {
            BasicTextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                modifier = Modifier.padding(16.dp, 0.dp),
                textStyle = MaterialTheme.typography.headlineMedium,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            ) {
                TextFieldDefaults.TextFieldDecorationBox(
                    value = title,
                    innerTextField = it,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = PaddingValues(8.dp, 2.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colorScheme.onBackground)
                )
            }
        } else {
            Text(
                text = title,
                modifier = Modifier.padding(24.dp, 2.dp),
                style = MaterialTheme.typography.headlineMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun DetailsPaneDateTimeSection(
    text: String,
    dateTime: LocalDateTime
) {
    Row(Modifier.padding(32.dp, 8.dp)) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun DetailsPaneTextSection(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(modifier) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}