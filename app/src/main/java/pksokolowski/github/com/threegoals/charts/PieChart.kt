package pksokolowski.github.com.threegoals.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.util.*

class PieChart : View {

    // paints
    private lateinit var mStroke: Paint
    private lateinit var mFill: Paint
    private lateinit var mText: Paint
    private lateinit var mNoDataText: Paint

    private var mPercentages: FloatArray? = null
    private var mAngles: FloatArray? = null

    private lateinit var rnd: Random
    private lateinit var mRectF: RectF
    private var mColors: IntArray? = null

    var mainColor: Int = Color.GRAY
    var noDataMessage: String? = null

    var lastTouchedIndex = -1
        private set

    var lastTouchedID: Long = -1
        private set
        get() = data?.getOrNull(lastTouchedIndex)?.ID ?: -1L

    var data: MutableList<Datum>? = null
        set(value) {
            field = value
            if (value == null || value.size == 0) {
                mAngles = null
                mPercentages = null
                invalidate()
                return
            }
            val n = value.size
            for (i in n - 1 downTo 0) {
                if (value[i].value == 0L) value.removeAt(i) else break
            }
            if (value.size == 0) {
                invalidate()
                return
            }

            var sum: Long = -1
            for (i in 0 until n) {
                sum += value[i].value
            }

            val percentages = FloatArray(n)
            val angles = FloatArray(n)
            for (i in 0 until n) {
                percentages[i] = value[i].value.toFloat() / sum
                angles[i] = value[i].value.toFloat() / sum * 360f
            }
            mPercentages = percentages
            mAngles = angles

            mColors = prepareColors(value, mainColor, 5)
            lastTouchedIndex = -1

            invalidate()
        }

    constructor(context: Context) : super(context) {
        preparePaints()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        preparePaints()
    }

    private fun dpToPixels(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
    }

    private fun preparePaints() {
        val textSizeInSpUnits = 15 //5dp

        val textSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSizeInSpUnits.toFloat(), resources.displayMetrics).toInt()

