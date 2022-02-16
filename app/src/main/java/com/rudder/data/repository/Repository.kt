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
import com.rudder.data.dto.ProfileImageResponse
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
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("fail","Server error")
        val verifyAPIResult = ExceptionUtil.retryWhenException(SignUpApi::emailSignUp,emailInfoSignUp,SignUpApi(),Response(serverFailJsonObject))
        return verifyAPIResult.results.get("fail").asString
    }

    suspend fun signUpIdDuplicated(idDuplicatedInfo: IdDuplicatedInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isDuplicated", true)
        val idDuplicatedAPIResultJson =ExceptionUtil.retryWhenException(SignUpApi::idDuplicatedSignUp,idDuplicatedInfo,SignUpApi(),Response(serverFailJsonObject))
        return idDuplicatedAPIResultJson.results.get("isDuplicated").asBoolean
    }

    suspend fun signUpCheckVerifyCode(checkVeriCodeInfo: CheckVerifyCodeInfo) : Boolean {
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess", false)
        val checkVerifyAPIResult = ExceptionUtil.retryWhenException(SignUpApi::checkVerifySignUp,checkVeriCodeInfo,SignUpApi(),Response(serverFailJsonObject))
        return checkVerifyAPIResult.results.get("isSuccess").asBoolean
    }

    private suspend fun checkToken(tokenInfo: TokenInfo): Boolean {
        try{
            val serverFailJsonObject = JsonObject()
            serverFailJsonObject.addProperty("isTokenValid", false)
            val tokenAPIResultJson = ExceptionUtil.retryWhenException(TokenApi::tokenValidation,tokenInfo,TokenApi(),Response(serverFailJsonObject))
            return tokenAPIResultJson.results.get("isTokenValid").asBoolean
        }catch (e: Exception){
            Log.d("Exception",e.message!!)
            e.printStackTrace()
            return false
        }

    }


    suspend fun signUpSchoolList(): JsonArray {
        val jsonArray = JsonArray()
        val schoolListFlagAPIResultJson = ExceptionUtil.retryWhenException(SignUpApi::schoolListSignUp,null,SignUpApi(),
            Response(jsonArray)
        )
        return schoolListFlagAPIResultJson.results
    }


    suspend fun signUpNickNameDuplicated(nickNameDuplicatedInfo: NickNameDuplicatedInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isDuplicated", true)
        val nickNameDuplicatedAPIResultJson = ExceptionUtil.retryWhenException(SignUpApi::nickNameDuplicatedSignUpApi,nickNameDuplicatedInfo,SignUpApi(),Response(serverFailJsonObject))
        return nickNameDuplicatedAPIResultJson.results.get("isDuplicated").asBoolean
    }


    suspend fun getPosts(getPostInfo: GetPostInfo): ArrayList<PreviewPost>{
        return ExceptionUtil.retryWhenException(PostApi::getPosts,getPostInfo,PostApi(), arrayListOf())
    }

    suspend fun getComments(getCommentInfo: GetCommentInfo): ArrayList<Comment> {

            val resJson = ExceptionUtil.retryWhenException(CommentApi::getComments,getCommentInfo,CommentApi(),
                Response(arrayListOf())
            )
            return resJson.results


    }

    suspend fun addComment(addCommentInfo: AddCommentInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess", false)
        val resJson = ExceptionUtil.retryWhenException(CommentApi::addComment,addCommentInfo,CommentApi(),Response(serverFailJsonObject))
        return resJson.results.get("isSuccess").asBoolean

    }

    suspend fun addPost(addPostInfo: AddPostInfo): AddPostResponse{
            val response =ExceptionUtil.retryWhenException(PostApi::addPostApi,addPostInfo,PostApi(),
                Response(AddPostResponse(false,-1))
            )
            return response.results
    }

    suspend fun signUpCreateAccount(signUpInsertInfo: SignUpInsertInfo) : Boolean { // Sign up, Complete!
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("signUpComplete", false)
        val createAccountAPIResult = ExceptionUtil.retryWhenException(SignUpApi::createAccountSignUp,signUpInsertInfo,SignUpApi(),Response(serverFailJsonObject))
        return createAccountAPIResult.results.get("signUpComplete").asBoolean
    }

    suspend fun findAccountID(emailInfo: EmailInfo) : Boolean {
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("sendIdToEmail", false)
        val forgotIDAPIResult = ExceptionUtil.retryWhenException(ForgotApi::findForgotID,emailInfo,ForgotApi(),Response(serverFailJsonObject))
        return forgotIDAPIResult.results.get("sendIdToEmail").asBoolean
    }

    suspend fun findAccountPassword(emailInfo: EmailInfo) : Boolean {
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("sendPwVerificationCode", false)
        val forgotPasswordAPIResult = ExceptionUtil.retryWhenException(ForgotApi::findForgotPassword,emailInfo,ForgotApi(),Response(serverFailJsonObject))
        return forgotPasswordAPIResult.results.get("sendPwVerificationCode").asBoolean
    }


    suspend fun sendAccountPassword(verifyInfo : CheckVerifyCodeInfo) : Boolean {
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccessForgot", false)
        val sendAccountPasswordAPIResult = ExceptionUtil.retryWhenException(ForgotApi::sendPassword,verifyInfo,ForgotApi(),Response(serverFailJsonObject))
        return sendAccountPasswordAPIResult.results.get("isSuccessForgot").asBoolean
    }

    suspend fun getCategories(getCategoriesRequest: GetCategoriesRequest): ArrayList<Category>{
        return ExceptionUtil.retryWhenException(BoardInfoApi::getCategoryList,getCategoriesRequest,BoardInfoApi(),
            Response(arrayListOf())
        ).results
    }

    suspend fun getClubCategories(getCategoriesRequest: GetCategoriesRequest): ArrayList<Category>{
        return ExceptionUtil.retryWhenException(BoardInfoApi::getClubCategoryList,getCategoriesRequest,BoardInfoApi(),
            Response(arrayListOf())
        ).results
    }

    suspend fun isLikePost(isLikePostInfo: IsLikePostInfo): Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess", false)
        return ExceptionUtil.retryWhenException(PostApi::isLikePost,isLikePostInfo,PostApi(),Response(serverFailJsonObject)).results.get("isSuccess").asBoolean
    }

    suspend fun addLikePost(addLikePostInfo: AddLikePostInfo): JsonObject{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess", false)
        return ExceptionUtil.retryWhenException(PostApi::addLikePost,addLikePostInfo,PostApi(),
            Response(serverFailJsonObject)
        ).results
    }

    suspend fun addLikeComment(addLikeCommentInfo: AddLikeCommentInfo): JsonObject{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess", false)
        return ExceptionUtil.retryWhenException(CommentApi::addLikeComment,addLikeCommentInfo,CommentApi(),Response(serverFailJsonObject)).results
    }

    suspend fun addPostViewCount(addPostViewCountInfo: AddPostViewCountInfo): Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
            return ExceptionUtil.retryWhenException(PostApi::addPostViewCount,addPostViewCountInfo,PostApi(),Response(serverFailJsonObject)).results.get("isSuccess").asBoolean

    }

    suspend fun deletePostRepository(deletePostInfo: DeletePostInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(DeleteApi::deletePostApi,deletePostInfo,DeleteApi(),
            Response(serverFailJsonObject)
        ).results.get("isSuccess").asBoolean
    }

    suspend fun deleteCommentRepository(deleteCommentInfo: DeleteCommentInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(DeleteApi::deletecommentApi,deleteCommentInfo,DeleteApi(),
            Response(serverFailJsonObject)
        ).results.get("isSuccess").asBoolean
    }

    suspend fun editPostRepository(editPostInfo: EditPostInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(EditApi::editPostApi,editPostInfo,EditApi(),
            Response(serverFailJsonObject)
        ).results.get("isSuccess").asBoolean
    }

    suspend fun editCommentRepository(editCommentInfo: EditCommentInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(EditApi::editCommentApi,editCommentInfo,EditApi(),
            Response(serverFailJsonObject)
        ).results.get("isSuccess").asBoolean
    }

    suspend fun reportRepository(reportInfo: ReportInfo) : Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(ReportApi::reportApi,reportInfo,ReportApi(),
            Response(serverFailJsonObject)
        ).results.get("isSuccess").asBoolean
    }

    suspend fun getUploadUrls(getUploadUrlsInfo: GetUploadUrlsInfo): ArrayList<String>{
        val serverErrorJsonObject = JsonObject()
        serverErrorJsonObject.addProperty("urls","")
        val arrayListType = object : TypeToken<ArrayList<String>>(){}.type
            return Gson().fromJson<ArrayList<String>>(ExceptionUtil.retryWhenException(FileApi::getUploadUrls,getUploadUrlsInfo,FileApi(),Response(serverErrorJsonObject)).results.get("urls"),arrayListType)

    }

    //예외처리 예정
    suspend fun uploadImage(file:RequestBody,url:String){

            FileApi.instance.uploadImage(file,url).await()


    }

    suspend fun profileImageListRepository() : JsonArray {
        val serverErrorJsonObject = JsonObject()
        serverErrorJsonObject.add("profileImageList",JsonArray())
        return ExceptionUtil.retryWhenException(SignUpApi::profileImageListSignUpApi,null,SignUpApi(),
            Response(serverErrorJsonObject)
        ).results.get("profileImageList").asJsonArray
    }

    suspend fun categorySelectSignUpRepository(categorySelectSignUpInfo: CategorySelectSignUpInfo) : Boolean{
        val serverErrorJsonObject = JsonObject()
        serverErrorJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(CategorySelectApi::categorySelectSignUpApi,categorySelectSignUpInfo,CategorySelectApi(),
            Response(serverErrorJsonObject)
        ).results.get("isSuccess").asBoolean
    }

    suspend fun categorySelectMyPageRepository(categorySelectMyPageInfo: CategorySelectMyPageInfo) : Boolean{
        val serverErrorJsonObject = JsonObject()
        serverErrorJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(CategorySelectApi::categorySelectMyPageApi,categorySelectMyPageInfo,CategorySelectApi(),
            Response(serverErrorJsonObject)
        ).results.get("isSuccess").asBoolean
    }


    suspend fun getSelectedCategoriesRepository(token : Token): ArrayList<Category> {
        return ExceptionUtil.retryWhenException(BoardInfoApi::getSelectedCategoryListApi,token,BoardInfoApi(),
            Response(arrayListOf())
        ).results
    }


    suspend fun getNotice(noticeRequest: NoticeRequest): NoticeResponse{

            return ExceptionUtil.retryWhenException(NoticeApi::getNotice,noticeRequest,NoticeApi(),
                Response(NoticeResponse(false,""))
            ).results


    }

    suspend fun getMyProfileImageUrl(myProfileImageRequest:MyProfileImageRequest): MyProfileImageResponse{

        return ExceptionUtil.retryWhenException(MyPageApi::getMyProfileImageUrl,myProfileImageRequest,MyPageApi(),
            Response(MyProfileImageResponse(""))
        ).results
    }

    suspend fun addUserRequest(addUserRequestRequest: AddUserRequestRequest): Boolean{
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("isSuccess",false)
        return ExceptionUtil.retryWhenException(MyPageApi::addUserRequest,addUserRequestRequest,MyPageApi(),
            Response(serverFailJsonObject)
        ).results.get("isSuccess").asBoolean


    }

    suspend fun requestJoinClub(requestJoinClubRequest: RequestJoinClubRequest): Boolean{

            return ExceptionUtil.retryWhenException(MyPageApi::requestJoinClub,requestJoinClubRequest,MyPageApi(),
                Response(RequestJoinClubResponse(false))
            ).results.isSuccess


    }

    suspend fun blockUser(blockUserRequest: BlockUserRequest): Boolean{
        return ExceptionUtil.retryWhenException(BlockUserApi::blockUser,blockUserRequest,BlockUserApi(),
            Response(BlockUserResponse(false))
        ).results.isSuccess

    }

    suspend fun updateNickname(updateNicknameRequest: UpdateNicknameRequest): UpdateResponse{
        return ExceptionUtil.retryWhenException(EditUserApi::updateNickname,updateNicknameRequest,EditUserApi(),
            Response(UpdateResponse(false,ResponseEnum.UNKNOWN))
        ).results

    }

    suspend fun getProfileImages() : ArrayList<ProfileImage> {
        return ExceptionUtil.retryWhenException(MyPageApi::getProfileImages,null,MyPageApi(),
            Response(ProfileImageResponse(arrayListOf()))
        ).results.profileImageList


    }

    suspend fun updateProfileImage(updateProfileImageRequest: UpdateProfileImageRequest): UpdateResponse{
        return ExceptionUtil.retryWhenException(EditUserApi::updateProfileImage,updateProfileImageRequest,EditUserApi(),
            Response(UpdateResponse(false,ResponseEnum.UNKNOWN))
        ).results


    }

    suspend fun sendPostMessage(sendPostMessageRequest: SendPostMessageRequest): SendPostMessageResponse{
        return ExceptionUtil.retryWhenException(MessageApi::sendPostMessage,sendPostMessageRequest,MessageApi(),
            Response(SendPostMessageResponse(false,ResponseEnum.UNKNOWN))
        ).results


    }

    suspend fun getMessagesByRoom(getMessagesByRoomRequest: GetMessagesByRoomRequest): ArrayList<PostMessage>{
        return ExceptionUtil.retryWhenException(MessageApi::getMessagesByRoom,getMessagesByRoomRequest,MessageApi(),
            Response(GetMessagesByRoomResponse(false,"Server error", arrayListOf()))
        ).results.messages


    }

    suspend fun getMyMessages(getMyMessageRoomsRequest: GetMyMessageRoomsRequest): ArrayList<PostMessageRoom> {

            val getMyMessagesResponse = ExceptionUtil.retryWhenException(PostMessageApi::getMyMessageRooms,getMyMessageRoomsRequest,PostMessageApi(),
                Response(GetMyMessageRoomsResponse(false,"Server error", arrayListOf()))
            ).results
            return if (getMyMessagesResponse.isSuccess) {
                getMyMessagesResponse.rooms
            } else{
                arrayListOf()
            }

    }
}