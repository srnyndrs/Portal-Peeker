package com.srnyndrs.android.portalpeeker.presentation.character_list.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.portalpeeker.domain.model.Character
import com.srnyndrs.android.portalpeeker.presentation.util.pxToDp
import com.srnyndrs.android.portalpeeker.ui.theme.PortalPeekerTheme

@Composable
fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.requiredHeight(400.pxToDp()),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CharacterImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .requiredWidth(400.pxToDp())
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                imageUrl = character.imageUrl,
                name = character.name
            )
            Column(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, end = 6.dp, start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(0.8f),
                        text = character.name,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    CustomLabel(
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        backgroundColor = Color.Gray,
                        borderColor = Color.Gray,
                        icon = {},
                        value = "#${character.id}"
                    )
                }
                val statusColor = when(character.status.lowercase()) {
                    "alive" -> Color(red = 33, green = 184, blue = 13)
                    "dead" -> Color(red = 170, green = 21, blue = 21, alpha = 255)
                    "unknown" -> Color(red = 204, green = 201, blue = 14, alpha = 255)
                    else -> Color.Gray
                }
                CustomLabel(
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = statusColor,
                    borderColor = statusColor,
                    icon = {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary)
                        )
                    },
                    value = character.status.uppercase()
                )
                CustomLabel(
                    textColor = Color.Gray,
                    backgroundColor = Color.Transparent,
                    borderColor = Color.Gray,
                    icon = {
                        Icon(
                            modifier = Modifier.size(14.dp),
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location Icon",
                            tint = Color.Gray
                        )
                    },
                    value = character.location.uppercase()
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterItemPreview() {
    PortalPeekerTheme {
        CharacterCard(
            character = Character(
                id = 123,
                name = "Rick Sanchez",
                imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                status = "Alive",
                location = "Earth"
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}