        mStroke = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.GRAY
            strokeWidth = Math.max(1, textSizeInPixels / 9).toFloat()
            isAntiAlias = true
        }

        mFill = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        mText = Paint().apply {
            color = Color.WHITE
            textSize = textSizeInPixels.toFloat()
            textAlign = Paint.Align.CENTER
        }

        mNoDataText = Paint().apply {
            color = Color.BLACK
            textSize = textSizeInPixels * 1.5f
        }

        rnd = Random()
        mRectF = RectF(0f, 0f, right.toFloat(), bottom.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val smallerOfTheTwo = Math.min(widthSize, heightSize)

        setMeasuredDimension(smallerOfTheTwo, smallerOfTheTwo)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRectF.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data == null || data!!.size == 0) {
            drawNoDataMessage(canvas)
            return
        }

        // null safety: if any of these are null, skip drawing
        val data = data ?: return
        val angles = mAngles ?: return
        val colors = mColors ?: return

        //val factor = dpToPixels(10)

        val radius = height / 2f
        var segStartAngle = START_ANGLE

        for (i in angles.indices) {
            val datum = data[i]
            mFill.color = colors[i]
            if (lastTouchedIndex != -1) {
                if (i != lastTouchedIndex) {
                    mFill.color = makeSemiTransparent(colors[i])
                }
            }
            canvas.drawArc(mRectF, segStartAngle, angles[i], true, mFill)

            // drawing text on slice
            drawTextIfThereIsSpaceEnough(canvas, segStartAngle, segStartAngle + angles[i], radius, datum.title)

            segStartAngle += angles[i]
        }
    }

    private fun drawNoDataMessage(canvas: Canvas) {
        mFill.color = mainColor
        canvas.drawArc(mRectF, START_ANGLE, 360f, true, mFill)

        if (noDataMessage == null) return
        // shift for the text color, compared to background, is applied uniformly to all channels:
        val shift = 30
        mNoDataText.color = Color.argb(255,
                Color.red(mainColor) + shift,
                Color.green(mainColor) + shift,
                Color.blue(mainColor) + shift)

        val angle = 360f
        val radius = height / 2f
        val textAngle = START_ANGLE + angle / 2
        canvas.drawText(noDataMessage!!,
                xProjectedAtAngle(textAngle, radius * 0.7f, radius),
                yProjectedAtAngle(textAngle, radius * 0.7f, radius),
                mNoDataText)
    }

    private fun prepareColors(data: MutableList<Datum>, baseColor: Int, colorStep: Int = 10): IntArray {
        val colors = IntArray(data.size)
        var r = Color.red(baseColor)
        var g = Color.green(baseColor)
        var b = Color.blue(baseColor)

        for (i in data.indices) {
            // assign color, unless there is one defined in data parameter
            val color = Color.argb(255, r, g, b)
            colors[i] = data[i].sliceColor ?: color

            // prepare next color
            r = Math.min(r + colorStep, 255)
            g = Math.min(g + colorStep, 255)
            b = Math.min(b + colorStep, 255)
        }

        return colors
    }

    private fun makeSemiTransparent(color: Int): Int {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return Color.argb(45, r, g, b)
    }

    private fun drawTextIfThereIsSpaceEnough(canvas: Canvas, startAngle: Float, endAngle: Float, radius: Float, textToDraw: String) {
        val textRadius = radius * 0.7f
        val halfFontHeight = (mText.descent() + mText.ascent()) / 2

        if (endAngle - startAngle < 90) {
            val distance = getDistance(
                    xProjectedAtAngle(startAngle, textRadius, radius), yProjectedAtAngle(startAngle, textRadius, radius) - halfFontHeight,
                    xProjectedAtAngle(endAngle, textRadius, radius), yProjectedAtAngle(endAngle, textRadius, radius) - halfFontHeight)
            if (distance <= mText.textSize) return
        }

        val textAngle = startAngle + (endAngle - startAngle) / 2
        canvas.drawText(textToDraw,
                xProjectedAtAngle(textAngle, textRadius, radius),
                yProjectedAtAngle(textAngle, textRadius, radius) - halfFontHeight,
                mText)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.action

        val x = event.x.toInt()
        val y = event.y.toInt()

        // reject and don't handle clicks outside the chart's radius
        val radius = height / 2f
        val distanceFromCenter = Math.sqrt(Math.pow((x - radius).toDouble(), 2.0) + Math.pow((y - radius).toDouble(), 2.0)).toFloat()
        if (distanceFromCenter > radius) return false

        when (eventAction) {
            MotionEvent.ACTION_UP -> {
                val touchedIndex = getIndexForAGivenXY(x, y)
                if (touchedIndex != -1) {
                    if (touchedIndex == lastTouchedIndex) {
                        lastTouchedIndex = -1
                        sliceSelectionChanged?.invoke(null)
                    } else {
                        lastTouchedIndex = touchedIndex
                        sliceSelectionChanged?.invoke(data!![touchedIndex])
                    }
                    invalidate()
                }
            }
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
        }

        // tell the View that we handled the event
        return true
    }

    private fun getIndexForAGivenXY(touch_x: Int, touch_y: Int): Int {
        if (mAngles == null) return -1
        val radius = height / 2f
        // reject clicks outside the chart's radius
        val distanceFromCenter = Math.sqrt(Math.pow((touch_x - radius).toDouble(), 2.0) + Math.pow((touch_y - radius).toDouble(), 2.0)).toFloat()
        if (distanceFromCenter > radius) return -1

        // atan2 - theta from polar coortinates (r, theta)
        var angle = Math.toDegrees(Math.atan2((touch_y - radius).toDouble(), (touch_x - radius).toDouble())).toFloat()
        // make it work for negative angles too, because theta might be small or even negative in some scenarios
        angle = (angle + 360) % 360

        var startAngle = START_ANGLE
        for (i in mAngles!!.indices) {
            if (angle > startAngle && angle < startAngle + mAngles!![i]) {
                return i
            }
            startAngle += mAngles!![i]
        }

        return -1
    }

    private fun xProjectedAtAngle(angle: Float, radius: Float, Xorigin: Float): Float {
        return Xorigin + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
    }

    private fun yProjectedAtAngle(angle: Float, radius: Float, Yorigin: Float): Float {
        return Yorigin + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
    }

    private fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0)).toFloat()
    }

    var sliceSelectionChanged: ((datumOrNull: Datum?) -> Unit)? = null

    data class Datum(val title: String, val value: Long, val ID: Long, val sliceColor: Int? = null)

    companion object {
        private const val START_ANGLE = 0f
    }
}