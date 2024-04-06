package com.journiapp.stampapplication.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journiapp.stampapplication.common.DataSingleton
import com.journiapp.stampapplication.data.network.NetworkUtil
import com.journiapp.stampapplication.R
import com.journiapp.stampapplication.presentation.view.adapter.SearchAdapter
import com.journiapp.stampapplication.model.API
import com.journiapp.stampapplication.model.CountryCodeRequest
import com.journiapp.stampapplication.model.CountryCodeResponse
import com.journiapp.stampapplication.model.SearchSuggestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/** Search for places and show autocomplete results, return selected country (ISO2 country code). */
class SearchActivity : AppCompatActivity(), SearchAdapter.OnItemClickListener {

    var adapter: SearchAdapter? = null
    var api: API? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Search"
        setSupportActionBar(toolbar)

        api = NetworkUtil.providesRetrofit(applicationContext).create(API::class.java)

        adapter = SearchAdapter(applicationContext, this)

        val rvSearchSuggestion = findViewById<RecyclerView>(R.id.rv_search_suggestion)
        rvSearchSuggestion.layoutManager = LinearLayoutManager(applicationContext)
        rvSearchSuggestion.adapter = adapter

        findViewById<EditText>(R.id.et_search_destination).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                requestSearchSuggestions(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })


        findViewById<Button>(R.id.btn_done).setOnClickListener { v ->
            finish()
        }
    }

    fun bindSearchSuggestion(countryName: String) {
        findViewById<RecyclerView>(R.id.rv_search_suggestion).visibility = View.GONE
        val etSearchDestination = findViewById<EditText>(R.id.et_search_destination)
        etSearchDestination.setText(countryName)
        etSearchDestination.setSelection(etSearchDestination.text!!.length)
    }

    fun requestSearchSuggestions(query: String?) {
        if (query != null && query.length >= 2) {
            api!!.getCountries(query).enqueue(object : Callback<ArrayList<SearchSuggestion>> {

                override fun onResponse(call: Call<ArrayList<SearchSuggestion>>, response: Response<ArrayList<SearchSuggestion>>) {
                    adapter?.setItems(response.body()!!)
                    adapter?.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<SearchSuggestion>>, t: Throwable) {
                }
            })
        }
    }

    fun requestCountryCode(searchSuggestion: SearchSuggestion) {
        val request = CountryCodeRequest(searchSuggestion.reference)
        api!!.getCountryCode(request)
            .enqueue(object : Callback<CountryCodeResponse> {
                override fun onResponse(call: Call<CountryCodeResponse>, response: Response<CountryCodeResponse>) {
                    val countryCode = response.body()!!

                    DataSingleton.countryCode = countryCode.countryCode
                }

                override fun onFailure(call: Call<CountryCodeResponse>, throwable: Throwable) {

                }
            })
    }

    override fun onSearchSuggestionClick(position: Int) {
        // very rarely items are not updated on time. in this case the object is null and we do nothing
        val searchSuggestion = adapter!!.getItem(position) ?: return
        bindSearchSuggestion(searchSuggestion.description)
        requestCountryCode(searchSuggestion)
    }
}