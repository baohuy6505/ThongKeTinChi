import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CertificateStamp(modifier: Modifier = Modifier) {
    val xValue = 1
    val outerRadiusDp = (50 + xValue).dp
    val innerRadiusDp = (38 + xValue).dp
    val numTeeth = 10 + xValue

    Canvas(modifier = modifier.size(160.dp)) {
        val cx = size.width / 2
        val cy = size.height / 2
        val outerRadiusPx = outerRadiusDp.toPx()
        val innerRadiusPx = innerRadiusDp.toPx()

        val path = Path()
        val angleStep = (2 * PI) / (numTeeth * 2)

        for (i in 0 until numTeeth * 2) {
            val radius = if (i % 2 == 0) outerRadiusPx else innerRadiusPx
            val angle = i * angleStep - (PI / 2)
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()

        val gradientBrush = Brush.linearGradient(
            colors = listOf(Color.Green, Color.Blue),
            start = Offset(cx - outerRadiusPx, cy - outerRadiusPx),
            end = Offset(cx + outerRadiusPx, cy + outerRadiusPx)
        )

        drawPath(
            path = path,
            brush = gradientBrush
        )
        drawPath(
            path = path,
            color = Color.Black,
            style = Stroke(width = 1.dp.toPx())
        )

        val circleRadius = innerRadiusPx - 4.dp.toPx()
        drawCircle(
            color = Color.Black,
            radius = circleRadius,
            center = Offset(cx, cy),
            style = Stroke(width = 2.dp.toPx())
        )
        val innerCircleRadius = circleRadius - 6.dp.toPx()
        drawCircle(
            color = Color.Black,
            radius = innerCircleRadius,
            center = Offset(cx, cy),
            style = Stroke(width = 2.dp.toPx())
        )

        // 4. VẼ DẢI RUY-BĂNG 3D
        val rbW = innerRadiusPx * 2.0f
        val rbH = outerRadiusPx * 0.45f
        val curve = 12.dp.toPx()

        val tailW = outerRadiusPx * 0.45f
        val tailH = rbH * 0.95f
        val offsetX = innerRadiusPx * 0.85f
        val offsetY = 8.dp.toPx()

        // 4.1. Nhánh ngoài (đuôi nheo)
        fun drawTail(isLeft: Boolean) {
            val side = if (isLeft) -1 else 1
            val tailPath = Path().apply {
                val startX = cx + (offsetX * side)
                val endX = cx + ((offsetX + tailW) * side)
                val yBase = cy + offsetY
                moveTo(startX, yBase - tailH / 2)
                lineTo(endX, yBase - tailH / 2)
                lineTo(endX - (12.dp.toPx() * side), yBase) // cắt đuôi nheo
                lineTo(endX, yBase + tailH / 2)
                lineTo(startX, yBase + tailH / 2)
                close()
            }
            drawPath(tailPath, brush = gradientBrush)
            // Lớp đen mờ tạo bóng khuất
            drawPath(tailPath, color = Color.Black.copy(alpha = 0.3f))
            drawPath(tailPath, color = Color.Black, style = Stroke(0.5.dp.toPx()))
        }
        drawTail(true)
        drawTail(false)

        // 4.2. Nếp gấp
        val foldLeft = Path().apply {
            moveTo(cx - rbW/2, cy + rbH/2)
            lineTo(cx - offsetX, cy + offsetY + tailH/2)
            lineTo(cx - offsetX, cy + offsetY - tailH/2)
            close()
        }
        drawPath(foldLeft, brush = gradientBrush)
        drawPath(foldLeft, color = Color.Black.copy(alpha = 0.6f))
        drawPath(foldLeft, color = Color.Black, style = Stroke(0.5.dp.toPx()))

        val foldRight = Path().apply {
            moveTo(cx + rbW/2, cy + rbH/2)
            lineTo(cx + offsetX, cy + offsetY + tailH/2)
            lineTo(cx + offsetX, cy + offsetY - tailH/2)
            close()
        }
        drawPath(foldRight, brush = gradientBrush)
        drawPath(foldRight, color = Color.Black.copy(alpha = 0.6f))
        drawPath(foldRight, color = Color.Black, style = Stroke(0.5.dp.toPx()))

        // 4.3. Mảnh giữa CONG VÒM
        val mainPath = Path().apply {
            val L = cx - rbW / 2
            val R = cx + rbW / 2
            val T = cy - rbH / 2
            val B = cy + rbH / 2
            moveTo(L, T)
            cubicTo(L, T, cx, T - curve, R, T)
            lineTo(R, B)
            cubicTo(R, B, cx, B - curve, L, B)
            close()
        }
        drawPath(mainPath, brush = gradientBrush)
        drawPath(mainPath, color = Color.Black, style = Stroke(1.5.dp.toPx()))

        // 5. IN CHỮ SỐ X LÊN GIỮA (Dùng nativeCanvas)
        drawContext.canvas.nativeCanvas.apply {
            val p = Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = Paint.Align.CENTER
                textSize = 45.dp.toPx()
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                // Đổ bóng chữ
                setShadowLayer(5f, 0f, 0f, android.graphics.Color.BLACK)
            }
            val text = xValue.toString()
            val b = Rect()
            p.getTextBounds(text, 0, text.length, b)
            drawText(text, cx, cy + b.height() / 2f - curve / 2f, p)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CertificateStampPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CertificateStamp()
    }
}