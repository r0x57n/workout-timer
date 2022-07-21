package com.example.workouttimer

import android.widget.EditText
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

class NumberPickerH(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    lateinit private var text: EditText

    init {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.number_picker_h, this)

        text = findViewById(R.id.edit_text)
    }

    fun setValue(int: Int) {
        text.setText(int.toString())
    }

    fun getValue(): Int {
        return Integer.parseInt(text.text.toString())
    }
}
