package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen

@Composable
fun DashboardScreen() {
    SectionScreen(
        titleRes = R.string.screen_dashboard_title,
        purposeRes = R.string.screen_dashboard_purpose
    )
}
