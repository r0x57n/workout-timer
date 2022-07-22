package com.example.workouttimer

import android.util.Log
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult

import android.widget.TextView
import android.app.AlertDialog

/*
 * Shows a clickable list of workouts, and a button to add new ones.
 * If a workout is clicked you can edit/delete it.
 */
class MainActivity : AppCompatActivity() {
    lateinit var workouts: Workouts
    lateinit var adapter: WorkoutsAdapter

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as WorkoutsAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                workouts.move(from, to)
                adapter.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                viewHolder.itemView.alpha = 1.0f
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // A data structure that handles the workouts
        workouts = Workouts(getApplicationContext())

        // The workouts list
        adapter = WorkoutsAdapter(workouts, this)
        val recycler: RecyclerView = findViewById(R.id.workouts)
        recycler.setAdapter(adapter)
        itemTouchHelper.attachToRecyclerView(recycler)

        // Adding a new workout button
        val btnNewWorkout: Button = findViewById(R.id.newWorkout)
        btnNewWorkout.setOnClickListener {
            newWorkout()
        }
    }

    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            Log.v("MainActivity", "Returned from activity, OK...")

            adapter.updateData()
            adapter.notifyDataSetChanged()
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.v("MainActivity", "Returned from activity, canceled...")
        }
    }

    fun editWorkout(pos: Int) {
        Log.v("MainActivity", "Edit workout: " + pos)

        val intent = Intent(this, EditWorkoutActivity::class.java)
        intent.putExtra("pos", pos)
        resultLauncher.launch(intent)
        adapter.notifyItemChanged(pos)
    }

    fun playWorkout(pos: Int) {
        Log.v("MainActivity", "Playing workot: " + pos)

        val intent = Intent(this, PlayWorkoutActivity::class.java)
        intent.putExtra("pos", pos)
        resultLauncher.launch(intent)
    }

    fun newWorkout() {
        Log.v("MainActivity", "New workout.")

        val intent = Intent(this, NewWorkoutActivity::class.java)
        resultLauncher.launch(intent)
    }

    fun remove(pos: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Remove workout?")
        builder.setMessage("Are you sure you want to delete workout \"" + workouts.workouts[pos].title + "\"")
        builder.setPositiveButton(android.R.string.yes) {
            dialog,
            which -> run {
                workouts.remove(pos)
                adapter.notifyItemRemoved(pos)
            }
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which -> run { } }
        builder.show()
    }
}
