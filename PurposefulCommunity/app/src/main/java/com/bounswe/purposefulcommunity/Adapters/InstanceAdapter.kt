package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.Models.EditModel
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_instance.view.*

class InstanceAdapter(val context : Context, val fieldList: ArrayList<AddTempBody>, val editModelArrayLists: ArrayList<EditModel>): RecyclerView.Adapter<InstanceAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.editText.setText(editModelArrayLists[position].getEditTextValue())
        holder.setData(fieldList[position].fieldType, fieldList[position].isRequired, fieldList[position].name, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_instance, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return fieldList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var editText: EditText
        var currentSearchShow : AddTempBody? = null
        var currentPosition : Int = 0
        init {

            editText = itemView.findViewById(R.id.txtInstanceValue) as EditText

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    editModelArrayLists[adapterPosition].setEditTextValue(editText.text.toString())
                }

                override fun afterTextChanged(editable: Editable) {

                }
            })
        }

        fun setData(fieldType : String, isRequired : Boolean, name : String,  position: Int){
        itemView.txtInstanceName.text = name
        this.currentSearchShow = AddTempBody(fieldType, isRequired, name)
        this.currentPosition = position
        }
    }
    companion object {
        lateinit var editModelArrayList: ArrayList<EditModel>
    }
}