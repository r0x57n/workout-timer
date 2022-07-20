package com.example.workouttimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.CountDownTimer
import java.util.Timer
import java.util.TimerTask
import android.os.Handler
import android.os.Looper

import android.widget.Button
import android.widget.TextView

class PlayWorkoutActivity : AppCompatActivity() {

    enum class Part {
        prepare, work, rest, between, cooldown
    }

    lateinit var workouts: Workouts
    lateinit var currentTitleT: TextView
    lateinit var currentTimeT: TextView
    lateinit var currentCycle: TextView
    lateinit var cyclesMax: TextView
    lateinit var currentSet: TextView
    lateinit var setsMax: TextView
    lateinit var totalTimeT: TextView

    var running: Boolean = true
    var currentTitle: String = "init"
    var currentTime: Int = 0
    var currentFulltime: Int = 0
    var cycles: Int = 0
    var sets: Int = 0
    var totalTime: Long = 0
    var position: Part = Part.prepare

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_workout_activity)

        val pos = getIntent().getIntExtra("pos", -1)

        workouts = Workouts(getApplicationContext())
        val w = workouts.workouts[pos]

        // All objects in view
        val workoutTitle: TextView = findViewById(R.id.workoutTitle)
        currentTitleT = findViewById(R.id.currentTitle)
        currentTimeT = findViewById(R.id.currentTime)
        currentCycle = findViewById(R.id.currentCycle)
        cyclesMax = findViewById(R.id.cyclesMax)
        currentSet = findViewById(R.id.currentSet)
        setsMax = findViewById(R.id.setsMax)
        totalTimeT = findViewById(R.id.fullCounter)
        val fullFulltime: TextView = findViewById(R.id.fullFulltime)
        val pause: Button = findViewById(R.id.pause)
        val stop: Button = findViewById(R.id.stop)

        // Holders
        // a workout goes like this:
        // Prepare
        // ((Work + Rest) * Cycles + Rest_between) * Sets
        // Cooldown
        position = Part.prepare
        currentTitle = "Prepare"
        currentTime = w.prepare
        cycles = 1
        sets = 1
        setsMax.text = w.sets.toString()
        cyclesMax.text = w.cycles.toString()
        val fulltime: Int = (w.prepare + ((w.work + w.rest) * w.cycles + w.restBetween) * w.sets + w.cooldown) * 1000

        // Set static values
        workoutTitle.text = w.title
        fullFulltime.text = (fulltime/1000).toString()

        val timerTask = object:TimerTask() {
            override fun run() {
                if (running) {
                    when(position) {
                        Part.prepare -> {
                            if (currentTime == 1) {
                                currentTitle = "Work"
                                currentTime = w.work
                                position = Part.work
                            }
                        }
                        Part.work -> {
                            if (currentTime == 1) {
                                currentTitle = "Rest"
                                currentTime = w.rest
                                position = Part.rest
                            }
                        }
                        Part.rest -> {
                            if (currentTime == 1) {
                                if (cycles < w.cycles) {
                                    currentTitle = "Work"
                                    currentTime = w.work
                                    position = Part.work
                                    cycles++
                                } else {
                                    currentTitle = "Between"
                                    currentTime = w.restBetween
                                    position = Part.between
                                    cycles = 1
                                }
                            }
                        }
                        Part.between -> {
                            if (currentTime == 1) {
                                if (sets < w.sets) {
                                    currentTitle = "Work"
                                    currentTime = w.work
                                    position = Part.work
                                    sets++
                                } else {
                                    currentTitle = "Cooldown"
                                    currentTime = w.cooldown
                                    position = Part.cooldown
                                    sets++
                                }
                            }
                        }
                        Part.cooldown -> {
                            if (currentTime == 1) {
                                currentTitle = "Finished"
                            }
                        }
                    }

                    totalTime++
                    currentTime--
                }
            }
        }

        val timer: Timer = Timer()
        timer.schedule(timerTask, 1000, 1000)

        val handler = Handler(Looper.getMainLooper())
        handler.post(object:Runnable{
                         override fun run() {
                            updateView()
                            handler.postDelayed(this, 1)
                         }
        })

        pause.setOnClickListener {
            if (running) {
                pause.setText("Resume")
            } else {
                pause.setText("Pause")
            }

            running = !running
        }

        stop.setOnClickListener {
            finish()
        }
    }

    fun updateView() {
        currentTitleT.text = currentTitle
        currentTimeT.text = currentTime.toString()
        currentCycle.text = cycles.toString()
        currentSet.text = sets.toString()
        totalTimeT.text = totalTime.toString()
    }

}
