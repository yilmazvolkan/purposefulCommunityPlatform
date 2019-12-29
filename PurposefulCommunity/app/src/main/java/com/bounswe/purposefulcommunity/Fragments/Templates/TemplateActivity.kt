package com.bounswe.purposefulcommunity.Fragments.Templates

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.TempAdapter
import com.bounswe.purposefulcommunity.Fragments.Community.CommunityActivity
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.Models.CreateTemplateBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_template.*
import kotlinx.android.synthetic.main.item_show_temp.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class TemplateActivity : AppCompatActivity() {

    private val templates = ArrayList<AddTempBody>()
    private var types = arrayOf("Text", "Boolean", "Decimal", "Rational", "Date-Time", "Date", "Time")
    private var w3ctypes = arrayOf("STRING", "BOOLEAN", "DECIMAL", "FLOAT", "DATE_TIME", "DATE", "TIME")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_activity_enter_templates)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val rv = findViewById<RecyclerView>(R.id.recyclerViewShowTemplates)
        rv.layoutManager = LinearLayoutManager(this@TemplateActivity, RecyclerView.VERTICAL, false)
        var adapter = TempAdapter(this@TemplateActivity, templates)
        rv.adapter = adapter

        val color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)

        fabAddTemp.setOnClickListener {
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("Add Data")
                .setCancelable(true)
                .create()
            val editView = layoutInflater.inflate(R.layout.item_show_temp, null)
            dialogBuilder.setView(editView)

            dialogBuilder.setButton(AlertDialog.BUTTON_POSITIVE, "Add", DialogInterface.OnClickListener{
                    dialog, id ->
                val name = dialogBuilder.editPropertyName.text
                val type = w3ctypes[dialogBuilder.typeSpinner.selectedItemPosition]
                if(name.toString().isEmpty()){
                    Toast.makeText(this@TemplateActivity, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
                }
                else{
                    templates.add(AddTempBody(type, true, name.toString()))
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            })
            dialogBuilder.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()

            })
            dialogBuilder.show()
            dialogBuilder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color)
            dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color)
            dialogBuilder.typeSpinner.adapter = aa
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.temp_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.upload -> {
            if(templateIsValid()){
                if(templates.size < 1){
                    Toast.makeText(this@TemplateActivity, "Add more features!", Toast.LENGTH_SHORT).show()
                }
                else{
                    val communityID = intent.getStringExtra("comm_temp_id")
                    val communityName = intent.getStringExtra("comm_temp_name")

                    createTemplate(communityID!!, editTempName.text.toString(), communityName!!)
                }
            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    private fun templateIsValid():Boolean {
        var isValid = true
        if (editTempName.text.toString().isEmpty()) {
            layTempName.isErrorEnabled = true
            layTempName.error = "Name cannot be empty!"
            isValid = false
        } else {
            layTempName.isErrorEnabled = false
        }
        return isValid
    }
    private fun createTemplate(communityId: String, name: String, communityName: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val userID = res.getString("user_id", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        val tempBody = CreateTemplateBody(communityId, templates, name, userID!!)

        purApp.createTemp(tempBody, tokenV!!).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@TemplateActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@TemplateActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@TemplateActivity, "Template is created successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TemplateActivity, CommunityActivity::class.java)
                    intent.putExtra("comm_id", communityId)
                    intent.putExtra("comm_name", communityName)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@TemplateActivity, "Template create is failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
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
