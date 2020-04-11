package com.hoony.youtubeplayer.application

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class MyApp : Application() {
    init {
        val mapper = ObjectMapper().registerModule(KotlinModule())
    }
}