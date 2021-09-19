package com.example.myapplication.DTO

data class BoardDTO(
    var contents : String? = null,
    var uid : String? =null,
    var postTitle : String? = null,
    var Writed_date : String? = null,
    var imageUrlWrite : String? = null,
    var timestamp : Long? = null,
    var imageWriteExplain : String? = null,
    var tag : String? = null,
    var nickname : String? = null,
    var ProfileUrl : String? = null,
    var likeCount : Int = 0,
    var like: MutableMap<String, Boolean> = HashMap()
) {
    data class Comment(
        var UID: String? = null,
        var userNickname: String? = null,
        var userprofile : String? = null,
        var comment: String? = null,
        var timestamp: Long? = null)
}
