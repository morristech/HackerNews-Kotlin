package com.hackernewsapp.di.modules

import android.app.Application
import com.hackernewsapp.data.StoryInteractor
import com.hackernewsapp.data.StoryInteractorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Tosin Onikute.
 */

@Module
class AppModule(private val mApplication: Application) {

    @Provides
    @Singleton
    internal fun providesApplication(): Application {
        return mApplication
    }

    @Provides
    internal fun provideDataManager(appDataManager: StoryInteractorImpl): StoryInteractor {
        return appDataManager
    }
}