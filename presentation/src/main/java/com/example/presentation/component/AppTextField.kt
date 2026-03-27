package com.example.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.theme.ThousandsCoursesTheme

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String? = null,
    leadingIcon: Painter? = null,
    height: Dp,
    paddingStart: Dp,
    color: Color,
    radius: Dp
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (title != null) {
            Text(
                modifier = Modifier,
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = color,
                    shape = RoundedCornerShape(radius)
                ),
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .height(height)
                        .padding(start = paddingStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            painter = leadingIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.01.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholderText != null) {
                            Text(
                                text = placeholderText,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewAppTextField() {
    ThousandsCoursesTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            placeholderText = "example@example.com",
            leadingIcon = painterResource(com.example.presentation.R.drawable.ic_search),
            height = 56.dp,
            paddingStart = 16.dp,
            color = MaterialTheme.colorScheme.surface,
            radius = 28.dp
        )
    }
}
