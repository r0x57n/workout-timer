package com.example.workouttimer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import android.content.Context
import java.io.File
import android.util.Log

@Serializable
data class Workout(
    val title: String,
    val prepare: Int,
    val work: Int,
    val rest: Int,
    val cycles: Int,
    val sets: Int,
    val restBetween: Int,
    val cooldown: Int,
)

// Data structure to reach workouts from the savefile for workouts on the phone
class Workouts(val context: Context) {
    var workoutsFile: File
    var workouts = mutableListOf<Workout>()

    init {
        val path = context.getFilesDir().resolve("workouts.json")
        workoutsFile = File(path.path)

        if (!workoutsFile.exists()) {
            Log.v("Workouts", "Creating new JSON file.")
            workoutsFile.createNewFile()
            workoutsFile.writeText("[]") // so we can parse it as a list later
        }

        readSavefile()
    }

    fun readSavefile() {
        Log.v("Workouts", "Reading JSON file.")
        val text: String = workoutsFile.readText()
        workouts = Json.decodeFromString<MutableList<Workout>>(text)
    }

    fun writeWorkoutsToFile() {
        Log.v("Workouts", "Writing workouts to file.")
        workoutsFile.writeText(Json.encodeToString(workouts))
    }

    fun new(workout: Workout) {
        Log.v("Workouts", "Adding new workout.")
        workouts.add(workout)
        writeWorkoutsToFile()
    }

    fun update(pos: Int, workout: Workout) {
        workouts.removeAt(pos)
        workouts.add(pos, workout)
        writeWorkoutsToFile()
    }

    fun remove(pos: Int) {
        Log.v("Workouts", "Removing workout.")
        workouts.removeAt(pos)
        writeWorkoutsToFile()
    }

    fun move(from: Int, to: Int) {
        Log.v("Workouts", "Moving items.")

        val itemFrom = workouts[from]
        workouts.removeAt(from)
        workouts.add(to, itemFrom)

        writeWorkoutsToFile()
    }
}
