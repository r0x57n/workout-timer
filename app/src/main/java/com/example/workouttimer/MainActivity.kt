package com.example.workouttimer

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var workouts = Workouts(getApplicationContext())
        val btnNewWorkout: Button = findViewById(R.id.newWorkout)
        btnNewWorkout.setOnClickListener {
            workouts.newWorkout()
        }

        val btnSave: Button = findViewById(R.id.save)
        btnSave.setOnClickListener {
            workouts.writeWorkouts()
        }
    }
}
