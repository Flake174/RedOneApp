package com.redone.app.resources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.redone.app.R
import java.util.*


class QuotesAdapter(arrayList: ArrayList<String>) : RecyclerView.Adapter<QuotesAdapter.MyViewHolder>() {

    private val quotesList: ArrayList<String>

    init {
        quotesList = arrayList
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById<View>(R.id.quote) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = quotesList.get(position)
    }

    override fun getItemCount(): Int {
        return quotesList.size
    }

}