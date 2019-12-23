package com.bounswe.purposefulcommunity.Fragments.Templates

import android.content.Context
import android.os.Bundle
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
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_explore.*
import kotlinx.android.synthetic.main.activity_show_template.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class ShowTemplateActivity : AppCompatActivity() {
    private var hasHeader: Boolean = false

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

        }
    }
    private fun getFields(id: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getFields(id, tokenV!!).enqueue(object : Callback<List<GetFieldsBody>> {
            override fun onFailure(call: Call<List<GetFieldsBody>>, t: Throwable) {
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
            override fun onResponse(call: Call<List<GetFieldsBody>>, response: Response<List<GetFieldsBody>>) {
                if (response.code() == 200) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewFields)
                    rv.layoutManager = LinearLayoutManager(this@ShowTemplateActivity, RecyclerView.VERTICAL, false)

                    val fields = ArrayList<AddTempBody>()

                    var adapter = FieldsAdapter(this@ShowTemplateActivity, fields)
                    rv.adapter = adapter

                    val res: List<GetFieldsBody>? = response.body()

                    for(i in res.orEmpty()){
                        fields.add(AddTempBody(i.fieldType, i.isRequired, i.name))
                    }
                    if(fields.isEmpty()){
                        Toast.makeText(this@ShowTemplateActivity, "No field is found!", Toast.LENGTH_SHORT).show()
                    }
                    //runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@ShowTemplateActivity, "Your fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun runLayoutAnimation() = recyclerView.apply {
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
