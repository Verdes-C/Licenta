package com.facultate.licenta.screens.product

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.DisplayRating
import com.facultate.licenta.components.DisplayReview
import com.facultate.licenta.components.HomeScreenProductDisplay
import com.facultate.licenta.components.MenuEntry
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProductPage(
    navController: NavHostController, productId: String,
    viewModel: ProductPageViewModel = hiltViewModel(),
) {
    val product = Product()

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val isFavorite = remember {
        mutableStateOf(false)
    }

    var descriptionMaxLines by remember {
        mutableIntStateOf(3)
    }

    var quantity by remember {
        mutableIntStateOf(1)
    }

    var specificationHeightHidden by remember {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Variables.grey1)
                .padding(horizontal = Variables.outerItemGap),
            verticalArrangement = Arrangement.spacedBy(Variables.outerItemGap),
            horizontalAlignment = Alignment.Start,
            state = scrollState
        ) {
            item {
                TopBar(
                    modifier = Modifier.padding(top = Variables.outerItemGap),
                    menuEntry = MenuEntry(
                        name = product.name,
                        iconDescription = product.name,
                    ),
                    displayArrow = true,
                    navController = navController
                )
            }

            //? Product galery
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 252.dp)
                        .clip(shape = RoundedCornerShape(Variables.cornerRadius))
                        .shadow(
                            elevation = Variables.elevation,
                            spotColor = Variables.shadowColor,
                            ambientColor = Variables.shadowColor,
                            shape = RoundedCornerShape(size = Variables.cornerRadius)
                        )
                        .background(color = Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyRow(
                        modifier = Modifier.padding(all = Variables.innerItemGap),
                        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
                    ) {
                        //TODO modify
                        repeat(5) {
                            item {
                                Image(
                                    painter = painterResource(id = R.drawable.image_placeholder),
                                    contentDescription = "image 1",
                                    modifier = Modifier
                                        .requiredWidth(width = 256.dp)
                                        .requiredHeight(height = 192.dp)
                                        .clip(shape = RoundedCornerShape(Variables.cornerRadius))
                                        .background(color = Variables.grey1)
                                )
                            }
                        }
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.icon_carousel),
                        contentDescription = "ic:round-view-carousel",
                        tint = Variables.grey6,
                        modifier = Modifier
                    )
                }
            }

            //? Product basic details
            item {

                //? Name and favorites toggle
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
                ) {
                    Row(
                        modifier = Modifier,
                    ) {
                        Text(
                            text = product.name,
                            style = Typography.h3,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(painter = painterResource(id = if (isFavorite.value) R.drawable.icon_favorites else R.drawable.icon_not_favorite),
                            contentDescription = if (isFavorite.value) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite.value) Color.Red else Variables.grey6,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isFavorite.value = !isFavorite.value
                                })
                    }

                    //? category and rating
                    Row {
                        Text(
                            text = product.category,
                            style = Typography.caption.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        DisplayRating(rating = product.rating, iconSize = 20.dp)
                    }

                    //? description
                    Text(text = product.description,
                        style = Typography.p.copy(color = Variables.grey3),
                        maxLines = descriptionMaxLines,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(), indication = null
                        ) {
                            when (descriptionMaxLines) {
                                3 -> descriptionMaxLines = 5
                                5 -> descriptionMaxLines = 3
                            }
                        })

                    //? quantity and add to cart
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterStart),
                            horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(painter = painterResource(id = R.drawable.icon_minus),
                                contentDescription = "Quantity to buy - 1",
                                tint = Variables.grey6,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { if (quantity > 1) quantity -= 1 })
                            Text(
                                text = "$quantity",
                                style = Typography.h4.copy(fontSize = 24.sp),
                                modifier = Modifier.width(32.dp),
                                textAlign = TextAlign.Center
                            )
                            Icon(painter = painterResource(id = R.drawable.icon_plus),
                                contentDescription = "Quantity to buy - 1",
                                tint = Variables.grey6,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { quantity += 1 })
                        }
                        Buttons.PrimaryActive(
                            text = "Add to cart", modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .align(
                                    Alignment.CenterEnd
                                )
                        ) {
                            // TODO add to cart
                        }
                    }
                }
            }

            //? Specifications
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Variables.innerItemGap),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Specifications",
                        style = Typography.h4,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Show more",
                        style = Typography.caption,
                        modifier = Modifier.clickable {
                            specificationHeightHidden = !specificationHeightHidden
                        })
                }
                LazyColumn(
                    userScrollEnabled = false,
                    modifier = Modifier
                        .height(if (specificationHeightHidden) 140.dp else ((product.specifications.size * 30)).dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(Variables.cornerRadius)
                        )
                        .padding(all = Variables.innerItemGapLow)
                        .clickable {
                            specificationHeightHidden = !specificationHeightHidden
                        },
                    verticalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow)
                ) {
                    items(items = product.specifications) { specification ->
                        Row {
                            Text(
                                text = specification.first,
                                style = Typography.p,
                                modifier = Modifier.weight(1.5f)
                            )
                            Text(
                                text = specification.second,
                                style = Typography.p,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Divider(
                            thickness = 1.dp,
                        )
                    }
                }
            }

            //? Recommendations
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Variables.innerItemGap),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recommendations",
                        style = Typography.h4,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Show more",
                        style = Typography.caption,
                        modifier = Modifier.clickable {
                            //TODO
                        })
                }
                LazyRow(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
                ) {
                    //TODO fix
                    repeat(7) {
                        item {
                            HomeScreenProductDisplay(
                                isSale = false,
                                navController = navController
                            )
                        }
                    }

                }
            }

            //? Reviews
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Variables.innerItemGap),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reviews",
                        style = Typography.h4,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Show more",
                        style = Typography.caption,
                        modifier = Modifier.clickable {
                            //TODO
                        })
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow),
                    modifier = Modifier.heightIn(
                        min = (product.reviews.subList(
                            fromIndex = 0,
                            toIndex = 6
                        ).size * 150).dp,
                        max = (product.reviews.subList(fromIndex = 0, toIndex = 6).size * 320).dp
                    )
                ) {
                    items(items = product.reviews.subList(fromIndex = 0, toIndex = 6)) { review ->
                        DisplayReview(review = review)
                    }
                }
                Text(
                    text = "Show more",
                    style = Typography.caption,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Variables.innerItemGap)
                        .clickable {
                            //TODO
                        })
            }

            //? Spacer to fix UI
            item {
                Spacer(modifier = Modifier.height(Variables.innerItemGapLow))
            }
        }

        if (remember { derivedStateOf { scrollState.firstVisibleItemIndex } }.value > 3) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = "Go up",
                tint = Variables.blue3,
                modifier = Modifier
                    .rotate(90f)
                    .align(alignment = Alignment.BottomEnd)
                    .background(shape = CircleShape, color = Color.Transparent)
                    .padding(all = 20.dp)
                    .clickable {
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(index = 0)
                        }
                    }
            )
        }
    }
}

