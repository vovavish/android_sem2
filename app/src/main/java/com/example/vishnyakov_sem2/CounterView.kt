package com.example.vishnyakov_sem2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var counterValue = 0

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }

    private val buttonPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val buttonTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    private val incrementRect = android.graphics.Rect()
    private val decrementRect = android.graphics.Rect()

    init {
        setWillNotDraw(false)
    }

    private fun updateCounterText(): String = "$counterValue"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 600
        val desiredHeight = 200

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)

        decrementRect.set(0, 0, height, height)
        incrementRect.set(width - height, 0, width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f + textPaint.textSize / 3

        canvas.drawRect(decrementRect, buttonPaint)
        canvas.drawText("-", decrementRect.centerX().toFloat(), centerY, buttonTextPaint)

        canvas.drawRect(incrementRect, buttonPaint)
        canvas.drawText("+", incrementRect.centerX().toFloat(), centerY, buttonTextPaint)

        canvas.drawText(updateCounterText(), centerX, centerY, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt()
            val y = event.y.toInt()

            when {
                incrementRect.contains(x, y) -> {
                    counterValue++
                    invalidate()
                    performClick()
                    return true
                }
                decrementRect.contains(x, y) -> {
                    counterValue--
                    invalidate()
                    performClick()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return CounterState(superState, counterValue)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is CounterState) {
            super.onRestoreInstanceState(state.superState)
            counterValue = state.value
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}

class CounterState : Parcelable {
    var superState: Parcelable?
    var value: Int

    constructor(superState: Parcelable?, value: Int) {
        this.superState = superState
        this.value = value
    }

    constructor(parcel: Parcel) {
        superState = parcel.readParcelable(Parcelable::class.java.classLoader)
        value = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(superState, flags)
        parcel.writeInt(value)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CounterState> {
        override fun createFromParcel(parcel: Parcel): CounterState {
            return CounterState(parcel)
        }

        override fun newArray(size: Int): Array<CounterState?> {
            return arrayOfNulls(size)
        }
    }
}