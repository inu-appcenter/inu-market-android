package com.example.inomtest.dataClass

import kotlin.collections.HashMap

//채팅방 정보 해쉬맵
class ChatModel (val users: HashMap<String, Boolean> = HashMap(),
                 val comments : HashMap<String, Comment> = HashMap()){
    class Comment(val uid: String? = null, val message: String? = null, val time: String? = null)
}