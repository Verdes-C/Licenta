package com.facultate.licenta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.facultate.licenta.R
import com.facultate.licenta.screens.profile.UserData
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun DisplayCardPaymentDetails(modifier: Modifier = Modifier, userData: UserData) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(height = 180.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = Variables.grey2)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 0.dp,
                    y = 20.dp
                )
                .fillMaxWidth()
                .background(color = Variables.grey6)
        ) {
            Text(
                text = "Payment Details",
                color = Color.White,
                style = Typography.p,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .padding(vertical = 4.dp)
                    .padding(start = 4.dp)
            )
        }

        // Name on card
        CustomTextField(
            modifier = Modifier
                .width(200.dp)
                .offset(
                    x = 0.dp,
                    y = 48.dp
                ),
            initialValue = "",
            label = "Name on card",
            placeholder = "Ion Ionescu",
            textColor = Variables.grey6,
            backgroundColor = Color.Transparent,
            hideIndicators = true,
            onValueChange = {
                //TODO
            })

        // Icons
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.TopEnd)
                .offset(
                    x = (-10).dp,
                    y = 60.dp
                )
                .requiredHeight(height = 124.dp)
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_visa),
                contentDescription = "visa",
                tint = Color.Unspecified,
                modifier = Modifier
                    .requiredWidth(width = 61.dp)
                    .requiredHeight(height = 20.dp)
                    .padding(end = 12.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_master),
                contentDescription = "mastercard",
                tint = Color.Unspecified,
                modifier = Modifier
                    .requiredWidth(width = 30.dp)
                    .requiredHeight(height = 23.dp)
            )
        }

        // Card number
        CustomTextField(
            modifier = Modifier
                .width(160.dp)
                .offset(
                    x = 0.dp,
                    y = 108.dp
                ),
            initialValue = "",
            label = "Card number",
            placeholder = if (userData.lastName.isNotEmpty()) "xxxx xxxx xxxx ${userData.lastName.first()}" else "xxxx xxxx xxxx xxxx",
            textColor = Variables.grey6,
            backgroundColor = Color.Transparent,
            hideIndicators = true,
            onValueChange = {
                //TODO
            })

        // Expiration date
        CustomTextField(
            modifier = Modifier
                .width(120.dp)
                .offset(
                    x = 160.dp,
                    y = 108.dp
                ),
            initialValue = "",
            label = "Expiration date",
            placeholder = "01 / 29",
            textColor = Variables.grey6,
            backgroundColor = Color.Transparent,
            hideIndicators = true,
            onValueChange = {
                //TODO
            })

        // Csv
        CustomTextField(
            modifier = Modifier
                .width(60.dp)
                .offset(
                    x = 280.dp,
                    y = 108.dp
                ),
            initialValue = "",
            label = "Csv",
            placeholder = "123",
            textColor = Variables.grey6,
            backgroundColor = Color.Transparent,
            hideIndicators = true,
            onValueChange = {
                //TODO
            })
    }
}