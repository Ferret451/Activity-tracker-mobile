package com.bsuir.activity_tracker_mobile.ui.screen

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.bsuir.activity_tracker_mobile.R
import com.bsuir.activity_tracker_mobile.ui.components.SectionScreen

private data class LanguageOption(
    val tag: String?,
    val labelRes: Int,
)

private val languageOptions = listOf(
    LanguageOption(tag = null, labelRes = R.string.language_option_system),
    LanguageOption(tag = "en", labelRes = R.string.language_option_english),
    LanguageOption(tag = "ru", labelRes = R.string.language_option_russian),
)

private fun currentLanguageTag(locales: LocaleListCompat): String? {
    if (locales.isEmpty) return null
    return locales[0]?.toLanguageTag()?.lowercase()
}

@Composable
fun LanguageScreen(
    onBack: () -> Unit
) {
    val locales = AppCompatDelegate.getApplicationLocales()
    val currentTag = currentLanguageTag(locales)

    SectionScreen(
        titleRes = R.string.language_label,
        purposeRes = R.string.screen_language_purpose,
        content = {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.action_back))
                    }
                }
            }

            languageOptions.forEach { option ->
                item {
                    val selected = when {
                        option.tag == null -> currentTag == null
                        currentTag == null -> false
                        else -> currentTag.startsWith(option.tag)
                    }

                    Card(
                        onClick = {
                            val newLocales = when (val tag = option.tag) {
                                null -> LocaleListCompat.getEmptyLocaleList()
                                else -> LocaleListCompat.forLanguageTags(tag)
                            }
                            AppCompatDelegate.setApplicationLocales(newLocales)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selected,
                                onClick = null
                            )
                            Text(
                                text = stringResource(option.labelRes),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

