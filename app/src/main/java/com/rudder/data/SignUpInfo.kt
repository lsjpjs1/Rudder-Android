package com.rudder.data

import com.google.gson.annotations.SerializedName


data class EmailInfoSignUp(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "school_id")
    val schoolId: Int
)


data class EmailInfo(
    @SerializedName(value = "email")
    val email: String
)

data class NickNameDuplicatedInfo(
    @SerializedName(value = "nickname")
    val userNickName: String
)



data class IdDuplicatedInfo(
    @SerializedName(value = "user_id")
    val user_id: String
)


data class CheckVerifyCodeInfo(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "verifyCode")
    val verifyCode : String
)


data class AccountInfo(
    @SerializedName(value = "user_id")
    val userId: String,
    @SerializedName(value = "user_password")
    val userPassword: String

)


data class SignUpInsertInfo(
    @SerializedName(value = "user_id")
    val userId: String,

    @SerializedName(value = "user_password")
    val userPassword: String,

    @SerializedName(value = "email")
    val userEmail: String,

    @SerializedName(value = "recommendationCode")
    val userRecommendCode: String,

    @SerializedName(value = "school_id")
    val userSchoolInt: Int,

    @SerializedName(value = "profile_body") // dummy
    val userIntroduce: String,

    @SerializedName(value = "user_nickname")
    val userNickName: String,

    @SerializedName(value = "user_profile_image_id") // profile Image ID
    val userProfileImageId: Int

)


data class SignUpInfo(
    val userEmail: String,
    val userPassword: String

)