package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.runtime.Composable
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen

@Composable
fun NutritionScreen() {
    SectionScreen(
        titleRes = R.string.screen_nutrition_title,
        purposeRes = R.string.screen_nutrition_purpose
    )
}
