package com.bounswe.purposefulcommunity.Fragments.Templates

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_create_template.*

class CreateTemplateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_template)

        val actionBar = supportActionBar
        actionBar!!.title = "Create Template"
        actionBar.setDisplayHomeAsUpEnabled(true)

        btn_enter_fields.setOnClickListener {
            val name = editTempName.text.toString()
            val size = editFieldNumber.text.toString()
            if(isValidForm(name, size)){
                val intent = Intent(this@CreateTemplateActivity, EnterTemplatesActivity::class.java)
                intent.putExtra("temp_name", name)
                intent.putExtra("temp_size", size)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
        }
    }
    private fun isValidForm(name: String, size: String):Boolean{

        var isValid = true
        if (name.isEmpty()){
            layTempName.isErrorEnabled = true
            layTempName.error = "Name cannot be empty!"
            isValid = false
        }else{
            layTempName.isErrorEnabled = false
        }
        if (size.isEmpty()){
            layFieldNumber.isErrorEnabled = true
            layFieldNumber.error = "Size cannot be empty!"
            isValid = false
        }
        else if (size.toInt() > 7){
            layFieldNumber.isErrorEnabled = true
            layFieldNumber.error = "Size cannot be more than 7!"
            isValid = false
        }
        else{
            layFieldNumber.isErrorEnabled = false
        }
        return isValid
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
