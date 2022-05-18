package com.rudder.data


//data class GetPostInfo ( // Node
//        val pagingIndex:Int,
//        val endPostId:Int,
//        val category_id:Int,
//        val token:String,
//        val searchBody:String = "",
//        val board_type:String = "bulletin"
//)



data class GetPostInfo ( // Spring
        val token : String,
        val categoryId : Int?,
        val endPostId : Int?,
        val searchBody : String?
)
