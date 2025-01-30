package com.sajeg.haka

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sajeg.haka.waka.classes.WakaTodayData
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun GitBranchVisualization(
    modifier: Modifier = Modifier,
    branches: List<WakaTodayData>
) {
    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val secondary = MaterialTheme.colorScheme.secondary.toArgb()
    val tertiary = MaterialTheme.colorScheme.tertiary.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelMedium.copy(color = Color(textColor))

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        val lineThickness = 8.dp.toPx()
        val dotRadius = 10.dp.toPx()
        val heightOffset = 50.dp.toPx()
        val textOffsetY = 20.dp.toPx()
        val textOffsetX = 20.dp.toPx()
        val pathLength = 40.dp.toPx()

        val mainLineY = size.height / 2
        val startX = 50f
        val endX = size.width - 50f

        drawCircle(
            color = Color(primary),
            radius = dotRadius,
            center = Offset(startX, mainLineY)
        )

        drawLine(
            color = Color(primary),
            start = Offset(startX, mainLineY),
            end = Offset(endX, mainLineY),
            strokeWidth = lineThickness
        )

        drawCircle(
            color = Color(primary),
            radius = dotRadius,
            center = Offset(endX, mainLineY)
        )

        drawText(
            textMeasurer = textMeasurer,
            text = "${branches.first().name}\n${branches.first().text}",
            topLeft = Offset(startX + 15.dp.toPx(), mainLineY + 5.dp.toPx()),
            style = textStyle
        )

        for ((index, branch) in branches.subList(1, branches.size).withIndex()) {
            val branchStartX = size.width / branches.size * (index + 0.5F)
            val branchEndY = if (index % 2 == 0) mainLineY - heightOffset else mainLineY + heightOffset
            val color = if (index % 2 == 0) Color(secondary) else Color(tertiary)

            val path = Path().apply {
                moveTo(branchStartX, mainLineY)
                cubicTo(
                    branchStartX + pathLength, mainLineY,
                    branchStartX + pathLength, branchEndY,
                    branchStartX + pathLength * 2, branchEndY
                )
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = lineThickness,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(branchStartX, mainLineY)
            )

            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(branchStartX + 2 * pathLength, branchEndY)
            )

            val text = "${branch.name}\n${branch.text}"

            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset((branchStartX + 2 * pathLength) + textOffsetX, branchEndY - textOffsetY),
                style = textStyle
            )
        }
    }
}