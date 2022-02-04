package com.rudder.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.rudder.BuildConfig
import com.rudder.data.*
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.data.dto.ProfileImage
import com.rudder.data.local.App.Companion.prefs
import com.rudder.data.remote.LoginApi
import com.rudder.data.remote.PostApi
import com.rudder.data.remote.SignUpApi
import com.rudder.data.remote.TokenApi
import com.rudder.data.remote.*
import com.rudder.util.ExceptionUtil
import okhttp3.RequestBody
import kotlin.Exception


class Repository {

    suspend fun login(loginInfo: LoginInfo) : Boolean{
        try{
            val key = BuildConfig.TOKEN_KEY
            val result : Boolean
            val userIdKey = "userId"

            if(prefs.getValue(key) == null || prefs.getValue(key) == "" ){ // 토큰이 비어있는 상태, 로그인의 서버요청이 필요한 상태
                val resLogin =  LoginApi.instance.login(loginInfo).await()
                if (resLogin.results.success){
                    val loginToken = resLogin.results.token
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
        }catch (e: Exception){
            Log.d("Exception",e.message!!)
            e.printStackTrace()
            return false
        }

    }


    suspend fun signUpSendVerifyCode(emailInfoSignUp : EmailInfoSignUp) : String{
        val verifyAPIResult = ExceptionUtil.retryWhenException(SignUpApi::emailSignUp,emailInfoSignUp,SignUpApi())
        return verifyAPIResult.results.get("fail").asString
    }

    suspend fun signUpIdDuplicated(idDuplicatedInfo: IdDuplicatedInfo) : Boolean{
        val idDuplicatedAPIResultJson =ExceptionUtil.retryWhenException(SignUpApi::idDuplicatedSignUp,idDuplicatedInfo,SignUpApi())
        return idDuplicatedAPIResultJson.results.get("isDuplicated").asBoolean
    }

    suspend fun signUpCheckVerifyCode(checkVeriCodeInfo: CheckVerifyCodeInfo) : Boolean {
        val checkVerifyAPIResult = ExceptionUtil.retryWhenException(SignUpApi::checkVerifySignUp,checkVeriCodeInfo,SignUpApi())
        return checkVerifyAPIResult.results.get("isSuccess").asBoolean
    }

    private suspend fun checkToken(tokenInfo: TokenInfo): Boolean {
        try{
            val tokenAPIResultJson = ExceptionUtil.retryWhenException(TokenApi::tokenValidation,tokenInfo,TokenApi())
            return tokenAPIResultJson.results.get("isTokenValid").asBoolean
        }catch (e: Exception){
            Log.d("Exception",e.message!!)
            e.printStackTrace()
            return false
        }

    }


    suspend fun signUpSchoolList(): JsonArray {
        val schoolListFlagAPIResultJson = ExceptionUtil.retryWhenException(SignUpApi::schoolListSignUp,null,SignUpApi())
        return schoolListFlagAPIResultJson.results
    }


    suspend fun signUpNickNameDuplicated(nickNameDuplicatedInfo: NickNameDuplicatedInfo) : Boolean{
        val nickNameDuplicatedAPIResultJson = ExceptionUtil.retryWhenException(SignUpApi::nickNameDuplicatedSignUpApi,nickNameDuplicatedInfo,SignUpApi())
        return nickNameDuplicatedAPIResultJson.results.get("isDuplicated").asBoolean
    }


    suspend fun getPosts(getPostInfo: GetPostInfo): ArrayList<PreviewPost>{
        return ExceptionUtil.retryWhenException(PostApi::getPosts,getPostInfo,PostApi())
    }

    suspend fun getComments(getCommentInfo: GetCommentInfo): ArrayList<Comment> {

            val resJson = ExceptionUtil.retryWhenException(CommentApi::getComments,getCommentInfo,CommentApi())
            return resJson.results


    }

    suspend fun addComment(addCommentInfo: AddCommentInfo) : Boolean{

            val resJson = ExceptionUtil.retryWhenException(CommentApi::addComment,addCommentInfo,CommentApi())
            return resJson.results.get("isSuccess").asBoolean

    }

    suspend fun addPost(addPostInfo: AddPostInfo): AddPostResponse{
            val response =ExceptionUtil.retryWhenException(PostApi::addPostApi,addPostInfo,PostApi())
            return response.results
    }

    suspend fun signUpCreateAccount(signUpInsertInfo: SignUpInsertInfo) : Boolean { // Sign up, Complete!
        val createAccountAPIResult = ExceptionUtil.retryWhenException(SignUpApi::createAccountSignUp,signUpInsertInfo,SignUpApi())
        return createAccountAPIResult.results.get("signUpComplete").asBoolean
    }

    suspend fun findAccountID(emailInfo: EmailInfo) : Boolean {
        val forgotIDAPIResult = ExceptionUtil.retryWhenException(ForgotApi::findForgotID,emailInfo,ForgotApi())
        return forgotIDAPIResult.results.get("sendIdToEmail").asBoolean
    }

    suspend fun findAccountPassword(emailInfo: EmailInfo) : Boolean {
        val forgotPasswordAPIResult = ExceptionUtil.retryWhenException(ForgotApi::findForgotPassword,emailInfo,ForgotApi())
        return forgotPasswordAPIResult.results.get("sendPwVerificationCode").asBoolean
    }


    suspend fun sendAccountPassword(verifyInfo : CheckVerifyCodeInfo) : Boolean {
        val sendAccountPasswordAPIResult = ExceptionUtil.retryWhenException(ForgotApi::sendPassword,verifyInfo,ForgotApi())
        return sendAccountPasswordAPIResult.results.get("isSuccessForgot").asBoolean
    }

    suspend fun getCategories(getCategoriesRequest: GetCategoriesRequest): ArrayList<Category>{
        return ExceptionUtil.retryWhenException(BoardInfoApi::getCategoryList,getCategoriesRequest,BoardInfoApi()).results
    }

    suspend fun getClubCategories(getCategoriesRequest: GetCategoriesRequest): ArrayList<Category>{
        return ExceptionUtil.retryWhenException(BoardInfoApi::getClubCategoryList,getCategoriesRequest,BoardInfoApi()).results
    }

    suspend fun isLikePost(isLikePostInfo: IsLikePostInfo): Boolean{
        return ExceptionUtil.retryWhenException(PostApi::isLikePost,isLikePostInfo,PostApi()).results.get("isSuccess").asBoolean
    }

    suspend fun addLikePost(addLikePostInfo: AddLikePostInfo): JsonObject{
        return ExceptionUtil.retryWhenException(PostApi::addLikePost,addLikePostInfo,PostApi()).results
    }

    suspend fun addLikeComment(addLikeCommentInfo: AddLikeCommentInfo): JsonObject{
        return ExceptionUtil.retryWhenException(CommentApi::addLikeComment,addLikeCommentInfo,CommentApi()).results
    }

    suspend fun addPostViewCount(addPostViewCountInfo: AddPostViewCountInfo): Boolean{
            return ExceptionUtil.retryWhenException(PostApi::addPostViewCount,addPostViewCountInfo,PostApi()).results.get("isSuccess").asBoolean

    }

    suspend fun deletePostRepository(deletePostInfo: DeletePostInfo) : Boolean{
        return ExceptionUtil.retryWhenException(DeleteApi::deletePostApi,deletePostInfo,DeleteApi()).results.get("isSuccess").asBoolean
    }

    suspend fun deleteCommentRepository(deleteCommentInfo: DeleteCommentInfo) : Boolean{
        return ExceptionUtil.retryWhenException(DeleteApi::deletecommentApi,deleteCommentInfo,DeleteApi()).results.get("isSuccess").asBoolean
    }

    suspend fun editPostRepository(editPostInfo: EditPostInfo) : Boolean{
        return ExceptionUtil.retryWhenException(EditApi::editPostApi,editPostInfo,EditApi()).results.get("isSuccess").asBoolean
    }

    suspend fun editCommentRepository(editCommentInfo: EditCommentInfo) : Boolean{
        return ExceptionUtil.retryWhenException(EditApi::editCommentApi,editCommentInfo,EditApi()).results.get("isSuccess").asBoolean
    }

    suspend fun reportRepository(reportInfo: ReportInfo) : Boolean{
        return ExceptionUtil.retryWhenException(ReportApi::reportApi,reportInfo,ReportApi()).results.get("isSuccess").asBoolean
    }

    suspend fun getUploadUrls(getUploadUrlsInfo: GetUploadUrlsInfo): ArrayList<String>{
            val arrayListType = object : TypeToken<ArrayList<String>>(){}.type
            return Gson().fromJson<ArrayList<String>>(ExceptionUtil.retryWhenException(FileApi::getUploadUrls,getUploadUrlsInfo,FileApi()).results.get("urls"),arrayListType)

    }

    //예외처리 예정
    suspend fun uploadImage(file:RequestBody,url:String){

            FileApi.instance.uploadImage(file,url).await()


    }

    suspend fun profileImageListRepository() : JsonArray {
        return ExceptionUtil.retryWhenException(SignUpApi::profileImageListSignUpApi,null,SignUpApi()).results.get("profileImageList").asJsonArray
    }

    suspend fun categorySelectSignUpRepository(categorySelectSignUpInfo: CategorySelectSignUpInfo) : Boolean{
        return ExceptionUtil.retryWhenException(CategorySelectApi::categorySelectSignUpApi,categorySelectSignUpInfo,CategorySelectApi()).results.get("isSuccess").asBoolean
    }

    suspend fun categorySelectMyPageRepository(categorySelectMyPageInfo: CategorySelectMyPageInfo) : Boolean{
        return ExceptionUtil.retryWhenException(CategorySelectApi::categorySelectMyPageApi,categorySelectMyPageInfo,CategorySelectApi()).results.get("isSuccess").asBoolean
    }


    suspend fun getSelectedCategoriesRepository(token : Token): ArrayList<Category> {
        return ExceptionUtil.retryWhenException(BoardInfoApi::getSelectedCategoryListApi,token,BoardInfoApi()).results
    }


    suspend fun getNotice(noticeRequest: NoticeRequest): NoticeResponse{

            return ExceptionUtil.retryWhenException(NoticeApi::getNotice,noticeRequest,NoticeApi()).results


    }

    suspend fun getMyProfileImageUrl(myProfileImageRequest:MyProfileImageRequest): MyProfileImageResponse{

        return ExceptionUtil.retryWhenException(MyPageApi::getMyProfileImageUrl,myProfileImageRequest,MyPageApi()).results
    }

    suspend fun addUserRequest(addUserRequestRequest: AddUserRequestRequest): Boolean{

            return ExceptionUtil.retryWhenException(MyPageApi::addUserRequest,addUserRequestRequest,MyPageApi()).results.get("isSuccess").asBoolean


    }

    suspend fun requestJoinClub(requestJoinClubRequest: RequestJoinClubRequest): Boolean{

            return ExceptionUtil.retryWhenException(MyPageApi::requestJoinClub,requestJoinClubRequest,MyPageApi()).results.isSuccess


    }

    suspend fun blockUser(blockUserRequest: BlockUserRequest): Boolean{
        return ExceptionUtil.retryWhenException(BlockUserApi::blockUser,blockUserRequest,BlockUserApi()).results.isSuccess

    }

    suspend fun updateNickname(updateNicknameRequest: UpdateNicknameRequest): UpdateResponse{
        return ExceptionUtil.retryWhenException(EditUserApi::updateNickname,updateNicknameRequest,EditUserApi()).results

    }

    suspend fun getProfileImages() : ArrayList<ProfileImage> {
        return ExceptionUtil.retryWhenException(MyPageApi::getProfileImages,null,MyPageApi()).results.profileImageList


    }

    suspend fun updateProfileImage(updateProfileImageRequest: UpdateProfileImageRequest): UpdateResponse{
        return ExceptionUtil.retryWhenException(EditUserApi::updateProfileImage,updateProfileImageRequest,EditUserApi()).results


    }

    suspend fun sendPostMessage(sendPostMessageRequest: SendPostMessageRequest): SendPostMessageResponse{
        return ExceptionUtil.retryWhenException(MessageApi::sendPostMessage,sendPostMessageRequest,MessageApi()).results


    }

    suspend fun getMessagesByRoom(getMessagesByRoomRequest: GetMessagesByRoomRequest): ArrayList<PostMessage>{
        return ExceptionUtil.retryWhenException(MessageApi::getMessagesByRoom,getMessagesByRoomRequest,MessageApi()).results.messages


    }

    suspend fun getMyMessages(getMyMessageRoomsRequest: GetMyMessageRoomsRequest): ArrayList<PostMessageRoom> {

            val getMyMessagesResponse = ExceptionUtil.retryWhenException(PostMessageApi::getMyMessageRooms,getMyMessageRoomsRequest,PostMessageApi()).results
            return if (getMyMessagesResponse.isSuccess) {
                getMyMessagesResponse.rooms
            } else{
                arrayListOf()
            }

    }
}