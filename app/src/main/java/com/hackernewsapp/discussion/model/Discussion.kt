package com.hackernewsapp.discussion.model

import java.util.*
import javax.annotation.Generated

@Generated("org.jsonschema2pojo")
class Discussion : java.io.Serializable {

    var by: String? = null
    var id: Long = 0
    var kids: List<Long>? = null
    var parent: Int? = null
    var text: String? = null
    var time: Int? = null
    var type: String? = null
    var removed: Boolean = false
    var level: Int = 0
    private val additionalProperties = HashMap<String, Any>()

    fun getId(): Long? {
        return id
    }

    fun setId(id: Long?) {
        this.id = id!!
    }

    fun getAdditionalProperties(): Map<String, Any> {
        return this.additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }

}