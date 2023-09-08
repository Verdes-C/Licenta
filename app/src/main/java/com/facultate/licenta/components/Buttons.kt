package com.facultate.licenta.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.facultate.licenta.R
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

object Buttons {
    @Composable
    fun PrimaryActive(modifier: Modifier = Modifier, text: String = "Text", onClick: () -> Unit) {
        Button(
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = Variables.grey6),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = modifier.shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            ),
            onClick = { }
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
    fun SecondaryActive(
        modifier: Modifier = Modifier,
        text: String = "Text",
        textStyle: TextStyle = Typography.buttonBold,
        onClick: () -> Unit,
    ) {
        OutlinedButton(
            onClick = { onClick.invoke() },
            shape = RoundedCornerShape(Variables.innerItemGap),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            border = BorderStroke(1.dp, Variables.grey6),
            modifier = modifier
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = textStyle
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            border = BorderStroke(2.dp, Variables.grey3),
            modifier = modifier,
            onClick = { }
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
    fun Tertiary(
        modifier: Modifier = Modifier,
        text: String = "Text",
        textStyle: TextStyle = Typography.buttonBold,
        onClick: () -> Unit,
    ) {
        TextButton(
            shape = RoundedCornerShape(Variables.cornerRadius),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            modifier = modifier,
            onClick = { onClick.invoke() }
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = textStyle
            )
        }
    }

    @Composable
    fun Checkbox(
        modifier: Modifier = Modifier,
        isChecked: Boolean,
        checkedDescription: String = "Conditions accepted",
        uncheckedDescription: String = "Conditions not accepted",
        tintColor: Color = Variables.blue3,
        onClick: () -> Unit,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = if (isChecked) R.drawable.icon_checked else R.drawable.icon_uncheked),
            contentDescription = if (isChecked) checkedDescription else uncheckedDescription,
            tint = tintColor,
            modifier = modifier
                .size(32.dp)
                .clickable {
                    onClick.invoke()
                },
        )
    }

    @Composable
    fun SocialLogin(
        modifier: Modifier = Modifier,
        textStyle: TextStyle = Typography.buttonBold,
        socialPlatform: SocialLoginPlatforms,
        onClick: () -> Unit,
    ) {

        OutlinedButton(
            onClick = { onClick.invoke() },
            shape = RoundedCornerShape(Variables.innerItemGap),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = Variables.innerItemGap, vertical = 2.dp),
            border = BorderStroke(1.dp, Variables.grey6),
            modifier = modifier
        ) {
            Text(
                text = socialPlatform.buttonText,
                textAlign = TextAlign.Center,
                style = textStyle
            )
            Icon(
                modifier = Modifier.padding(start = Variables.innerItemGap).size(32.dp),
                imageVector = ImageVector.vectorResource(id = socialPlatform.platformIconId),
                contentDescription = socialPlatform.buttonText,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(widthDp = 190, showBackground = true)
@Composable
private fun LoginGooglePreview() {
    Buttons.SocialLogin(socialPlatform = SocialLoginPlatforms.Google) {}
}

@Preview(widthDp = 190, showBackground = true)
@Composable
private fun LoginFacebookPreview() {
    Buttons.SocialLogin(socialPlatform = SocialLoginPlatforms.Facebook) {}
}

@Preview(widthDp = 169, showBackground = true)
@Composable
private fun ScopeTertiaryStatusActivePreview() {
    Buttons.Tertiary(Modifier, "Forgot Password?") {}
}

@Preview(widthDp = 120, showBackground = true, backgroundColor = 0xFFEBECEF)
@Composable
private fun ScopePrimaryStatusActivePreview() {
    Buttons.PrimaryActive(Modifier) {}
}

@Preview(widthDp = 120, showBackground = true)
@Composable
private fun ScopePrimaryStatusInactivePreview() {
    Buttons.PrimaryInactive(Modifier)
}

@Preview(widthDp = 120, showBackground = true)
@Composable
private fun ScopeOutlinedStatusActivePreview() {
    Buttons.SecondaryActive(Modifier) {}
}


@Preview(widthDp = 120, showBackground = true)
@Composable
private fun ScopeOutlinedStatusInactivePreview() {
    Buttons.SecondaryInactive(Modifier)
}

@Composable
fun ActionsButtonsOrder(modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
        border = BorderStroke(1.dp, Color(0xff172145)),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Track your order",
                color = Color(0xff172145),
                textAlign = TextAlign.Center,
                lineHeight = 11.43.em,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionsButtonsOrderPreview() {
    ActionsButtonsOrder(Modifier)
}

sealed class SocialLoginPlatforms(
    val buttonText: String,
    val platformIconId: Int,
){
    object Google: SocialLoginPlatforms(
        buttonText = "Google", platformIconId = R.drawable.icon_google
    )

    object Facebook: SocialLoginPlatforms(
        buttonText = "Facebook", platformIconId = R.drawable.icon_facebook
    )
}
