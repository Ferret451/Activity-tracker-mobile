package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.appcompat.app.AppCompatDelegate
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen

private fun currentLanguageLabelRes(locales: LocaleListCompat): Int? {
    val tag = locales[0]?.toLanguageTag()?.lowercase()
    return when {
        tag == null -> null
        tag.startsWith("en") -> R.string.language_option_english
        tag.startsWith("ru") -> R.string.language_option_russian
        else -> null
    }
}

@Composable
fun ProfileScreen(
    onOpenLanguage: () -> Unit
) {
    val locales = AppCompatDelegate.getApplicationLocales()
    val currentRes = currentLanguageLabelRes(locales)
    val currentLabel = when {
        locales.isEmpty -> stringResource(R.string.language_option_system)
        currentRes != null -> stringResource(currentRes)
        else -> locales[0]?.toLanguageTag() ?: stringResource(R.string.language_option_system)
    }

    SectionScreen(
        titleRes = R.string.screen_profile_title,
        purposeRes = R.string.screen_profile_purpose,
        content = {
            item {
                Card(
                    onClick = onOpenLanguage,
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = stringResource(R.string.language_label),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp)
                    )
                    Text(
                        text = currentLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
                    )
                }
            }
        }
    )
}