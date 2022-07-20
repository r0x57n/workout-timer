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
        val workoutID: TextView
        val delete: TextView

        init {
            // Define the different components of the view
            title = view.findViewById(R.id.title)
            workoutID = view.findViewById(R.id.workoutID)
            delete = view.findViewById(R.id.delete)
        }
    }

    // Sets values to the elements in the XML layout for each entry of the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //viewHolder.workoutID.text = workouts.workouts[position].id.toString()
        holder.workoutID.text = holder.getAdapterPosition().toString()
        holder.title.text = workouts.workouts[holder.getAdapterPosition()].title

        holder.delete.setOnClickListener {
            workouts.remove(holder.getAdapterPosition())
            notifyItemRemoved(holder.getAdapterPosition())
        }

        holder.title.setOnClickListener {
            parent.editWorkout(holder.getAdapterPosition())
            notifyItemChanged(holder.getAdapterPosition())
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
