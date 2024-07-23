package com.example.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object{
        private const val METRIC_UNITS_VIEW ="METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW ="US_UNITS_VIEW"
    }

    private var currentVisibleView: String =
        METRIC_UNITS_VIEW

    private var binding :ActivityBmiBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title ="CALCULATE BMI"

        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
             onBackPressed()
        }
        makeVisibleMetricUnitsView()
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId:Int ->
            if (checkedId==R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }

         binding?.btnCalculateUnits?.setOnClickListener {
           calculateUnits()
         }

    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE // METRIC  Height UNITS VIEW is Visible
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE // METRIC  Weight UNITS VIEW is Visible
        binding?.tilUsUnitWeight?.visibility = View.GONE // make weight view Gone.
        binding?.tilUsUnitHeightFeet?.visibility = View.GONE // make height feet view Gone.
        binding?.tilUsUnitHeightInch?.visibility = View.GONE // make height inch view Gone.

        binding?.etMetricUnitHeight?.text!!.clear() // height value is cleared if it is added.
        binding?.etMetricUnitWeight?.text!!.clear() // weight value is cleared if it is added.

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE // METRIC  Height UNITS VIEW is InVisible
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE // METRIC  Weight UNITS VIEW is InVisible
        binding?.tilUsUnitWeight?.visibility = View.VISIBLE // make weight view visible.
        binding?.tilUsUnitHeightFeet?.visibility = View.VISIBLE // make height feet view visible.
        binding?.tilUsUnitHeightInch?.visibility = View.VISIBLE // make height inch view visible.

        binding?.etUsUnitWeight?.text!!.clear() // weight value is cleared.
        binding?.etUsUnitHeightFeet?.text!!.clear() // height feet value is cleared.
        binding?.etUsUnitHeightInch?.text!!.clear() // height inch is cleared.

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }


    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        //Use to set the result layout visible
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text = bmiValue // Value is set to TextView
        binding?.tvBMIType?.text = bmiLabel // Label is set to TextView
        binding?.tvBMIDescription?.text = bmiDescription // Description is set to TextView
    }

    //To check whether the text-field contains text or not
    private  fun validateMetricUnits():Boolean{
        var isValid=true

        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid=false
        }
        else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid=false
        }
        return isValid
    }

    private  fun validateUsUnits():Boolean {
        var isValid = true

        when {
            (binding?.etUsUnitWeight?.text.toString().isEmpty()) -> {
                isValid = false
            }

            (binding?.etUsUnitHeightFeet?.text.toString().isEmpty()) -> {
                isValid = false
            }

            (binding?.etUsUnitHeightInch?.text.toString().isEmpty()) -> {
                isValid = false
            }

        }
        return isValid

    }

    private fun calculateUnits(){
        if(currentVisibleView== METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                //we divide by 100 in height value to convert cm into meter
                val heightValue :Float =binding?.etMetricUnitHeight?.text.toString().toFloat()/100
                val weightValue :Float =binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi =weightValue /(heightValue*heightValue)
                displayBMIResult(bmi)
            }else{
                Toast.makeText(
                    this@BMIActivity
                    ,"Please enter valid values.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        else{
            if(validateUsUnits()){
                val heightValueFeet :String =
                    binding?.etUsUnitHeightFeet?.text.toString()
                val heightValueInch :String =
                    binding?.etUsUnitHeightInch?.text.toString()
                val usUnitWeightValue:Float=
                    binding?.etUsUnitWeight?.text.toString().toFloat()
                // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                val heightValue =
                    heightValueInch.toFloat() + heightValueFeet.toFloat() * 12

                // This is the Formula for US UNITS result.
                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                displayBMIResult(bmi)
            }

            else{
                Toast.makeText(
                    this@BMIActivity
                    ,"Please enter valid values.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}