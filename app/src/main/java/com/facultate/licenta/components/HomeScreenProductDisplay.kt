package com.facultate.licenta.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables


@Composable
fun HomeScreenProductDisplay(
    productImageDescription: String = "Product Image",
    productName: String = "Ink Pen Second In Line",
    productPrice: Double = 123.24,
    productDiscountedPrice: Double = 103.24,
    isSale: Boolean = false,
    navController : NavHostController,
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.grey6,
                ambientColor = Variables.grey6,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .border(
                width = 1.dp,
                color = Variables.blue3,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .width(224.dp)
            .height(152.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .padding(all = Variables.innerItemGap)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                //TODO update
                navController.navigate(Screens.Product.route+productName)
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .width(84.dp)
                    .height(128.dp)
                    .background(
                        color = Variables.grey1,
                        shape = RoundedCornerShape(size = Variables.cornerRadius)
                    ),
                painter = painterResource(id = R.drawable.image_placeholder),
                contentDescription = productImageDescription,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .width(108.dp)
                        .height(36.dp),
                    text = productName,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    style = Typography.buttonBold,
                    color = Variables.blue3
                )
                Text(
                    text = "Category",
                    style = Typography.caption,
                    color = Variables.grey3
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isSale) {
                        Text(
                            text = "$${productDiscountedPrice}",
                            style = Typography.buttonBold,
                            color = Variables.red,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${productPrice}",
                            style = Typography.p.copy(
                                textDecoration = TextDecoration.LineThrough,
                            ),
                            color = Variables.grey3
                        )
                    } else {
                        Text(
                            text = "$${productPrice}",
                            style = Typography.buttonBold,
                            color = Variables.grey6,
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xff172145
                        )
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    modifier = Modifier
                        .requiredWidth(width = 107.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .requiredWidth(width = 107.dp)
                    ) {
                        Text(
                            text = "Add to cart",
                            color = Color(0xffebecef),
                            textAlign = TextAlign.Center,
                            lineHeight = 9.29.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}