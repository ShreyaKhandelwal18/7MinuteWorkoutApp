package com.example.a7minutesworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        //here root is constraint layout
        setContentView(binding?.root)

      //  val flStartBtn : FrameLayout = findViewById(R.id.flStartBtn)
        binding?.flStartBtn?.setOnClickListener{
            val intent =Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }
        binding?.flBMI?.setOnClickListener{
            val intent =Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }
        binding?.flHistory?.setOnClickListener {
            val intent =Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding =null
    }


}