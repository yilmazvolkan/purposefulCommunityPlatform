package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_layout.view.txtName
import kotlinx.android.synthetic.main.template_item.view.*

class TemplatesAdapter(val context : Context, val userList: ArrayList<AddTempBody>): RecyclerView.Adapter<TemplatesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(userList[position].name, userList[position].fieldType, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dialog_temp, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentAddTempBody : AddTempBody? = null
        var currentPosition : Int = 0

        fun setData(name : String, fieldType : String, position: Int){
            itemView.txtName.text = name
            itemView.txtType.text = fieldType

            this.currentAddTempBody = AddTempBody(name, fieldType)
            this.currentPosition = position
        }
    }

    private fun isValidForm(layPropName: TextInputLayout, name: String, size: String, layPropType: TextInputLayout):Boolean{

        var isValid = true
        if (name.isEmpty()){
            layPropName.isErrorEnabled = true
            layPropName.error = "Name cannot be empty!"
            isValid = false
        }else{
            layPropName.isErrorEnabled = false
        }
        if (size.isEmpty()){
            layPropType.isErrorEnabled = true
            layPropType.error = "Type cannot be empty!"
            isValid = false
        }else{
            layPropType.isErrorEnabled = false
        }
        return isValid
    }
}