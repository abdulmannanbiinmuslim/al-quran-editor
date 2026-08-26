package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RightNavigationDrawerContent(
    onItemClick: (String) -> Unit,
    onSocialClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxHeight()
            .width(340.dp)
    ) {
        FloatingModalNavigationMenuContent(
            onDismiss = {},
            onItemClick = onItemClick,
            onSocialClick = onSocialClick
        )
    }
}
