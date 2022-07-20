package com.example.workouttimer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText

/*
 * The view where creation/editing/deletion of workouts happens.
 */
class EditWorkoutActivity : AppCompatActivity() {
    lateinit var workouts: Workouts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_workout_activity)

        // Available fields in the view
        val title: EditText = findViewById(R.id.title)
        val btnSave: Button = findViewById(R.id.save)

        // Workouts DS
        workouts = Workouts(getApplicationContext())

        // Get workout ID we're editing
        val pos = getIntent().getIntExtra("pos", -1)

        // Update values if we're editing a workout
        if (pos != -1) {
            title.setText(workouts.workouts[pos].title)
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }

        // Save workout
        btnSave.setOnClickListener {
            if (!title.text.equals("")) {
                //val workout: Workout = Workout(title.text.toString())
                //workouts.update(pos, workout)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }
}
