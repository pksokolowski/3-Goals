package pksokolowski.github.com.threegoals.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.View

class BarChart(context: Context, private val data: IntArray, private val maxValue: Int, private val dim2Data: IntArray, private val dim2MaxValue: Int, private val columnTitles: Array<String>? = null) : View(context) {
    private lateinit var framePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var barPaint: Paint
    private lateinit var scaleLinesPaint: Paint

    private var scaledMaxValue: Float = 1F
    private var scaledData: FloatArray

    init {
        preparePaints()
        scaledData = FloatArray(this.data.size)
        convertIntValsToFloats(height)
    }

    private fun dpToPixels(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
    }

    private fun preparePaints() {
        val textSizeInPixels = dpToPixels(15)

        textPaint = Paint().apply {
            color = Color.WHITE
            textSize = textSizeInPixels
            textAlign = Paint.Align.CENTER
        }

        framePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = dpToPixels(4)
            color = Color.GRAY
        }

        barPaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.GRAY
        }

        scaleLinesPaint = Paint().apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = Math.max(1f, dpToPixels(1))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        SpecialMeasurements(widthSize.toFloat(), heightSize.toFloat())

        //MUST CALL THIS
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        convertIntValsToFloats(h)
    }

    private fun convertIntValsToFloats(height: Int) {
        val maxYForPlotting = SpecialCalculateY(height)
        val scaleY = (maxYForPlotting - scaleLinesPaint.strokeWidth / 2F) / maxValue.toFloat()

        for (i in scaledData.indices) {
            scaledData[i] = Math.max(0, data[i]).toFloat() * scaleY
        }
        scaledMaxValue = maxValue * scaleY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val x = width
        val yBottom = height
        val y = SpecialCalculateY(yBottom)

        if (scaledData.isEmpty()) {
            SpecialOzdobyWykresu(x.toFloat(), y.toFloat(), yBottom.toFloat(), canvas)
            return
        }

        // chart data:
        var lenToUseInXScaleCalc = scaledData.size - 1
        if (data.isNotEmpty()) lenToUseInXScaleCalc = data.size - 1
        val xPerEntry = x.toFloat() / lenToUseInXScaleCalc.toFloat()

        plot(y, x, xPerEntry, canvas, 0f)
        SpecialOzdobyWykresu(x.toFloat(), y.toFloat(), yBottom.toFloat(), canvas)
    }

    /**
     * Calculates the height of the drawable area for the plotting
     * Below that y, returned by this method, are ozdoby lub nothing
     * @param y_bottom
     * @return maxY for drawable area for the plot
     */
    private fun SpecialCalculateY(y_bottom: Int): Int {
        return y_bottom - framePaint.strokeWidth.toInt()
    }

    private fun plot(y: Int, x: Int, x_per_entry: Float, canvas: Canvas, minX: Float) {
        val halfTheStrokeWidth = barPaint.strokeWidth / 2f
        val maxX = x - halfTheStrokeWidth
        val modifiedX = maxX - halfTheStrokeWidth
        var lenToUseInXScaleCalc = (scaledData.size - 1).toFloat()
        if (lenToUseInXScaleCalc < 1) lenToUseInXScaleCalc = 1F
        val modifiedXPerEntry = modifiedX / lenToUseInXScaleCalc

        // drawing
        for (i in 0 until scaledData.size) {
            if (scaledData[i] == 0f) {
                continue
            }

            var calculatedX = halfTheStrokeWidth + modifiedXPerEntry * i
            if (scaledData.size == 1) calculatedX += modifiedXPerEntry / 2
            val calculatedY = y - scaledData[i]

            // 2nd data dimension:
            barPaint.color = colorDimensionAt(i)

            canvas.drawLine(calculatedX, y.toFloat(), calculatedX, calculatedY, barPaint)

            // column titles
            if (columnTitles != null) {
                if (i in columnTitles.indices) {
                    canvas.drawText(columnTitles[i],
                            calculatedX,
                            y - dpToPixels(8),
                            textPaint
                    )
                }
            }

        }
    }

    private fun colorDimensionAt(pos: Int): Int {
        if (pos !in dim2Data.indices) return Color.RED
        val baseColor = Color.GRAY
        val r = Color.red(baseColor)
        val g = Color.green(baseColor)
        val b = Color.blue(baseColor)

        // modify green:
        val rangeLeft = 255 - g
        val percentageOfMaxScore = dim2Data[pos] / dim2MaxValue.toFloat()

        val shiftedG = Math.min(255F, g + (rangeLeft * percentageOfMaxScore))
        return Color.argb(255, r, shiftedG.toInt(), b)
    }

    private fun SpecialMeasurements(width: Float, height: Float) {
        // setting bar thickness:
        val relativeThickness = if(data.size > 48) 1F else 0.9F
        val barThickness = width / data.size.toFloat() * relativeThickness
        barPaint.strokeWidth = Math.max(1f, barThickness)
    }

    private fun SpecialOzdobyWykresu(x: Float, y: Float, y_bottom: Float, canvas: Canvas) {
        val fiftyMark = y - (scaledMaxValue / 2)
        val hundredMark = y - scaledMaxValue

        with(canvas) {
            drawScaleLine("100%", hundredMark)
            drawScaleLine(" 50%", fiftyMark)
        }

        // frame line:
        val frameLineY = y + framePaint.strokeWidth / 2
        canvas.drawLine(0F, frameLineY, x, frameLineY, framePaint)
    }

    private fun Canvas.drawScaleLine(text: String, y: Float) {
        //drawText(text, 0F, y+textPaint.textSize/2, textPaint)
        //drawLine(getScaleAwareAddedXShift(), y, this.width.toFloat(), y, scaleLinesPaint)
        drawLine(0F, y, this.width.toFloat(), y, scaleLinesPaint)

    }

    private fun getScaleAwareAddedXShift(): Float {
        return 3 * textPaint.textSize
    }
}