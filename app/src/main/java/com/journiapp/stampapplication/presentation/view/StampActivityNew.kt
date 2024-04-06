package com.journiapp.stampapplication.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.journiapp.stampapplication.presentation.view.compose.StampListView
import com.journiapp.stampapplication.presentation.viewmodel.StampViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StampActivityNew : ComponentActivity() {
    private val viewModel: StampViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StampListView(
                    viewModel,
                    openSearchScreen = { openSearch() },
                    onRemoveStamp = { stamp ->
                        viewModel.removeStamp(stamp)
                    })
            }
        }
    }

    private fun openSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }
}