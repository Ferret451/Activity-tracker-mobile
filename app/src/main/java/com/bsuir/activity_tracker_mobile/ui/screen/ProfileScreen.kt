package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen

private data class LanguageOption(val tag: String, val labelRes: Int)

@Composable
fun ProfileScreen() {
    SectionScreen(
        titleRes = R.string.screen_profile_title,
        purposeRes = R.string.screen_profile_purpose
    )
}