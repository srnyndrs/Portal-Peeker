package com.srnyndrs.android.portalpeeker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.srnyndrs.android.portalpeeker.presentation.character_list.CharacterListScreen
import com.srnyndrs.android.portalpeeker.presentation.character_list.CharacterViewModel
import com.srnyndrs.android.portalpeeker.ui.theme.PortalPeekerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortalPeekerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<CharacterViewModel>()
                    val characters by rememberUpdatedState(newValue = viewModel.uiState.collectAsLazyPagingItems())
                    CharacterListScreen(
                        characters = characters,
                        onSearch = viewModel::onSearch,
                    )
                }
            }
        }
    }
}