package com.rudder.data


data class GetPostInfo (
        val board_type:String,
        val pagingIndex:Int,
        val endPostId:Int,
        val category_id:Int,
        val token:String
)