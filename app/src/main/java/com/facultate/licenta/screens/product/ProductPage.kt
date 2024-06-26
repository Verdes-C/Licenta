package com.facultate.licenta.screens.product

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.DisplayLoading
import com.facultate.licenta.components.DisplayRating
import com.facultate.licenta.components.DisplayReview
import com.facultate.licenta.components.HomeScreenProductDisplay
import com.facultate.licenta.components.MenuEntry
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.model.DataState
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.screens.home.HomePageViewModel
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.Utils
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ProductPage(
    navController: NavHostController,
    productId: String,
    productCategory: String,
    viewModel: ProductPageViewModel = hiltViewModel(),
    homePageViewModel: HomePageViewModel = hiltViewModel(),
) {
    val productState by viewModel.product.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val recommendationsState by viewModel.recommendations.collectAsState()


    LaunchedEffect(key1 = Unit) {
            viewModel.updateProduct(productCategory = productCategory, productId = productId)
            viewModel.getRecommendedProducts()
    }

    var quantity by remember {
        mutableIntStateOf(1)
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var descriptionMaxLines by remember {
        mutableIntStateOf(3)
    }

    var specificationHeightHidden by remember {
        mutableStateOf(true)
    }

    if (productState is DataState.Loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DisplayLoading()
        }
    } else if (productState is DataState.Error) {
        val error = (productState as DataState.Error).exception
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "${error.message}", style = Typography.pBig)
        }
    } else if (productState is DataState.Success) {
        val product = (productState as DataState.Success).data
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
                        displayArrow = true,
                        menuEntry = MenuEntry(
                            name = product.name,
                            iconDescription = product.name,
                        ),
                        navController = navController,
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
                            items(items = product.images) { image ->
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current).data(image)
                                        .crossfade(true).build(),
                                    placeholder = painterResource(R.drawable.image_placeholder),
                                    contentDescription = "Image for ${product.name}",
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier
                                        .requiredWidth(width = 256.dp)
                                        .requiredHeight(height = 192.dp)
                                        .clip(shape = RoundedCornerShape(Variables.cornerRadius))
                                        .background(color = Variables.grey1)
                                )
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
                            Icon(painter = painterResource(id = if (isFavorite) R.drawable.icon_favorites else R.drawable.icon_not_favorite),
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                                tint = if (isFavorite) Color.Red else Variables.grey6,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                            viewModel.toggleFavorite(
                                                productId = productId,
                                                productCategory = productCategory
                                            )
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

                        Row(

                        ) {
                            Text(
                                text = "$${
                                    String.format(
                                        Locale.US, "%.2f", Utils.calculateTotal(
                                            price = product.price - product.discount * product.price,
                                            quantity = quantity
                                        )
                                    )
                                }", style = Typography.h4.copy(
                                    fontSize = 23.sp
                                )
                            )
                        }

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
                                viewModel.addToCart(
                                    productId = productId,
                                    productCategory = productCategory,
                                    quantity = quantity,
                                    discount = product.discount
                                )
                            }
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
                        Text(text = "Show more",
                            style = Typography.caption,
                            modifier = Modifier.clickable {
                                val route = "search/${Uri.encode("Find Something New")}/null"
                                navController.navigate(route)
                            })
                    }
                    LazyRow(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
                    ) {
                        if (recommendationsState is DataState.Loading) {
                            item {
                                DisplayLoading()
                            }
                        } else if (recommendationsState is DataState.Error) {
                            val error = (recommendationsState as DataState.Error).exception
                            item {
                                Text(text = "${error.message}", style = Typography.pBig)
                            }
                        } else if (recommendationsState is DataState.Success) {
                            val products = (recommendationsState as DataState.Success).data
                            items(items = products) { product ->
                                HomeScreenProductDisplay(
                                    productImageDescription = product.description,
                                    productName = product.name,
                                    productId = product.id,
                                    productCategory = product.category,
                                    productImage = product.images.first(),
                                    productPrice = product.price,
                                    discount = product.discount,
                                    viewModel = homePageViewModel
                                ) {
                                    val route =
                                        "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                                    navController.navigate(route)
                                }
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
                            text = "Reviews", style = Typography.h4, modifier = Modifier.weight(1f)
                        )
                        Text(text = "Show more",
                            style = Typography.caption,
                            modifier = Modifier.clickable {
                                //TODO
                            })
                    }
                    val endIndex = minOf(4, product.reviews.size)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow),
                        modifier = Modifier.heightIn(
                            min = 160.dp, max = (product.reviews.subList(
                                fromIndex = 0, toIndex = endIndex
                            ).size * 260 + 60).dp
                        )
                    ) {
                        if (product.reviews.size > 0) {
                            items(
                                items = product.reviews.subList(
                                    fromIndex = 0, toIndex = endIndex
                                )
                            ) { review ->
                                DisplayReview(review = review)
                            }
                        } else {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "There are no reviews yet. Be the first to share your thought!",
                                        style = Typography.p,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                    Text(text = "Show more",
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
                Icon(painter = painterResource(id = R.drawable.icon_arrow_left),
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
                        })
            }
        }
    }
}


