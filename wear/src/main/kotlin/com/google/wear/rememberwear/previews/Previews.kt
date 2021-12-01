package com.google.wear.rememberwear.previews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.google.wear.rememberwear.ui.RememberTheMilkTheme
import java.time.LocalDateTime
import java.time.ZoneOffset

object Previews {
    val timestamp = LocalDateTime.of(2021, 10, 26, 12, 34).toInstant(ZoneOffset.UTC)

}

@Composable
fun RememberTheMilkThemePreview(
    round: Boolean = true,
    content: @Composable () -> Unit,
) {
    RememberTheMilkTheme {
        if (round) {
            val configuration =
                LocalConfiguration.current.let {
                    Configuration(it).apply {
                        screenLayout =
                            (screenLayout or Configuration.SCREENLAYOUT_ROUND_YES) //xor Configuration.SCREENLAYOUT_ROUND_YES
                    }
                }

            CompositionLocalProvider(LocalConfiguration provides configuration) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                ) {
                    content()
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                content()
            }
        }
    }
}