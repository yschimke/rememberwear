/*
 * https://gist.github.com/c5inco/2210c3d00c49d100dc7348b06ad58ca1
 */

package com.google.wear.soyted.previews

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

val gray100 = Color(0xff333333)
val gray200 = Color(0xff444444)
val gray400 = Color(0xff1e1e1e)
val androidLightBlue = Color(0xffd6f0ff)

val buttonShape = RoundedCornerShape(
    topStart = 2.dp,
    topEnd = 4.dp,
    bottomStart = 2.dp,
    bottomEnd = 4.dp
)

@Composable
fun RoundWatchPreviewScaffold(
    centerContent: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        Modifier
            .size(340.dp)
            .background(androidLightBlue),
        contentAlignment = Alignment.Center
    ) {
        WatchBand(
            Modifier
                .align(Alignment.TopCenter)
                .size(200.dp)
                .rotate(180f)
                .offset(y = 116.dp)
        )
        WatchBand(
            Modifier
                .align(Alignment.BottomCenter)
                .size(200.dp)
                .offset(y = 116.dp)
        )

        Box(
            contentAlignment = Alignment.Center
        ) {
            // Hardware button 1 & 2
            SecondaryButton(
                Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = -56.dp, x = 0.dp)
                    .rotate(-24f)
            )
            SecondaryButton(
                Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = 56.dp, x = 0.dp)
                    .rotate(24f)
            )

            // Main button
            MainButton(
                Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 14.dp)
            )

            // Watch face and bezel
            Box(
                Modifier
                    .border(
                        width = 4.dp,
                        brush = SolidColor(gray400),
                        shape = CircleShape
                    )
                    .border(
                        width = 7.dp,
                        brush = SolidColor(gray200),
                        shape = CircleShape
                    )
                    .border(
                        width = 16.dp,
                        brush = SolidColor(gray400),
                        shape = CircleShape
                    )
                    .padding(16.dp)
            ) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .size(240.dp)
                        .drawWithContent {
                            drawContent()

                            val (height, width) = this.size
                            val path = Path()

                            path.lineTo(width * 0.1f, 0f)
                            path.lineTo(width * 0.7f, height)
                            path.lineTo(0f, height)
                            path.lineTo(0f, 0f)
                            path.close()

                            drawPath(
                                path = path,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        gray400.copy(alpha = 0.2f),
                                        gray400.copy(alpha = 0f)
                                    )
                                )
                            )
                        }
                ) {
                    WatchShapeConfigurationProvider(isRound = true) {
                        MaterialTheme {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colors.background),
                                verticalArrangement = if (centerContent) Arrangement.Center else Arrangement.Top,
                                horizontalAlignment = if (centerContent) Alignment.CenterHorizontally else Alignment.Start
                            ) {
                                content()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ScaffoldPreview() {
    RoundWatchPreviewScaffold {
        Text(
            text = "Hello there!",
            color = MaterialTheme.colors.primary
        )
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(0.6f),
            onClick = { }
        ) {
            Text("Tap")
        }
    }
}

@Composable
private fun WatchShapeConfigurationProvider(
    isRound: Boolean,
    content: @Composable () -> Unit
) {
    val newConfiguration = Configuration(LocalConfiguration.current)
    newConfiguration.screenLayout = newConfiguration.screenLayout and
        Configuration.SCREENLAYOUT_ROUND_MASK.inv() or
        if (isRound) {
            Configuration.SCREENLAYOUT_ROUND_YES
        } else {
            Configuration.SCREENLAYOUT_ROUND_NO
        }

    CompositionLocalProvider(
        LocalConfiguration provides newConfiguration,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun SecondaryButton(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .size(height = 12.dp, width = 3.dp)
                .background(gray100)
        ) {}
        Row(
            Modifier
                .size(height = 28.dp, width = 10.dp)
                .background(
                    color = gray400,
                    shape = buttonShape
                )
                .clip(buttonShape),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(gray200)
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainButton(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .size(height = 16.dp, width = 3.dp)
                .background(gray100)
        ) {}
        Row(
            Modifier
                .size(height = 32.dp, width = 12.dp)
                .background(
                    color = gray400,
                    shape = buttonShape
                )
                .clip(buttonShape),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(gray200)
            ) {}
        }
    }
}

@Preview
@Composable
private fun WatchBand(
    modifier: Modifier = Modifier.size(360.dp)
) {
    Canvas(modifier = modifier) {
        val (height, width) = size
        val path = Path()

        path.lineTo(width, 0f)
        path.cubicTo(width, 0f, width * 0.8f, height * 0.2f, width * 0.75f, height)
        path.lineTo(width * 0.25f, height)
        path.cubicTo(width * 0.25f, height, width * 0.2f, height * 0.2f, 0f, 0f)
        path.close()

        drawPath(
            path = path,
            brush = SolidColor(Color(0xff555555))
        )
    }
}