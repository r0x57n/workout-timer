package com.example.workouttimer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText

/*
 * The view where creation/editing/deletion of workouts happens.
 */
class NewWorkoutActivity : AppCompatActivity() {
    lateinit var workouts: Workouts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_workout_activity)

        // Available fields in the view
        val title: EditText = findViewById(R.id.title)
        val btnSave: Button = findViewById(R.id.save)

        // Workouts DS
        workouts = Workouts(getApplicationContext())

        // Save workout
        btnSave.setOnClickListener {
            if (!title.text.equals("")) {
                workouts.new(title.text.toString())
                setResult(RESULT_OK)
                finish() // finishes the intent
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }
}
