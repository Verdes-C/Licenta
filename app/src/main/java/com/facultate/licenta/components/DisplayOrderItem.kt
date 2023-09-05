package com.facultate.licenta.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.facultate.licenta.R
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.OrderStatus
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun DisplayOrderItem(orderItem: Order, navController:NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor
            )
            .wrapContentHeight()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = Variables.verticalPadding),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Order number:", style = Typography.p)
            Text(text = orderItem.orderNumber.toString(), style = Typography.p)
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .border(width = 1.dp, color = Color(0x33172145))
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            modifier = Modifier
                .padding(vertical = 4.dp)
        ) {
            items(items = orderItem.products) { productBought ->
                //TODO MODIFY FOR IMAGE
                AsyncImage(
                    model = productBought.productImage,
                    contentDescription = productBought.productImageDescription,
                    modifier = Modifier
                        .requiredSize(size = 60.dp)
                        .clickable(

                        ) {
                            navController.navigate("product/${productBought.productCategory}/${productBought.productId}")
                        }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total: $${orderItem.totalPrice}",
                style = Typography.p,
                fontWeight = FontWeight.Bold
            )
            Row {
                when (orderItem.status) {
                    is OrderStatus.AwaitingPayment -> {
                        Text(text = "Awaiting payment", style = Typography.p)
                    }

                    is OrderStatus.Paid -> {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_card),
                            contentDescription = "Order paid"
                        )
                    }

                    is OrderStatus.Shipped -> {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_card),
                            contentDescription = "Order paid"
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.icon_delivery),
                            contentDescription = "Order shipped"
                        )
                    }

                    is OrderStatus.Delivered -> {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_shipping_box),
                            contentDescription = "Order delivered"
                        )
                    }

                    else -> {
                        //TODO
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (orderItem.status == OrderStatus.Shipped) {
                Buttons.SecondaryActive(Modifier, "Track your order") {
                    //TODO SEND TO TRACKING
                }
            } else {
                Buttons.SecondaryInactive(Modifier, "Track your order")
            }

            when (orderItem.status) {
                is OrderStatus.Paid -> {
                    Text(text = "Paid", style = Typography.p)
                }

                is OrderStatus.Shipped -> {
                    Text(text = "Shipped", style = Typography.p)
                }

                is OrderStatus.Delivered -> {
                    Text(text = "Delivered", style = Typography.p)
                }

                else -> {}
            }
        }

    }
}


