package com.facultate.licenta.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import javax.inject.Inject

object Buttons {
    @Composable
    fun PrimaryActive(modifier: Modifier = Modifier, text: String = "Text", onClick: () -> Unit) {
        Button(
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = Variables.grey6),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = modifier.shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            ),
            enabled = true,
            onClick = { onClick.invoke() }
        ) {
            Text(
                text = text,
                color = Variables.white,
                textAlign = TextAlign.Center,
                style = Typography.buttonBold
            )
        }
    }

    @Composable
    fun PrimaryInactive(modifier: Modifier = Modifier, text: String = "Text") {
        Button(
            enabled = false,
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(disabledContainerColor = Variables.grey4),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = modifier.shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            ),
            onClick = {  }
        ) {
            Text(
                text = text,
                color = Variables.white,
                textAlign = TextAlign.Center,
                style = Typography.buttonBold
            )
        }
    }

    @Composable
    fun SecondaryActive(modifier: Modifier = Modifier, text: String = "Text", onClick: () -> Unit) {
        OutlinedButton(
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            border = BorderStroke(2.dp, Variables.grey6),
            modifier = modifier.shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            ),
            enabled = true,
            onClick = { onClick.invoke() }
        ) {
            Text(
                text = text,
                color = Variables.grey6,
                textAlign = TextAlign.Center,
                style = Typography.buttonBold
            )
        }
    }

    @Composable
    fun SecondaryInactive(
        modifier: Modifier = Modifier,
        text: String = "Text",
    ) {
        OutlinedButton(
            enabled = false,
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            border = BorderStroke(2.dp, Variables.grey3),
            modifier = modifier,
            onClick = {  }
        ) {
            Text(
                text = text,
                color = Variables.grey3,
                textAlign = TextAlign.Center,
                style = Typography.buttonBold
            )
        }
    }

    @Composable
    fun Tertiary(modifier: Modifier = Modifier, text: String = "Text", onClick: () -> Unit) {
        TextButton(
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
            modifier = modifier,
            onClick = { }
        ) {
            Text(
                text = text,
                color = Variables.grey6,
                textAlign = TextAlign.Center,
                style = Typography.buttonBold
            )
        }
    }


}

@Preview(widthDp = 169, heightDp = 42, showBackground = true)
@Composable
private fun ScopeTertiaryStatusActivePreview() {
    Buttons.Tertiary(Modifier, "Forgot Password?") {}
}

@Preview(widthDp = 120, heightDp = 42, showBackground = true)
@Composable
private fun ScopePrimaryStatusActivePreview() {
    Buttons.PrimaryActive(Modifier) {}
}

@Preview(widthDp = 120, heightDp = 42, showBackground = true)
@Composable
private fun ScopePrimaryStatusInactivePreview() {
    Buttons.PrimaryInactive(Modifier)
}

@Preview(widthDp = 120, heightDp = 42, showBackground = true)
@Composable
private fun ScopeOutlinedStatusActivePreview() {
    Buttons.SecondaryActive(Modifier) {}
}


@Preview(widthDp = 120, heightDp = 42, showBackground = true)
@Composable
private fun ScopeOutlinedStatusInactivePreview() {
    Buttons.SecondaryInactive(Modifier)
}