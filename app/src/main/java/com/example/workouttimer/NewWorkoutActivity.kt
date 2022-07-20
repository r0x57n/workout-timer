package com.example.workouttimer

import com.example.workouttimer.Workout
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
        val prepare: EditText = findViewById(R.id.prepare)
        val work: EditText = findViewById(R.id.work)
        val rest: EditText = findViewById(R.id.rest)
        val cycles: EditText = findViewById(R.id.cycles)
        val sets: EditText = findViewById(R.id.sets)
        val restBetween: EditText = findViewById(R.id.rest_between)
        val cooldown: EditText = findViewById(R.id.cooldown)
        val btnSave: Button = findViewById(R.id.save)

        // Workouts DS
        workouts = Workouts(getApplicationContext())

        // Save workout
        btnSave.setOnClickListener {
            if (title.text.isBlank()) {
                title.setError("Can't be empty.")
            } else {
                val workout: Workout = Workout(
                    title = title.text.toString(),
                    prepare = Integer.parseInt(prepare.text.toString()),
                    work = Integer.parseInt(work.text.toString()),
                    rest = Integer.parseInt(rest.text.toString()),
                    cycles = Integer.parseInt(cycles.text.toString()),
                    sets = Integer.parseInt(sets.text.toString()),
                    restBetween = Integer.parseInt(restBetween.text.toString()),
                    cooldown = Integer.parseInt(cooldown.text.toString())
                )

                workouts.new(workout)
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
