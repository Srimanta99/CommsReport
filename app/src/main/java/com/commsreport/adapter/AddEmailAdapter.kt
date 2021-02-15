package com.commsreport.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemEmailAddBinding
import com.commsreport.model.EmailModel
import com.commsreport.screens.fragments.addemail.AddEmailFragment
import com.commsreport.screens.home.HomeActivity

class AddEmailAdapter(
    val activity: HomeActivity,
    val addemaillist: ArrayList<EmailModel>,
    val addEmailFragment: AddEmailFragment, ): RecyclerView.Adapter<AddEmailAdapter.ViewHolder>() {
    var  itemView: ItemEmailAddBinding?=null
    class ViewHolder(val  itemView: ItemEmailAddBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEmailAddBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.etName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.etEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvAdd.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvDelete.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.rlDelete.setOnClickListener {
            addEmailFragment.addemaillist.removeAt(position)
            notifyDataSetChanged()

        }

        itemView!!.rlAdd.setOnClickListener {
            val emailModel=EmailModel("name","test@gmail.com")
            addEmailFragment.addemaillist.add(emailModel)
            notifyDataSetChanged()

        }

        itemView!!.etName.addTextChangedListener(object :TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addEmailFragment.addemaillist.get(position).email=p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        itemView!!.etEmail.addTextChangedListener(object :TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addEmailFragment.addemaillist.get(position).name=p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    override fun getItemCount(): Int {
        return  addemaillist.size;
    }

}