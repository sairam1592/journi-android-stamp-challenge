package com.journiapp.stampapplication.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journiapp.stampapplication.common.DataSingleton
import com.journiapp.stampapplication.data.network.NetworkUtil
import com.journiapp.stampapplication.R
import com.journiapp.stampapplication.common.SharedPrefHelper
import com.journiapp.stampapplication.presentation.view.adapter.StampAdapter
import com.journiapp.stampapplication.model.API
import com.journiapp.stampapplication.model.LoginRequest
import com.journiapp.stampapplication.model.ProfileResponse
import com.journiapp.stampapplication.model.Stamp
import com.journiapp.stampapplication.model.StampRequest
import com.journiapp.stampapplication.model.StampResponse
import com.journiapp.stampapplication.model.UserInformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/** Main screen. Shows stamps added by the user. */
class StampActivity : AppCompatActivity() {

    var api: API? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Refactor to compose
        // setContent {
        //     MaterialTheme {
        //         // Compose view
        //     }
        // }

        setContentView(R.layout.activity_stamp)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Stamps"
        setSupportActionBar(toolbar)

        val sharedPrefHelper = SharedPrefHelper(applicationContext)

        api = NetworkUtil.providesRetrofit(applicationContext).create(API::class.java)

        val user = sharedPrefHelper.loadUserInfoJson()

        if (user == null) {
            // Note: Programmatic login to speed things up, but we would need a proper login screen
            val loginRequest = LoginRequest(
                email = "challenge@journiapp.com",
                password = "FreshAccount5"
            )
            /*api!!.login(loginRequest).enqueue(object : Callback<ProfileResponse> {

                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    var userInfo = UserInformation()
                    NetworkUtil.getCookieString(response.headers())?.let { user?.sessionCookie = it }
                    sharedPrefHelper.saveUserInfo(userInfo)

                    bindStamps(response.body()!!.stamps)
                    addStamp()
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    TODO("Handle failure")
                }
            })*/
        } else {

            /*api!!.getProfile().enqueue(object : Callback<ProfileResponse> {

                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    bindStamps(response.body()!!.stamps)
                   // addStamp()
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

                }
            })*/
        }

        findViewById<Button>(R.id.btn_search).setOnClickListener { openSearch() }
    }

    override fun onResume() {
        super.onResume()
       // addStamp()
    }

    fun bindStamps(stamp: List<Stamp>) {

        var adapter = StampAdapter()
        adapter.stamps = stamp
        val rvStamps = findViewById<RecyclerView>(R.id.rv_stamps)
        rvStamps.layoutManager = GridLayoutManager(applicationContext, 2)
        rvStamps.adapter = adapter
        adapter.notifyDataSetChanged()
    }

   /* fun addStamp() {
        if (DataSingleton.countryCode == null) return

        val request = StampRequest(
            countryCode = DataSingleton.countryCode!!,
            create = System.currentTimeMillis()
        )
        DataSingleton.countryCode = null
        api!!.postStamp(request).enqueue(object : Callback<StampResponse> {

            override fun onResponse(call: Call<StampResponse>, response: Response<StampResponse>) {

                api!!.getProfile().enqueue(object : Callback<ProfileResponse> {

                    override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                        bindStamps(response.body()!!.stamps)
                        addStamp()
                    }

                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

                    }
                })
            }

            override fun onFailure(call: Call<StampResponse>, t: Throwable) {
            }
        })
    }*/

    fun openSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }
}