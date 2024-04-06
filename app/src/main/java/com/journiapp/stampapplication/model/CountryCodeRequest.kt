package com.journiapp.stampapplication.model

import com.google.gson.annotations.SerializedName

/** used to request an ISO2 country code for the given [reference] (See SearchSuggestion.reference) */
class CountryCodeRequest(@SerializedName("googleReference") val reference: String)