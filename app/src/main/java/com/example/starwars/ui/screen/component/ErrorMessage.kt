package com.example.starwars.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    showRetryButton: Boolean = true,
    onRetry: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("ErrorMessageTag"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Error: $message",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onErrorContainer),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (showRetryButton) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Retry")
                }
            }
        }
    }
}
