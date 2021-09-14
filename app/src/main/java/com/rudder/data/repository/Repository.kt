package com.rudder.data.repository

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.rudder.BuildConfig
import com.rudder.data.*
import com.rudder.data.local.App.Companion.prefs
import com.rudder.data.remote.LoginApi
import com.rudder.data.remote.PostApi
import com.rudder.data.remote.SignUpApi
import com.rudder.data.remote.TokenApi
import com.rudder.data.remote.*
import okhttp3.RequestBody


class Repository {

    suspend fun login(loginInfo: LoginInfo) : Boolean{
        val key = BuildConfig.TOKEN_KEY
        val result : Boolean
        val userIdKey = "userId"

        if(prefs.getValue(key) == null || prefs.getValue(key) == "" ){ // 토큰이 비어있는 상태, 로그인의 서버요청이 필요한 상태
            val resLogin =  LoginApi.instance.login(loginInfo).await()
            val value = resLogin.results
            val flag = value.get("success").asBoolean
            if (flag){
                val loginToken = value.get("token").asString
                prefs.setValue(key, loginToken)
                prefs.setValue(userIdKey,loginInfo.userId)
                result = true
            } else{
                result = false
            }
        } else { //      자동 로그인 하는 상태, 로그인의 서버요청이 필요하지 않는 상태, 토큰이 차있는 상태, 토큰 유효 검사 해야됨.
            result = checkToken(TokenInfo(prefs.getValue(key)!!))
        }
        return result
    }


    suspend fun signUpSendVerifyCode(emailInfoSignUp : EmailInfoSignUp) : String{
        val verifyAPIResult = SignUpApi.instance.emailSignUp(emailInfoSignUp).await()
        return verifyAPIResult.results.get("fail").asString
    }

    suspend fun signUpIdDuplicated(idDuplicatedInfo: IdDuplicatedInfo) : Boolean{
        val idDuplicatedAPIResultJson = SignUpApi.instance.idDuplicatedSignUp(idDuplicatedInfo).await()
        return idDuplicatedAPIResultJson.results.get("isDuplicated").asBoolean
    }

    suspend fun signUpCheckVerifyCode(checkVeriCodeInfo: CheckVerifyCodeInfo) : Boolean {
        val checkVerifyAPIResult = SignUpApi.instance.checkVerifySignUp(checkVeriCodeInfo).await()
        return checkVerifyAPIResult.results.get("isSuccess").asBoolean
    }

    private suspend fun checkToken(tokenInfo: TokenInfo): Boolean {
        val tokenAPIResultJson = TokenApi.instance.tokenValidation(tokenInfo).await()
        return tokenAPIResultJson.results.get("isTokenValid").asBoolean
    }


    suspend fun signUpSchoolList(): JsonArray {
        val schoolListFlagAPIResultJson = SignUpApi.instance.schoolListSignUp().await()
        return schoolListFlagAPIResultJson.results
    }


    suspend fun signUpNickNameDuplicated(nickNameDuplicatedInfo: nickNameDuplicatedInfo) : Boolean{
        val nickNameDuplicatedAPIResultJson = SignUpApi.instance.nickNameDuplicatedSignUpApi(nickNameDuplicatedInfo).await()
        return nickNameDuplicatedAPIResultJson.results.get("isDuplicated").asBoolean
    }


    suspend fun getPosts(pagingIndex:Int, endPostId:Int,categoryId:Int,token:String): ArrayList<PreviewPost>{
        return PostApi.instance.getPosts(pagingIndex, endPostId,categoryId,token).await()
    }

    suspend fun getComments(getCommentInfo: GetCommentInfo): ArrayList<Comment> {
        val resJson :Response<ArrayList<Comment>> = CommentApi.instance.getComments(getCommentInfo).await()
        return resJson.results
    }

    suspend fun addComment(addCommentInfo: AddCommentInfo) : Boolean{
        val resJson = CommentApi.instance.addComment(addCommentInfo).await()
        return resJson.results.get("isSuccess").asBoolean
    }

