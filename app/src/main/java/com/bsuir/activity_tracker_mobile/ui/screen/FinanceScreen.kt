package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.components.ScreenHeader

@Composable
fun FinanceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ScreenHeader(
            titleRes = R.string.screen_finance_title,
            descriptionRes = R.string.screen_finance_purpose
        )
    }
}
