package com.bounswe.purposefulcommunity.Fragments.Instances

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.FieldsAdapter
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.Models.GetFieldsBody
import com.bounswe.purposefulcommunity.Models.GetTempBody
import com.bounswe.purposefulcommunity.R
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_show_template.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class ShowTemplateActivity : AppCompatActivity() {
    private var hasHeader: Boolean = false
    private val fields = ArrayList<AddTempBody>()
    private var adapter = FieldsAdapter(this@ShowTemplateActivity, fields)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_template)

        val tempID = intent.getStringExtra("temp_id")
        val tempName = intent.getStringExtra("temp_name")

        val actionBar = supportActionBar
        actionBar!!.title = tempName
        actionBar.setDisplayHomeAsUpEnabled(true)

        if(tempID!!.isNotEmpty()){
            getFields(tempID)
        }

        fabAddInst.setOnClickListener {
            val intent = Intent(this@ShowTemplateActivity, CreateInstanceActivity::class.java)
            intent.putExtra("temp_id", tempID)
            intent.putExtra("temp_name", tempName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.search -> {
            val tempID = intent.getStringExtra("temp_id")
            val intent = Intent(this@ShowTemplateActivity, SearchActivity::class.java)
            intent.putExtra("temp_id", tempID)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    private fun getFields(communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getOneTempl(communityID, tokenV!!).enqueue(object : Callback<GetTempBody> {
            override fun onFailure(call: Call<GetTempBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@ShowTemplateActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ShowTemplateActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<GetTempBody>, response: Response<GetTempBody>) {
                if (response.code() == 200) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewFields)
                    rv.layoutManager = LinearLayoutManager(this@ShowTemplateActivity, RecyclerView.VERTICAL, false)
                    rv.adapter = adapter

                    val res: GetTempBody? = response.body()
                    val myListGeneric: List<GetFieldsBody> = res!!.fieldResources
                    val myListTypes: JsonObject = res.templatesNameId
                    var isExist = true
                    for (key in myListTypes.keySet()) {
                        isExist = false
                        getFieldsInner(myListTypes.get(key).toString().replace("\"", ""))
                    }

                    for(i in myListGeneric){
                        fields.add(AddTempBody(i.fieldType, i.isRequired, i.name))
                    }
                    if(fields.isEmpty() && isExist){
                        Toast.makeText(this@ShowTemplateActivity, "No field is found!", Toast.LENGTH_SHORT).show()
                    }
                    runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@ShowTemplateActivity, "Fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun getFieldsInner(communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getOneTempl(communityID, tokenV!!).enqueue(object : Callback<GetTempBody> {
            override fun onFailure(call: Call<GetTempBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@ShowTemplateActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ShowTemplateActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<GetTempBody>, response: Response<GetTempBody>) {
                if (response.code() == 200) {
                    val res: GetTempBody? = response.body()
                    val myListGeneric: List<GetFieldsBody> = res!!.fieldResources

                    var showName = ""
                    for(i in myListGeneric) {
                        showName += "${i.name}, "
                    }
                    if(showName.length > 3){
                        showName = showName.substring(0, showName.length-2)
                    }
                    fields.add(AddTempBody(showName, true, res.name))

                    for(i in myListGeneric){
                        fields.add(AddTempBody(i.fieldType, i.isRequired, i.name))
                    }
                    runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@ShowTemplateActivity, "Fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun runLayoutAnimation() = recyclerViewFields.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()

        if (hasHeader) {
            layoutAnimationListener = object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    layoutManager?.findViewByPosition(0)?.clearAnimation()
                }
                override fun onAnimationEnd(animation: Animation?) = Unit
                override fun onAnimationRepeat(animation: Animation?) = Unit
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
