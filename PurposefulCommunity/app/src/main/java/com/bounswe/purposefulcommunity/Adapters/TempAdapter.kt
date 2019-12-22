package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_layout.view.eachItem
import kotlinx.android.synthetic.main.template_item.view.*

class TempAdapter(val context : Context, val tempList: ArrayList<AddTempBody>): RecyclerView.Adapter<TempAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(tempList[position].fieldType, tempList[position].isRequired, tempList[position].name, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return tempList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentSearchShow : AddTempBody? = null
        var currentPosition : Int = 0
        init {
            itemView.eachItem.setOnClickListener {

            }

        }
        fun setData(fieldType : String, isRequired : Boolean, name : String,  position: Int){
            itemView.txtTemplateName.text = name
            itemView.txtTemplateType.text = fieldType

            itemView.removeTemp.setOnClickListener {
                if(position < tempList.size){
                    tempList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            }
            this.currentSearchShow = AddTempBody(fieldType, isRequired, name)
            this.currentPosition = position
        }
    }
}