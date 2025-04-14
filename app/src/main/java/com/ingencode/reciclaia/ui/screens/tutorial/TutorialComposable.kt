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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                modifier = Modifier.fillMaxWidth().weight(10f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                pageSpacing = 16.dp) {
                page -> GetContentForPage(page)
            }

            Spacer(modifier = Modifier.height(4.dp).weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp).weight(2f),
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
                            .clickable(enabled = true, onClick = { coroutineScope.launch {
                                pager.animateScrollToPage(iteration)
                            }})
                    )
                }
            }
        }
    }

        /*Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text("Recicla-IA", style = MaterialTheme.typography.displayLarge)
            }
        }*/

}

@Composable
fun GetContentForPage(page: Int) {
   return Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column {
            Box(
                modifier =
                Modifier
                    .padding(0.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Tutorial, página ${page+1}", fontSize = 28.sp, color = MaterialTheme.colorScheme.surface)
            }
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