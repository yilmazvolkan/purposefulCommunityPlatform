package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.ShowInstanceBody
import com.bounswe.purposefulcommunity.Models.UpperInstanceShowBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_show_inst.view.*

class ShowInstanceAdapter(val context : Context, val instancesList: ArrayList<UpperInstanceShowBody>): RecyclerView.Adapter<ShowInstanceAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(instancesList[position].name, instancesList[position].createdDate, instancesList[position].fieldList, instancesList[position].tempFieldList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_show_inst, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return instancesList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentSearchShow : UpperInstanceShowBody? = null
        var currentPosition : Int = 0

        fun setData(name: String, createdDate: String, fieldList: ArrayList<ShowInstanceBody>, innerFields: ArrayList<ShowInstanceBody>, position: Int){

            val newDate = "Created: $createdDate"
            //itemView.txtInstanceDate.text = newDate

            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.weight = 3.0f

            val param2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            param2.weight = 6.0f

            val tvDynamic0 = TextView(context)
            tvDynamic0.textSize = 14f
            tvDynamic0.typeface = Typeface.DEFAULT_BOLD
            tvDynamic0.setPadding(6,6,4,8)
            tvDynamic0.text = name
            itemView.InstanceOutCard.addView(tvDynamic0)

            if(fieldList.size > 0){
                for (i in 0 until fieldList.size){
                    val card_view = CardView(context)
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // CardView width
                        LinearLayout.LayoutParams.WRAP_CONTENT // CardView height
                    )
                    card_view.radius = 10F
                    card_view.layoutParams = layoutParams
                    layoutParams.bottomMargin = 6
                    card_view.setContentPadding(3,3,3,3)
                    card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground1))
                    card_view.cardElevation = 3.6F

                    val linearCH = LinearLayout(context)
                    linearCH.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    linearCH.orientation = LinearLayout.HORIZONTAL

                    val tvDynamic = TextView(context)
                    tvDynamic.layoutParams = param2
                    tvDynamic.textSize = 14f
                    tvDynamic.setPadding(4)
                    tvDynamic.text = fieldList[i].parent + " : " +  fieldList[i].name

                    val tvDynamic2 = TextView(context)
                    tvDynamic2.layoutParams = param
                    tvDynamic2.textSize = 14f
                    tvDynamic.setPadding(4)
                    tvDynamic2.text = fieldList[i].value

                    linearCH.addView(tvDynamic)
                    linearCH.addView(tvDynamic2)
                    card_view.addView(linearCH)
                    itemView.InstanceOutCard.addView(card_view)
                }
            }



            if(innerFields.size > 0){
                for (i in 0 until innerFields.size){

                    val card_view = CardView(context)
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // CardView width
                        LinearLayout.LayoutParams.WRAP_CONTENT // CardView height
                    )
                    card_view.radius = 10F
                    card_view.layoutParams = layoutParams
                    layoutParams.bottomMargin = 6
                    card_view.setContentPadding(3,3,3,3)
                    card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground2))
                    card_view.cardElevation = 3.6F

                    val linearCH = LinearLayout(context)
                    linearCH.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    linearCH.orientation = LinearLayout.HORIZONTAL

                    val tvDynamic = TextView(context)
                    tvDynamic.layoutParams = param2
                    tvDynamic.textSize = 14f
                    tvDynamic.setPadding(4)
                    //tvDynamic.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    tvDynamic.text = innerFields[i].parent + " : " +  innerFields[i].name

                    val tvDynamic2 = TextView(context)
                    tvDynamic2.layoutParams = param
                    tvDynamic2.textSize = 14f
                    tvDynamic.setPadding(4)
                    tvDynamic2.text = innerFields[i].value

                    linearCH.addView(tvDynamic)
                    linearCH.addView(tvDynamic2)
                    card_view.addView(linearCH)
                    itemView.InstanceOutCard.addView(card_view)
                }
            }

            val tvDynamic3 = TextView(context)
            tvDynamic3.textSize = 12f
            tvDynamic3.setPadding(6,8,4,4)
            tvDynamic3.text = newDate
            itemView.InstanceOutCard.addView(tvDynamic3)

            this.currentSearchShow = UpperInstanceShowBody(name, createdDate, fieldList, innerFields)
            this.currentPosition = position
        }
    }
}