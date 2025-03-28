package com.example.vishnyakov_sem2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.os.Parcel
import android.os.Parcelable

class CounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var counterValue = 0
    private var counterText: TextView
    private var incrementButton: Button
    private var decrementButton: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.counter_view, this, true)

        counterText = findViewById(R.id.counter_text)
        incrementButton = findViewById(R.id.increment_button)
        decrementButton = findViewById(R.id.decrement_button)

        updateCounterText()

        incrementButton.setOnClickListener {
            counterValue++
            updateCounterText()
        }

        decrementButton.setOnClickListener {
            counterValue--
            updateCounterText()
        }
    }

    private fun updateCounterText() {
        counterText.text = "$counterValue"
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return CounterState(superState, counterValue)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is CounterState) {
            super.onRestoreInstanceState(state.superState)
            counterValue = state.value
            updateCounterText()
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