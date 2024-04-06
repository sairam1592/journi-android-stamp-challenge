package com.journiapp.stampapplication.model

/** Represents a stamp for a given country.
 *
 * pictureGuid: identifier for loading the image of this stamp
 * create: unix timestamp in milliseconds when stamp was added */
class Stamp {
    var countryCode: String? = null
    var pictureGuid: String? = null
    var create: Long = 0
}