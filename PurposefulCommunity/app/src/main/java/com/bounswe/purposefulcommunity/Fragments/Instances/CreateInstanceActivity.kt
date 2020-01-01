package com.bounswe.purposefulcommunity.Fragments.Instances

import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.InstanceAdapter
import com.bounswe.purposefulcommunity.Models.*
import com.bounswe.purposefulcommunity.R
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_create_instance.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CreateInstanceActivity : AppCompatActivity() {

    lateinit var editModelArrayList: ArrayList<EditModel>
    private var hasHeader: Boolean = false
    private val fields = ArrayList<AddTempSelectedBody>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_instance)

        val tempID = intent.getStringExtra("temp_id")
        val tempName = intent.getStringExtra("temp_name")

        val actionBar = supportActionBar
        actionBar!!.title = "Post: $tempName"
        actionBar.setDisplayHomeAsUpEnabled(true)

        if(tempID!!.isNotEmpty()){
            getFields(tempID)
        }


        fabSaveIns.setOnClickListener {
            createInstance(tempID)
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
                        this@CreateInstanceActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CreateInstanceActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<GetTempBody>, response: Response<GetTempBody>) {
                if (response.code() == 200) {


                    val res: GetTempBody? = response.body()
                    val myListGeneric: List<GetFieldsBody> = res!!.fieldResources
                    val myListTypes: JsonObject = res.templatesNameId
                    var isExist = true
                    for (key in myListTypes.keySet()) {
                        isExist = false
                        getFieldsInner(key, myListTypes.get(key).toString().replace("\"", ""))
                    }

                    for(i in myListGeneric){
                        fields.add(AddTempSelectedBody(i.fieldType, i.isRequired, i.name, "BbBb"))
                    }
                    if(isExist){
                        editModelArrayList = populateList(fields.size, fields)
                        val rv = findViewById<RecyclerView>(R.id.recyclerViewInstanceValues)
                        rv.layoutManager = LinearLayoutManager(this@CreateInstanceActivity, RecyclerView.VERTICAL, false)

                        var adapter = InstanceAdapter(this@CreateInstanceActivity, fields, editModelArrayList)
                        rv.adapter = adapter
                        runLayoutAnimation()
                        adapter.notifyDataSetChanged()
                    }
                    if(fields.isEmpty() && isExist){
                        Toast.makeText(this@CreateInstanceActivity, "No field is found!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@CreateInstanceActivity, "Fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun getFieldsInner(key: String, communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getOneTempl(communityID, tokenV!!).enqueue(object : Callback<GetTempBody> {
            override fun onFailure(call: Call<GetTempBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@CreateInstanceActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CreateInstanceActivity,
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
                    fields.add(AddTempSelectedBody(showName, true, res.name, "NnNn"))

                    for(i in myListGeneric){
                        fields.add(AddTempSelectedBody(i.fieldType, i.isRequired, i.name, key))
                    }
                    editModelArrayList = populateList(fields.size, fields)
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewInstanceValues)
                    rv.layoutManager = LinearLayoutManager(this@CreateInstanceActivity, RecyclerView.VERTICAL, false)

                    var adapter = InstanceAdapter(this@CreateInstanceActivity, fields, editModelArrayList)
                    rv.adapter = adapter
                    runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@CreateInstanceActivity, "Fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun populateList(size: Int, fields : ArrayList<AddTempSelectedBody>): ArrayList<EditModel> {

        val list = ArrayList<EditModel>()

        for (i in 0 until size) {
            val editModel = EditModel()
            if(fields[i].fieldType == "TIME"){
                editModel.setEditTextValue("hh:mm:ss")
            }
            else if(fields[i].fieldType == "DATE"){
                editModel.setEditTextValue("yyyy-MM-dd")
            }
            else if(fields[i].fieldType == "DATE_TIME"){
                editModel.setEditTextValue("yyyy:MM:ddThh:mm:ss")
            }
            else if(fields[i].fieldType == "DECIMAL"){
                editModel.setEditTextValue("Number")
            }
            else if(fields[i].fieldType == "STRING"){
                editModel.setEditTextValue("Text")
            }
            else if(fields[i].fieldType == "BOOLEAN"){
                editModel.setEditTextValue("True or False")
            }
            else if(fields[i].fieldType == "FLOAT"){
                editModel.setEditTextValue("Rational")
            }
            else{
                if(fields[i].parent == "NnNn"){
                    editModel.setEditTextValue(" is a community type.")
                }
                else{
                    editModel.setEditTextValue("Value")
                }
            }
            list.add(editModel)
        }
        return list
    }

    private fun createInstance(templateId: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        val jsonRes = JsonObject()

        val items = HashMap<String, JsonObject>()

        var isValid = true

        for (i in 0 until editModelArrayList.size) {
            if(fields[i].fieldType == "FLOAT"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("-?\\d+(\\.\\d+)?".toRegex())){
                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toFloat())
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toFloat())
                    }
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid number input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "DECIMAL"){
                if(editModelArrayList[i].getEditTextValue()!!.isDigitsOnly()){
                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toInt())
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toInt())
                    }
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid number input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "TIME"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}".toRegex())){
                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                    }
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid time input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "DATE"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}".toRegex())){
                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                    }
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid date input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "DATE_TIME"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}T[0-9]{2}:[0-9]{2}:[0-9]{2}".toRegex())){
                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                    }
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid date/time input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "BOOLEAN"){
                if(editModelArrayList[i].getEditTextValue()!!.equals("true", true)){

                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, true)
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, true)
                    }
                }
                else if(editModelArrayList[i].getEditTextValue()!!.equals("false", true)){
                    if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                        var jsonTemp = JsonObject()
                        if(items.containsKey(fields[i].parent)){
                            jsonTemp = items[fields[i].parent]!!
                        }
                        jsonTemp.addProperty(fields[i].name, false)
                        items[fields[i].parent] = jsonTemp
                    }
                    else if(fields[i].parent == "BbBb"){
                        jsonRes.addProperty(fields[i].name, false)
                    }
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid date/time input!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if(fields[i].parent != "NnNn" && fields[i].parent != "BbBb"){
                    var jsonTemp = JsonObject()
                    if(items.containsKey(fields[i].parent)){
                        jsonTemp = items[fields[i].parent]!!
                    }
                    jsonTemp.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                    items[fields[i].parent] = jsonTemp
                }
                else if(fields[i].parent == "BbBb"){
                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                }
            }
        }

        for ((k, v) in items) {
            jsonRes.add(k, v)
        }
        items.clear()
        if(isValid){
            val instanceBody = CreateInstanceBody(jsonRes, templateId)

            purApp.createInstance(instanceBody, tokenV!!).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if(t.cause is ConnectException){
                        Toast.makeText(
                            this@CreateInstanceActivity,
                            "Check your connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        Toast.makeText(
                            this@CreateInstanceActivity,
                            "Something bad happened!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(this@CreateInstanceActivity, "Instance is created successfully!", Toast.LENGTH_SHORT).show()
                        onSupportNavigateUp()
                    } else {
                        Toast.makeText(this@CreateInstanceActivity, "Instance create is failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
    private fun runLayoutAnimation() = recyclerViewInstanceValues.apply {
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
