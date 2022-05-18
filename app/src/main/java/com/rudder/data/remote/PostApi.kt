package com.rudder.data.remote



import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.GetPostInfo
import com.rudder.data.PreviewPost
import com.rudder.data.Response
import kotlinx.coroutines.*

class PostApi {

    companion object{
        val instance = PostApi()
    }

    private val postService : PostService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(PostService::class.java)
    private val postServiceSpring : PostService = RetrofitClientSpring.getClient(BuildConfig.BASE_URL_SPRING).create(PostService::class.java)



    fun getPostsApi(getPostInfo: GetPostInfo) : Deferred<retrofit2.Response<GetPostsResponse>> { // Spring
        return CoroutineScope(Dispatchers.IO).async{
            postServiceSpring.getPostsService( getPostInfo.token, getPostInfo.categoryId, getPostInfo.endPostId, getPostInfo.searchBody)
        }
    }

    fun getPostFromIdApi(postFromIdRequest: PostFromIdRequest) : Deferred<retrofit2.Response<PreviewPost>> { // Spring
        return CoroutineScope(Dispatchers.IO).async{
            postServiceSpring.getPostFromIdService( postFromIdRequest.token, postFromIdRequest.postId )
        }
    }



    fun getPosts(getPostInfo: GetPostInfo) : Deferred<ArrayList<PreviewPost>> {
        return GlobalScope.async(Dispatchers.IO){
                postService.renderPost(getPostInfo)

        }
    }

    fun addPostApi(addPostInfo: AddPostInfo):Deferred<Response<AddPostResponse>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.addPost(addPostInfo)
        }
    }

    fun isLikePost(isLikePostInfo:IsLikePostInfo):Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.isLikePost(isLikePostInfo)
        }
    }

    fun addLikePost(addLikePostInfo: AddLikePostInfo):Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.addLikePost(addLikePostInfo)
        }
    }

    fun addPostViewCount(addPostViewCountInfo: AddPostViewCountInfo):Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.addPostViewCount(addPostViewCountInfo)
        }
    }


    fun postFromIdApi(postFromIdRequest: PostFromIdRequest) : Deferred<Response<PostFromIdResponse>> {
        return GlobalScope.async(Dispatchers.IO){
            postService.postFromIdService(postFromIdRequest)
        }
    }



}