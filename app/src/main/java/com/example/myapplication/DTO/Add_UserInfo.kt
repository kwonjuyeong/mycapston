package com.example.myapplication.DTO

import java.sql.Timestamp

data class Add_UserInfo(
    var uid: String? = null,
    var userId: String? = null,
    var name: String? = null,
    var explain: String? = null,
    var phoneNumber: String? = null,
    var gender: String? = null,
    var nickname: String? = null,
    var photoUrl: String? = null,
    var timestamp: String? = null
)