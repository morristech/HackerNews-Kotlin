package com.hackernewsapp.story.model

import java.util.*
import javax.annotation.Generated

@Generated("org.jsonschema2pojo")
class Story : java.io.Serializable {

    var by: String? = null
    var descendants: Int? = null
    var id: Long? = null
    var kids: List<Long>? = null
    var score: Int? = null
    var time: Int? = null
    var title: String? = null
    var type: String? = null
    var url: String? = null
    private val additionalProperties = HashMap<String, Any>()

    fun getAdditionalProperties(): Map<String, Any> {
        return this.additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }

}