data class Review(
    val title: String = "I was very excited for...",
    val date: String = "November 5, 2022",
    val rating: Double = 4.5,
    val reviewBody: String = "I was very excited for this and made a special order just to get it. I neglected to remember that Pilot would run finer. For me, it writes very dry even when I make my handwriting smaller. Some other super cheap nibs dip pen nibs I have found write smoother and wetter than this. I will likely pass it on. If I had the chance to try a Medium maybe I would, but I wasn’t impressed. ",
)

data class Product(
    val name: String = "Ink Pen Second In Line long long long name",
    val images: List<String> = emptyList(),
    val category: String = "Calligraphy ink brush",
    val rating: Double = 4.2,
    var description: String = "Lorem ipsum dolor sit amet consectetur. A orci vulputate id tempus duis erat bibendum lacus suspendisse. Sed commodo tristique vitae et augue leo viverra. Tincidunt in nibh elementum scelerisque in. Tempus facilisis rhoncus vel at facilisis ridiculus id eu.",
    var specifications: List<Pair<String, String>> = listOf(
        "Model Number" to "UNI 211207",
        "Manufacturer" to "Uni",
        "Body Color" to "Light Blue",
        "Body Material" to "Plastic",
        "Capped" to "No",
        "Clean-out Rod Included" to "No",
        "Clip Material" to "Plastic",
        "Clippable" to "Yes",
        "Tip Material" to "Metal",
        "Tip Replaceable" to "No"
    ),
    val reviews: List<Review> = listOf(
        Review(),
        Review(),
        Review(),
        Review(),
        Review(),
        Review(),
        Review(),
        Review(),
        Review(),
    ),
)
