package com.example.a7minutesworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkoutapp.databinding.ActivityExerciseBinding
import com.example.a7minutesworkoutapp.databinding.DialogCustomBackConfirmationBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding :ActivityExerciseBinding? =null
    private var restTimer : CountDownTimer? =null
    private var restProgress =0
    private var restTimerDuration:Long= 4

    private var exerciseTimer : CountDownTimer? =null
    private var exerciseProgress =0
    private var exerciseTimerDuration:Long=30

    private var exerciseList :ArrayList<ExerciseModel>? =null
    private var currentExercisePosition =-1

    private var tts :TextToSpeech?=null
    private var player :MediaPlayer? =null

    private var exerciseAdapter :ExerciseStatusAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
         exerciseList =Constants.defaultExerciseList()

        tts =TextToSpeech(this,this)

        binding?.toolbarExercise?.setNavigationOnClickListener {
           // onBackPressed()
            customDialogForBackButton()
        }
        setupRestView()
        setUpExerciseStatusRecyclerView()
    }



    private fun customDialogForBackButton(){
        val customDialog =Dialog(this)
        //we need to create own binding because the ui of custom dialog is in different xml file
        val dialogBinding =DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
          dialogBinding.btnYes.setOnClickListener {
              this@ExerciseActivity.finish()
              customDialog.dismiss()

          }
        dialogBinding.btnNo.setOnClickListener {
              customDialog.dismiss()
        }
        customDialog.show()

    }

    private fun setUpExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter=ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter=exerciseAdapter
    }

    private fun setupRestView(){

        try{
            val soundURI =Uri.parse(
                "android.resource://com.example.a7minutesworkoutapp/"+ R.raw.button_press)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping=false
            player?.start()

        }catch (e:Exception){
           e.printStackTrace()
        }

        binding?.flRestView?.visibility= View.VISIBLE
        binding?.tvTitle?.visibility=View.VISIBLE
        binding?.tvExerciseName?.visibility=View.INVISIBLE
        binding?.flExerciseView?.visibility=View.INVISIBLE
        binding?.ivImage?.visibility=View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility=View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility=View.VISIBLE


        if(restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }

        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition +1].getName()
        setResetProgressBar()
    }

    private  fun setupExerciseView(){
        binding?.flRestView?.visibility= View.INVISIBLE
        binding?.tvTitle?.visibility=View.INVISIBLE
        binding?.tvExerciseName?.visibility=View.VISIBLE
        binding?.flExerciseView?.visibility=View.VISIBLE
        binding?.ivImage?.visibility=View.VISIBLE
        binding?.tvUpcomingLabel?.visibility=View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility=View.INVISIBLE

        if(exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress =0

        }

        speckOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text =exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }
    private fun setResetProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(4000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                   restProgress++
                binding?.progressBar?.progress = (restTimerDuration-restProgress ).toInt()
                binding?.tvTimer?.text = (restTimerDuration-restProgress ).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
               setupExerciseView()

            }

        }.start()

    }


    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = (exerciseTimerDuration-exerciseProgress).toInt()
                binding?.tvTimerExercise?.text = (exerciseTimerDuration-exerciseProgress ).toString()
            }

            override fun onFinish() {


                if (currentExercisePosition < exerciseList!!.size - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }

        if(exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress =0

        }

        //Shutting down the text speech feature when activity is destroyed
        if (tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!=null){
            player!!.stop()

        }
        binding=null
    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result =tts?.setLanguage(Locale.US)

            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported!")
            }
        }

        else{
            Log.e("TTS","Initialization Failed!")
        }
    }

    private fun speckOut(text:String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}