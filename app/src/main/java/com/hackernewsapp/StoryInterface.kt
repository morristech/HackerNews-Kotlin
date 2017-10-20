package com.hackernewsapp


import com.hackernewsapp.data.model.Discussion
import com.hackernewsapp.data.model.Story
import retrofit.Callback
import retrofit.http.GET
import retrofit.http.Path
import rx.Observable

/**
 * @author Tosin Onikute.
 */

interface StoryInterface {

    @GET("/{story_type}.json")
    fun getStories(@Path("story_type") storyType: String): Observable<List<Long>>

    @GET("/item/{itemId}.json")
    fun getStory(@Path("itemId") itemId: String): Observable<Story>

    @GET("/item/{itemId}.json")
    fun getComment(@Path("itemId") itemId: Long): Observable<Discussion>

    @GET("/{story_type}.json")
    fun getStory2(@Path("story_type") storyType: String, response: Callback<List<Long>>)

}
