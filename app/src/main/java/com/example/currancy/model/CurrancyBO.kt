package com.example.currancy.model

import com.google.gson.JsonObject

data class CurrancyBO (
    var base:String,
    var date:String,
    var rates:Map<String,Double>
)
data class RateBO (
    var cur: String ,
    var curVal: Double
)