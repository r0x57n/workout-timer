package com.example.workouttimer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import android.content.Context
import java.io.File
import android.util.Log

@Serializable
data class Workout(val pos: Int, val title: String)

class Workouts(
    val context: Context,
) {
    var workoutsFile: File
    val workouts = mutableListOf<Workout>()

    init {
        val path = context.getFilesDir().resolve("workouts.json")
        workoutsFile = File(path.path)

        if (!workoutsFile.exists()) {
            workoutsFile.createNewFile()
            Log.v("Workouts", "Creating new json file.")
        }
    }

    fun fetchWorkouts() {
        Log.v("Workouts", "Parsing json file.")

        val text = workoutsFile.readText()

        //val workouts = Json.decodeFromString<Workout>(text)
    }

    fun writeWorkouts() {
        Log.v("Workouts", "Writing workouts to file.")
        val json = Json.encodeToString(workouts)

        workoutsFile.appendText(json)
    }

    fun newWorkout() {
        Log.v("Workouts", "Adding new workout.")
        workouts.add(Workout(workouts.size, "testar"))

        val json = Json.encodeToString(workouts)
        Log.v("Workouts", "All: " + json)
    }
}
