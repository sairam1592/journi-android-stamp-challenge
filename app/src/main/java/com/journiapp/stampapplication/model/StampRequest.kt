package com.journiapp.stampapplication.model

/**
 * Model for the body of a post stamp request (See API.postStamp())
 * [countryCode] ISO2 country code (upper-case) of the stamp to add or delete
 * [create] unix timestamp (milliseconds) when stamp was added. list of stamps is sorted in ascending order of this date.
 * [delete] true to delete the stamp
 */
class StampRequest(val countryCode: String, val create: Long, val delete: Boolean? = null)