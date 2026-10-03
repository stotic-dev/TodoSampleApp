package com.example.todosampleapp.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todosampleapp.designsystem.theme.surfaceBrightLightMediumContrast

@Composable
fun SectionCard(content: @Composable () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false,
                ).background(
                    color = surfaceBrightLightMediumContrast,
                    shape = RoundedCornerShape(8.dp),
                ).padding(
                    vertical = 24.dp,
                    horizontal = 16.dp,
                ),
    ) {
        content()
    }
}

@Preview(showBackground = true, name = "SectionCard")
@Composable
fun SectionCardPreview() {
    SectionCard {
        Text("テスト")
    }
}
