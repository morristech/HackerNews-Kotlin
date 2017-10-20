package com.hackernewsapp.di.modules


import com.hackernewsapp.StoryInterface
import dagger.Module
import dagger.Provides
import retrofit.RestAdapter


/**
 * @author Tosin Onikute.
 */

@Module
class RetrofitModule {

    @Provides
    fun providesStoryInterface(restAdapter: RestAdapter): StoryInterface {
        return restAdapter.create(StoryInterface::class.java)
    }
}