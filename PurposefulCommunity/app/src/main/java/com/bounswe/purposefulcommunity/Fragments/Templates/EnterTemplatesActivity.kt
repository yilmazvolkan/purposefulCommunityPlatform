package com.bounswe.purposefulcommunity.Fragments.Templates

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_enter_templates.*

class EnterTemplatesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_templates)

        val actionBar = supportActionBar
        actionBar!!.title = "Create Template"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val tempName = intent.getStringExtra("temp_name")
        val tempSize = intent.getStringExtra("temp_size")

        var types = arrayOf("Text", "Boolean", "Decimal", "Rational", "Duration", "Date-Time", "Date", "Time")
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        templateCreator(tempSize!!.toInt(), aa)
        create_temp.setOnClickListener {
            if(templateIsValid()){

            }
        }
    }
    private fun templateCreator(size : Int, aa: ArrayAdapter<String>){
        for(i in 1..size){
            if(i == 1){
                item1.visibility = View.VISIBLE
                type1.adapter = aa
            }
            else if(i == 2){
                item2.visibility = View.VISIBLE
                type2.adapter = aa
            }
            else if(i == 3){
                item3.visibility = View.VISIBLE
                type3.adapter = aa
            }
            else if(i == 4){
                item4.visibility = View.VISIBLE
                type4.adapter = aa
            }
            else if(i == 5){
                item5.visibility = View.VISIBLE
                type5.adapter = aa
            }
            else if(i == 6){
                item6.visibility = View.VISIBLE
                type6.adapter = aa
            }
            else if(i == 7){
                item7.visibility = View.VISIBLE
                type7.adapter = aa
            }
        }
    }

    private fun templateIsValid():Boolean{
        var isValid = true

        if (editPropName1.text.toString().isEmpty()){
            layPropName1.isErrorEnabled = true
            layPropName1.error = "Name 1 cannot be empty!"
            isValid = false
        }else{
            layPropName1.isErrorEnabled = false
        }
        if (editPropName2.text.toString().isEmpty()){
            layPropName2.isErrorEnabled = true
            layPropName2.error = "Name 2 cannot be empty!"
            isValid = false
        }else{
            layPropName2.isErrorEnabled = false
        }
        if (editPropName3.text.toString().isEmpty()){
            layPropName3.isErrorEnabled = true
            layPropName3.error = "Name 3 cannot be empty!"
            isValid = false
        }else{
            layPropName3.isErrorEnabled = false
        }
        if (editPropName4.text.toString().isEmpty()){
            layPropName4.isErrorEnabled = true
            layPropName4.error = "Name 4 cannot be empty!"
            isValid = false
        }else{
            layPropName4.isErrorEnabled = false
        }
        if (editPropName5.text.toString().isEmpty()){
            layPropName5.isErrorEnabled = true
            layPropName5.error = "Name 5 cannot be empty!"
            isValid = false
        }else{
            layPropName5.isErrorEnabled = false
        }
        if (editPropName6.text.toString().isEmpty()){
            layPropName6.isErrorEnabled = true
            layPropName6.error = "Name 6 cannot be empty!"
            isValid = false
        }else{
            layPropName6.isErrorEnabled = false
        }
        if (editPropName7.text.toString().isEmpty()){
            layPropName7.isErrorEnabled = true
            layPropName7.error = "Name 7 cannot be empty!"
            isValid = false
        }else{
            layPropName7.isErrorEnabled = false
        }
        return isValid
    }
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
