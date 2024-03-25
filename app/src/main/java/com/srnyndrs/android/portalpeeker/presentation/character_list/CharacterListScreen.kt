package com.srnyndrs.android.portalpeeker.presentation.character_list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.srnyndrs.android.portalpeeker.domain.model.Character
import com.srnyndrs.android.portalpeeker.presentation.character_list.components.CharacterCard
import com.srnyndrs.android.portalpeeker.presentation.character_list.components.ErrorField
import com.srnyndrs.android.portalpeeker.ui.theme.PortalPeekerTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun CharacterListScreen(
    characters: LazyPagingItems<Character>,
    onSearch: (String) -> Unit
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    val searchQuery = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp, top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                )
            },
            trailingIcon = {
                if(searchQuery.value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchQuery.value = ""
                            onSearch(searchQuery.value)
                            coroutineScope.launch {
                                listState.animateScrollToItem(index = 0)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear icon"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                onSearch(it)
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.None
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focusManager.clearFocus() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(characters.loadState.refresh) {
                is LoadState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoadState.Error -> {
                    val errorState = characters.loadState.refresh as LoadState.Error
                    ErrorField(
                        modifier = Modifier.fillMaxWidth(),
                        error = errorState.error,
                        onRetry = {
                            onSearch(searchQuery.value)
                        }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = listState
                    ) {
                        items(characters.itemCount) { index ->
                            val character = characters[index]
                            if(character != null) {
                                CharacterCard(
                                    character = character,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                        if(characters.loadState.append is LoadState.Loading) {
                            item {
                                CircularProgressIndicator()
                            }
                        } else if(characters.loadState.append is LoadState.Error) {
                            item {
                                Text(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    text = "Failed to load more results",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CharacterListScreenPreview() {
    PortalPeekerTheme {
        Surface {
            CharacterListScreen(
                characters = flowOf(
                    PagingData.from(
                        data = listOf(
                            Character(
                                id = 1,
                                name = "Rick Sanchez",
                                status = "Alive",
                                imageUrl = "",
                                location = "Earth"
                            ),
                            Character(
                                id = 2,
                                name = "Morty Smith",
                                status = "Dead",
                                imageUrl = "",
                                location = "Earth"
                            ),
                            Character(
                                id = 3,
                                name = "Squanchy",
                                status = "unknown",
                                imageUrl = "",
                                location = "Earth"
                            )
                        ),
                        sourceLoadStates = LoadStates(
                            refresh = LoadState.NotLoading(false),
                            append = LoadState.NotLoading(false),
                            prepend = LoadState.NotLoading(false),
                        )
                    )
                ).collectAsLazyPagingItems(),
                onSearch = {}
            )
        }
    }
}