    suspend fun addPost(addPostInfo: AddPostInfo): AddPostResponse{
        val response = PostApi.instance.addPostApi(addPostInfo).await()
        return response.results
    }

    suspend fun signUpCreateAccount(signUpInsertInfo: SignUpInsertInfo) : Boolean { // Sign up, Complete!
        val createAccountAPIResult = SignUpApi.instance.createAccountSignUp(signUpInsertInfo).await()
        return createAccountAPIResult.results.get("signUpComplete").asBoolean
    }

    suspend fun findAccountID(emailInfo: EmailInfo) : Boolean {
        val forgotIDAPIResult = ForgotApi.instance.findForgotID(emailInfo).await()
        return forgotIDAPIResult.results.get("sendIdToEmail").asBoolean
    }

    suspend fun findAccountPassword(emailInfo: EmailInfo) : Boolean {
        val forgotPasswordAPIResult = ForgotApi.instance.findForgotPassword(emailInfo).await()
        return forgotPasswordAPIResult.results.get("sendPwVerificationCode").asBoolean
    }


    suspend fun sendAccountPassword(verifyInfo : CheckVerifyCodeInfo) : Boolean {
        val sendAccountPasswordAPIResult = ForgotApi.instance.sendPassword(verifyInfo).await()
        return sendAccountPasswordAPIResult.results.get("isSuccessForgot").asBoolean
    }

    suspend fun getCategories(): ArrayList<Category>{
        return BoardInfoApi.instance.getCategoryList().await().results
    }

    suspend fun isLikePost(isLikePostInfo: IsLikePostInfo): Boolean{
        return PostApi.instance.isLikePost(isLikePostInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun addLikePost(addLikePostInfo: AddLikePostInfo): JsonObject{
        return PostApi.instance.addLikePost(addLikePostInfo).await().results
    }

    suspend fun addLikeComment(addLikeCommentInfo: AddLikeCommentInfo): JsonObject{
        return CommentApi.instance.addLikeComment(addLikeCommentInfo).await().results
    }

    suspend fun addPostViewCount(addPostViewCountInfo: AddPostViewCountInfo): Boolean{
        return PostApi.instance.addPostViewCount(addPostViewCountInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun deletePostRepository(deletePostInfo: DeletePostInfo) : Boolean{
        return DeleteApi.instance.deletePostApi(deletePostInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun deleteCommentRepository(deleteCommentInfo: DeleteCommentInfo) : Boolean{
        return DeleteApi.instance.deletecommentApi(deleteCommentInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun editPostRepository(editPostInfo: EditPostInfo) : Boolean{
        return EditApi.instance.editPostApi(editPostInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun editCommentRepository(editCommentInfo: EditCommentInfo) : Boolean{
        return EditApi.instance.editCommentApi(editCommentInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun reportRepository(reportInfo: ReportInfo) : Boolean{
        return ReportApi.instance.reportApi(reportInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun getUploadUrls(getUploadUrlsInfo: GetUploadUrlsInfo): ArrayList<String>{
        val arrayListType = object : TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson<ArrayList<String>>(FileApi.instance.getUploadUrls(getUploadUrlsInfo).await().results.get("urls"),arrayListType)
    }

    suspend fun uploadImage(file:RequestBody,url:String){
        FileApi.instance.uploadImage(file,url).await()
    }

    suspend fun profileImageListRepository() : JsonArray {
        return SignUpApi.instance.profileImageListSignUpApi().await().results.get("profileImageList").asJsonArray
    }

    suspend fun categorySelectRepository(categorySelectInfo: CategorySelectInfo) : Boolean{
        return CategorySelectApi.instance.categorySelectApi(categorySelectInfo).await().results.get("isSuccess").asBoolean
    }

    suspend fun getNotice(noticeRequest: NoticeRequest): NoticeResponse{
        return NoticeApi.instance.getNotice(noticeRequest).await().results
    }

    suspend fun getMyProfileImageUrl(myProfileImageRequest:MyProfileImageRequest): MyProfileImageResponse{
        return MyPageApi.instance.getMyProfileImageUrl(myProfileImageRequest).await().results
    }

}