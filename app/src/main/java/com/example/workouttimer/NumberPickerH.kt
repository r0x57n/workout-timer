package com.example.workouttimer

import android.widget.EditText
import android.widget.Button
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

class NumberPickerH(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
   private var text: EditText
   private var incMax: Button
   private var incMin: Button
   private var decMax: Button
   private var decMin: Button

    init {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.number_picker_h, this)

        text = findViewById(R.id.text)
        incMax = findViewById(R.id.inc_max)
        incMin = findViewById(R.id.inc_min)
        decMax = findViewById(R.id.dec_max)
        decMin = findViewById(R.id.dec_min)

        incMax.setOnClickListener {
            setValue(getValue() + 5)
        }

        incMin.setOnClickListener {
            setValue(getValue() + 1)
        }

        decMax.setOnClickListener {
            var int: Int = getValue()

            if (int >= 5)
                setValue(getValue() - 5)
            else
                setValue(0)
        }

        decMin.setOnClickListener {
            var int: Int = getValue()

            if (int > 0)
                setValue(getValue() - 1)
        }
    }


    fun setValue(int: Int) {
        text.setText(int.toString())
    }

    fun getValue(): Int {
        return Integer.parseInt(text.text.toString())
    }
}
