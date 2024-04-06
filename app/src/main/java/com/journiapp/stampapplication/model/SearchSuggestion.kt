package com.journiapp.stampapplication.model

/**
 * Represents a search autocomplete suggestion
 * @property description description of the place (e.g. "France"). may be shown to the user
 * @property reference a internal identifier for the given result that can be used to exchange the search suggestion for
 *      a country code using API.getCountryCode().
 * */
class SearchSuggestion(val description: String, val reference: String)