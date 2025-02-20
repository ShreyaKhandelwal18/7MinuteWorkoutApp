package com.example.a7minutesworkoutapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding :ActivityHistoryBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistoryActivity)

        if(supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title ="HISTORY"

        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        val dao=(application as WorkoutApp).db.historyDao()
        getAllCompletedDates(dao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDate().collect(){
                allCompletedDatesList->
                if(allCompletedDatesList.isNotEmpty()){
                        binding?.tvHistory?.visibility= View.VISIBLE
                        binding?.rvHistory?.visibility=View.VISIBLE
                        binding?.tvNoDataAvailable?.visibility=View.GONE

                        binding?.rvHistory?.layoutManager=LinearLayoutManager(this@HistoryActivity)

                        val dates =ArrayList<String>()
                        for (date in allCompletedDatesList){
                            dates.add(date.date)
                        }
                       val historyAdapter =HistoryAdapter(dates)

                       binding?.rvHistory?.adapter= historyAdapter
                    }else{
                        binding?.tvNoDataAvailable?.visibility=View.VISIBLE
                        binding?.tvHistory?.visibility= View.GONE
                        binding?.rvHistory?.visibility=View.GONE
                    }
            }
        }
    }
}