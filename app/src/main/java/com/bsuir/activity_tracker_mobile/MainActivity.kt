package com.bsuir.activity_tracker_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bsuir.activity_tracker_mobile.data.repository.InMemoryTrackerRepository
import com.bsuir.activity_tracker_mobile.ui.ActivityTrackerApp
import com.bsuir.activity_tracker_mobile.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = InMemoryTrackerRepository()

        setContent {
            AppTheme {
                ActivityTrackerApp(repository = repository)
            }
        }
    }
}
