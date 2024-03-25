package com.srnyndrs.android.portalpeeker.presentation.character_list.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srnyndrs.android.portalpeeker.ui.theme.PortalPeekerTheme
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

@Composable
fun ErrorField(
    modifier: Modifier,
    error: Throwable,
    onRetry: () -> Unit
) {

    var retryAvailable = false
    val errorMessage = when(error) {
        is HttpException -> {
            when(error.code()) {
                404 -> "Character not found!"
                else -> error.message.toString()
            }
        }
        is UnknownHostException -> {
            retryAvailable = true
            "No internet connection!"
        }
        else -> error.message.toString()
    }

    Row(
        modifier = modifier.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            if(retryAvailable) {
                Button(onClick = onRetry) {
                    Text(text = "Try again")
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ErrorFieldPreview() {
    val errors = listOf<Throwable>(
        HttpException(
            Response.error<ResponseBody>(
                404,
                "content".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        ),
        UnknownHostException()
    )
    PortalPeekerTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for(error in errors) {
                    ErrorField(
                        modifier = Modifier.fillMaxWidth(),
                        error = error,
                        onRetry = {}
                    )
                    Divider(thickness = 3.dp)
                }
            }
        }
    }
}