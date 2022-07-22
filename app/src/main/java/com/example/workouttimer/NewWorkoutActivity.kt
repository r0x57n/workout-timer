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
        setContentView(R.layout.new_and_edit_workout_activity)

        // Available fields in the view
        val title: EditText = findViewById(R.id.title)
        val prepare: NumberPickerH = findViewById(R.id.prepare)
        val work: NumberPickerH = findViewById(R.id.work)
        val rest: NumberPickerH = findViewById(R.id.rest)
        val cycles: NumberPickerH = findViewById(R.id.cycles)
        val sets: NumberPickerH = findViewById(R.id.sets)
        val restBetween: NumberPickerH = findViewById(R.id.rest_between)
        val cooldown: NumberPickerH = findViewById(R.id.cooldown)
        val btnSave: Button = findViewById(R.id.save)

        // Workouts DS
        workouts = Workouts(getApplicationContext())

        // Save workout
        btnSave.setOnClickListener {
            if (title.text.isBlank()) {
                title.setError("Can't be empty.")
            } else {
                var w: Workout = Workout(
                    title = title.text.toString(),
                    prepare = prepare.getValue(),
                    work = work.getValue(),
                    rest = rest.getValue(),
                    cycles = cycles.getValue(),
                    sets = sets.getValue(),
                    restBetween = restBetween.getValue(),
                    cooldown = cooldown.getValue(),
                    totalTime = 0,
                )

                w.totalTime = w.prepare + w.cooldown + (w.work + w.rest) * w.cycles * w.sets + w.sets * w.restBetween

                workouts.new(w)
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
