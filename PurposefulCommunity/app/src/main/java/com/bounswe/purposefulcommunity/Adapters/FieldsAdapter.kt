package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_field.view.*

class FieldsAdapter(val context : Context, val fieldList: ArrayList<AddTempBody>): RecyclerView.Adapter<FieldsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(fieldList[position].fieldType, fieldList[position].isRequired, fieldList[position].name, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_field, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return fieldList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentSearchShow : AddTempBody? = null
        var currentPosition : Int = 0
        init {
            itemView.eachItemField.setOnClickListener {

            }
        }
        fun setData(fieldType : String, isRequired : Boolean, name : String,  position: Int){
            itemView.txtFieldName.text = name
            val res = "Type: $fieldType"
            itemView.txtFieldType.text = res
            this.currentSearchShow = AddTempBody(fieldType, isRequired, name)
            this.currentPosition = position
        }
    }
}