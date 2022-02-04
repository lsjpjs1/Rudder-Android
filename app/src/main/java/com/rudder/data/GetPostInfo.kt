package com.rudder.data


data class GetPostInfo (

        val pagingIndex:Int,
        val endPostId:Int,
        val category_id:Int,
        val token:String,
        val searchBody:String = "",
        val board_type:String = "bulletin"
)