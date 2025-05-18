package com.ingencode.reciclaia.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper
import com.ingencode.reciclaia.ui.theme.ReciclaIaTheme
import kotlinx.coroutines.launch

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */
@Composable
fun Tutorial() {
    var pager = rememberPagerState(initialPage = 0, pageCount = { 3 }, initialPageOffsetFraction = 0f)
    val coroutineScope = rememberCoroutineScope()

    ReciclaIaTheme {
        Column(Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pager,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                pageSpacing = 16.dp
            ) { page ->
                GetContentForPage(page)
            }

            Spacer(
                modifier = Modifier
                    .height(4.dp)
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .weight(2f),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pager.pageCount) { iteration ->
                    val color =
                        if (pager.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(16.dp)
                            .clickable(enabled = true, onClick = {
                                coroutineScope.launch {
                                    pager.animateScrollToPage(iteration)
                                }
                            })
                    )
                }
            }
        }
    }
}

@Composable
fun GetContentForPage(page: Int) {
    val c = LocalContext.current
    val listTitles = arrayListOf<Int>(
        R.string.tutorial_title_1, R.string.tutorial_title_2,
        R.string.tutorial_title3
    )
    val listLotties = arrayListOf<String>("search.json", "consult.json", "learn.json")
    val listContents = arrayListOf<Int>(
        R.string.tutorial_content_1, R.string.tutorial_content_2,
        R.string.tutorial_content_3
    )

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(listLotties[page]))

    return Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the card
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp), // Add padding to the column
            horizontalAlignment = Alignment.CenterHorizontally, // Center all content horizontally
            verticalArrangement = Arrangement.spacedBy(16.dp) // Add spacing between items
        ) {

            Text(
                text = c.getString(listTitles[page]).toUpperCase(Locale.current),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center) // Center horizontally within the width
            )

            // Lottie Animation (fills the middle space and is centered horizontally)
            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .weight(1f) // Give the Lottie animation all available vertical space
                    // No fillMaxWidth() here, the horizontalAlignment of the Column will center it
                    // .aspectRatio(1f) // Keep aspectRatio if you want a square animation centered
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }


            // Description (bottom, centered horizontally)
            Text(
                text = c.getString(listContents[page]),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center) // Center horizontally within the width
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyComposeWrapper {
        Tutorial()
    }
}