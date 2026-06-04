package com.example.c64plasmaeffectdemo

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            C64PlasmaEffect()
        }
    }
}


@Composable
fun C64PlasmaEffect() {

    //classic 320x200 >> half pixels
    val width = 160
    val height = 100

    val pixels = remember { IntArray(width * height) }
    var plasmaBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        var time = 0f
        while (isActive) {
            time += 0.08f
            generatePlasmaPixels(width, height, time, pixels)
            plasmaBitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
            delay(16)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        plasmaBitmap?.let { img ->
            Image(
                bitmap = img.asImageBitmap(),
                contentDescription = "C64 Authetic Plasma Effect with Dithering",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

private val C64_PALETTE = intArrayOf(
    0xFF000000.toInt(), 0xFFFFFFFF.toInt(), 0xFF880000.toInt(), 0xFFAAFFEE.toInt(),
    0xFFCC44CC.toInt(), 0xFF00CC55.toInt(), 0xFF0000AA.toInt(), 0xFFEEEE77.toInt(),
    0xFFDD8855.toInt(), 0xFF664400.toInt(), 0xFFFF9999.toInt(), 0xFF333333.toInt(),
    0xFF777777.toInt(), 0xFF99FF99.toInt(), 0xFF9999FF.toInt(), 0xFFBBBBBB.toInt()
)

private fun generatePlasmaPixels(width: Int, height: Int, time: Float, pixels: IntArray) {
    val wHalf = width * 0.5f
    val hHalf = height * 0.5f
    val f1 = 0.045f
    val f2 = 0.055f
    val f3 = 0.035f
    val f4 = 0.040f
    val scaleToIndex = 1.875f // $15 / 8$

    val bayer = intArrayOf(
        0, 8, 2, 10, 12, 4, 14, 6,
        3, 11, 1, 9, 15, 7, 13, 5
    )

    for (y in 0 until height) {
        for (x in 0 until width) {
            val dx = x - wHalf
            val dy = y - hHalf
            val dist = sqrt(dx * dx + dy * dy)

            val v = sin(x * f1 + time) +
                    sin(y * f2 + time) +
                    sin((x + y) * f3 + time) +
                    sin(dist * f4 + time)

            val floatIdx = ((v + 4f) * scaleToIndex).coerceIn(0f, 15f)

            val idx1 = floatIdx.toInt()
            val idx2 = (idx1 + 1).coerceAtMost(15)

            val frac = floatIdx - idx1

            val ditherThreshold = bayer[(y % 4) * 4 + (x % 4)] * 0.0625f

            val pixelIdx = if (frac >= ditherThreshold) idx2 else idx1

            pixels[y * width + x] = C64_PALETTE[pixelIdx]
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlasmaPreview() {
    C64PlasmaEffect()
}