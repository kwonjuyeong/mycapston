package com.example.myapplication.DTO

data class BoardDTO(
    var uid : String? =null,
    var contents : String? = null,
    var postTitle : String? = null,
    var Writed_date : String? = null,
    var imageUrlWrite : String? = null,
    var timestamp : Long? = null,
    var imageWriteExplain : String? = null,
    var tag : String? = null,
    var nickname : String? = null,
    var ProfileUrl : String? = null,
    var like: MutableMap<String, Boolean> = HashMap()
) {
    data class Comment(
        var uid: String? = null,
        var userNickname: String? = null,
        var userprofile : String? = null,
        var comment: String? = null,
        var timestamp: Long? = null
    )
}
