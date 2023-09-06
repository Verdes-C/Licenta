package com.facultate.licenta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.facultate.licenta.R
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.OrderStatus
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun DisplayAdminOrder(
    orderItem: Order,
    navController: NavHostController,
    indexOutOf: String,
    onStatusChanged: (order: Order) -> Unit
) {
    var orderStatus by remember {
        mutableStateOf(orderItem.status)
    }
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
            .padding(
                start = Variables.innerItemGapLow,
                top = Variables.innerItemGapLow,
                end = Variables.innerItemGapLow,
                bottom = Variables.verticalPadding
            ),
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .heightIn(
                    min = (orderItem.products.size * 60 + 30).dp,
                    max = (orderItem.products.size * 120 + 30).dp
                )
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Image", style = Typography.p)
                    Text(text = "Product", style = Typography.p)
                    Text(text = "Quantity", style = Typography.p)
                }
            }

            items(items = orderItem.products) { productBought ->
                Divider(modifier = Modifier.height(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = Variables.innerItemGapLow)
                    ) {
                        Text(text = "${productBought.productId}", style = Typography.p)
                        Divider(modifier = Modifier.height(1.dp))
                        Text(text = "${productBought.productName}", style = Typography.p)
                    }
                    Text(
                        text = "${productBought.productQuantity} ", style = Typography.pBig.copy(
                            color = Variables.orange3
                        ), modifier = Modifier.weight(0.3f)
                    )
                }
            }
            item {
                Text(
                    text = "Shipping address: ${orderItem.fullAddress}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
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
            SelectValueScreen(selectedValue = orderStatus.toString(), onValueSelected = {
                when (it) {
                    "Awaiting Payment" -> orderStatus = OrderStatus.AwaitingPayment
                    "Paid" -> orderStatus = OrderStatus.Paid
                    "Shipped" -> orderStatus = OrderStatus.Shipped
                    "Delivered" -> orderStatus = OrderStatus.Delivered
                }
                onStatusChanged(orderItem.copy(
                    status = orderStatus
                ))
            })
            Text(text = "${indexOutOf}", style = Typography.p)
        }
    }
}


@Composable
fun SelectValueScreen(selectedValue: String, onValueSelected: (String) -> Unit) {
    val values = listOf("Awaiting Payment", "Paid", "Shipped", "Delivered")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
    ) {
        Text(
            text = selectedValue,
            modifier = Modifier
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach { value ->
                DropdownMenuItem(
                    onClick = {
                        onValueSelected(value)
                        expanded = false
                    },
                    text = { Text(text = value) }
                )
            }
        }
    }
}
