package com.srnyndrs.android.portalpeeker.presentation.character_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.srnyndrs.android.portalpeeker.presentation.util.pxToDp
import com.srnyndrs.android.portalpeeker.ui.theme.PortalPeekerTheme

@Composable
fun CharacterImage(
    modifier: Modifier,
    imageUrl: String,
    name: String
) {

    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .build()
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(painter.state) {
            is AsyncImagePainter.State.Error -> {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Portrait icon"
                )
            }
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(6.dp)
                )
            }
            else -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentDescription = "$name's portrait",
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Preview
@Composable
fun CharacterImagePreview() {
    PortalPeekerTheme {
        Surface {
            CharacterImage(
                modifier = Modifier
                    .requiredWidth(400.pxToDp())
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        )
                    ),
                imageUrl = "",
                name = "Rick Sanchez"
            )
        }
    }
}