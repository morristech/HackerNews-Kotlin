package com.hackernewsapp.di.modules

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.hackernewsapp.Api
import dagger.Module
import dagger.Provides
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import javax.inject.Singleton

/**
 * @author Tosin Onikute.
 */

@Module
class NetModule {

    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    internal fun provideRestAdapter(): RestAdapter {

        //return restAdapter.create(StoryInterface.class);
        return RestAdapter.Builder()
                .setEndpoint(Api.BASE_URL)
                .setConverter(GsonConverter(GsonBuilder().create()))
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
    }


}
