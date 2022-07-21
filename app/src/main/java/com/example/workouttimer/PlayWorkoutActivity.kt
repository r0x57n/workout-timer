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
import android.media.MediaPlayer

class PlayWorkoutActivity : AppCompatActivity() {

    enum class Part {
        prepare, work, rest, between, cooldown, finished
    }

    var quit: Boolean = false

    lateinit var workouts: Workouts
    lateinit var currentTitleT: TextView
    lateinit var currentTimeT: TextView
    lateinit var currentCycle: TextView
    lateinit var fullCounterT: TextView
    lateinit var cyclesMax: TextView
    lateinit var currentSet: TextView
    lateinit var setsMax: TextView

    var running: Boolean = true
    var currentTitle: String = "init"
    var currentTime: Int = 0
    var currentFulltime: Int = 0
    var cycles: Int = 0
    var sets: Int = 0
    var fullCounter: Int = 0
    var position: Part = Part.prepare

    val timer: Timer = Timer()
    val handler = Handler(Looper.getMainLooper())

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
        fullCounterT = findViewById(R.id.fullCounter)
        val pause: Button = findViewById(R.id.pause)
        val stop: Button = findViewById(R.id.stop)

        // Media player plays a "tick tick toook" the last three ticks of any part of the workout
        val mp_c5: MediaPlayer = MediaPlayer.create(this, R.raw.beep_c5)
        val mp_c6: MediaPlayer = MediaPlayer.create(this, R.raw.beep_c6)

        // Initialize timers and labels
        workoutTitle.text = w.title
        position = Part.prepare
        currentTitle = "Prepare"
        currentTime = w.prepare
        cycles = 1
        sets = 1
        setsMax.text = w.sets.toString()
        cyclesMax.text = w.cycles.toString()
        fullCounter = w.prepare + w.cooldown + (w.work + w.rest) * w.cycles * w.sets + w.sets * w.restBetween

        // The timer task is run every second and is the backbone to the workout timer
        val timerTask = object:TimerTask() {
            override fun run() {
                if (running) {
                    // play sounds
                    if (currentTime == 3 || currentTime == 2) {
                        mp_c5.start()
                    } else if (currentTime == 1) {
                        mp_c6.start()
                    }

                    when(position) {
                        Part.prepare -> {
                            if (currentTime == 1) {
                                currentTitle = "Work"
                                currentTime = w.work + 1
                                position = Part.work
                            }
                        }
                        Part.work -> {
                            if (currentTime == 1) {
                                currentTitle = "Rest"
                                currentTime = w.rest + 1
                                position = Part.rest
                            }
                        }
                        Part.rest -> {
                            if (currentTime == 1) {
                                if (cycles < w.cycles) {
                                    currentTitle = "Work"
                                    currentTime = w.work + 1
                                    position = Part.work
                                    cycles++
                                } else {
                                    currentTitle = "Between"
                                    currentTime = w.restBetween + 1
                                    position = Part.between
                                    cycles = 1
                                }
                            }
                        }
                        Part.between -> {
                            if (currentTime == 1) {
                                if (sets < w.sets) {
                                    currentTitle = "Work"
                                    currentTime = w.work + 1
                                    position = Part.work
                                    sets++
                                } else {
                                    currentTitle = "Cooldown"
                                    currentTime = w.cooldown + 1
                                    position = Part.cooldown
                                }
                            }
                        }
                        Part.cooldown -> {
                            if (currentTime == 1) {
                                currentTitle = "Finished"
                                position = Part.finished
                                running = false
                            }
                        }
                        Part.finished -> {

                        }
                    }

                    fullCounter--
                    currentTime--
                }
            }
        }
        timer.schedule(timerTask, 1000, 1000)

        // We need to update the UI from the main thread (timertask is it's own thread)
        handler.post(object:Runnable{
                         override fun run() {
                            updateView()

                            if (!quit) {
                                handler.postDelayed(this, 1000)
                            }
                         }
        })

        /* click listeners */
        pause.setOnClickListener {
            if (running) {
                pause.setText("Resume")
            } else {
                pause.setText("Pause")
            }

            running = !running
        }

        stop.setOnClickListener {
            quitView()
        }
    }

    // Updates things in the view
    fun updateView() {
        currentTitleT.text = currentTitle
        currentTimeT.text = currentTime.toString()
        currentCycle.text = cycles.toString()
        currentSet.text = sets.toString()
        fullCounterT.text = fullCounter.toString()
    }

    override fun onBackPressed() {
        quitView()
    }

    fun quitView() {
        timer.cancel()
        quit = true
        finish()
    }
}
