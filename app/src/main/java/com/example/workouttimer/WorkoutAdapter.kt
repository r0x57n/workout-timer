package com.example.workouttimer

import android.util.Log
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView

/*
 * The adapter for the workouts list.
 * Specifies how the list works.
 */
class WorkoutsAdapter(private val workouts: Workouts, private val parent: MainActivity) :
    RecyclerView.Adapter<WorkoutsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val totalTime: TextView
        val play: TextView
        val delete: TextView

        init {
            title = view.findViewById(R.id.title)
            totalTime = view.findViewById(R.id.totalTime)
            play = view.findViewById(R.id.play)
            delete = view.findViewById(R.id.delete)
        }
    }

    // Sets values to the elements in the XML layout for each entry of the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val w: Workout = workouts.get(holder.getAdapterPosition())
        holder.title.text = w.title

        var totalTimeMinutes: Int = w.totalTime / 60
        var totalTimeSeconds: Int = w.totalTime - (totalTimeMinutes * 60)
        holder.totalTime.text = totalTimeMinutes.toString() + ":" + totalTimeSeconds.toString()

        holder.title.setOnClickListener {
            parent.editWorkout(holder.getAdapterPosition())
        }

        holder.play.setOnClickListener {
            parent.playWorkout(holder.getAdapterPosition())
        }

        holder.delete.setOnClickListener {
            parent.remove(holder.getAdapterPosition())
        }

    }

    // Create new views (items, invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    fun updateData() {
        workouts.readSavefile()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = workouts.workouts.size
}
