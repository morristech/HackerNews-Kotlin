package com.hackernewsapp.di.components

import android.content.SharedPreferences
import com.hackernewsapp.di.modules.AppModule
import com.hackernewsapp.di.modules.NetModule
import dagger.Component
import retrofit.RestAdapter
import javax.inject.Singleton


/**
 * @author Tosin Onikute.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class))
interface NetComponent {

    // downstream components need these exposed
    fun restAdapter(): RestAdapter

    fun sharedPreferences(): SharedPreferences

}
