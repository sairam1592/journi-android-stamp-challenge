package com.journiapp.stampapplication.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.journiapp.stampapplication.R
import com.journiapp.stampapplication.model.SearchSuggestion
import java.util.*

class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.SearchSuggestionView>() {

    lateinit var context: Context
    lateinit var listener: OnItemClickListener

    var searchSuggestions: ArrayList<SearchSuggestion>? = null

    constructor(context: Context, listener: OnItemClickListener) : this() {
        this.context = context
        this.listener = listener
        searchSuggestions = ArrayList()
    }

    fun setItems(searchSuggestions: ArrayList<SearchSuggestion>) {
        this.searchSuggestions = searchSuggestions
    }

    fun getItem(position: Int): SearchSuggestion? {
        return if (searchSuggestions!!.size <= position) null else searchSuggestions!![position]
    }

    override fun getItemCount(): Int {
        return searchSuggestions!!.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionView {
        return SearchSuggestionView(LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false))
    }

    override fun onBindViewHolder(holder: SearchSuggestionView, position: Int) {
        val searchSuggestion = searchSuggestions!![position]
        val tvDestination = holder.itemView.findViewById<TextView>(R.id.tv_destination)
        tvDestination.text = searchSuggestion.description
        tvDestination.setOnClickListener { v -> listener.onSearchSuggestionClick(position) }
    }

    inner class SearchSuggestionView internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun onSearchSuggestionClick(position: Int)
    }
}