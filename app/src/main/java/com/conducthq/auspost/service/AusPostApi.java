package com.conducthq.auspost.service;

import com.conducthq.auspost.helper.Constants;
import com.conducthq.auspost.model.data.AchievementComplete;
import com.conducthq.auspost.model.data.AchievementLock;
import com.conducthq.auspost.model.data.DeviceToken;
import com.conducthq.auspost.model.data.Feedback;
import com.conducthq.auspost.model.request.RequestPin;
import com.conducthq.auspost.model.data.Principal;
import com.conducthq.auspost.model.request.RequestPost;
import com.conducthq.auspost.model.request.RequestPrincipalEvent;
import com.conducthq.auspost.model.response.ContentResponse;
import com.conducthq.auspost.model.response.FeedbackResponse;
import com.conducthq.auspost.model.response.ImageUploadResponse;
import com.conducthq.auspost.model.response.MessageResponse;
import com.conducthq.auspost.model.response.PrincipalResponse;
import com.conducthq.auspost.model.response.PrincipalUpdateResponse;
import com.conducthq.auspost.model.response.StandardResponse;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by conduct19 on 21/10/2016.
 */

public interface AusPostApi {

    @POST("auth")
    Call<PrincipalResponse> Login(@Body RequestPin body);

    @PATCH("principals/{principalId}")
    Call<Principal> UpdatePrincipal(
            @Body Principal body,
            @Path("principalId") Integer principalId,
            @Header("Authorization") String authorization
    );

    @Multipart
    @POST("image")
    Call<ImageUploadResponse> UploadImage(@Part MultipartBody.Part photo,
                                          @Header("Authorization") String authorization);

    @PATCH("principals/{principalId}/events/{eventId}")
    Call<StandardResponse> UpdatePrincipalEvent(
            @Body RequestPrincipalEvent body,
            @Path("principalId") Integer principalId,
            @Path("eventId") Integer eventId,
            @Header("Authorization") String authorization
    );

    @GET("content")
    Call<ContentResponse> GetContent(@Header("Authorization") String authorization);

    @GET("events/{eventId}/messages?count=" + Constants.MESSAGE_LIMIT)
    Call<MessageResponse> GetMessages(
            @Header("Authorization") String authorization,
            @Path("eventId") Integer eventId
    );

    @POST("events/{eventId}/messages")
    Call<StandardResponse> PostMessage(
            @Header("Authorization") String authorization,
            @Body RequestPost body,
            @Path("eventId") Integer eventId
    );

    @POST("events/{eventId}/messages/{messageId}/likes")
    Call<StandardResponse> LikeMessage(
            @Header("Authorization") String authorization,
            @Path("eventId") Integer eventId,
            @Path("messageId") Integer messageId
    );

    @DELETE("events/{eventId}/messages/{messageId}/like")
    Call<StandardResponse> UnLikeMessage(
            @Header("Authorization") String authorization,
            @Path("eventId") Integer eventId,
            @Path("messageId") Integer messageId
    );

    @POST("events/{eventId}/feedbacks")
    Call<FeedbackResponse> Feedback(
            @Header("Authorization") String authorization,
            @Path("eventId") Integer eventId,
            @Body Feedback feedback
    );

    @POST("principals/{principalId}/achievement")
    Call<StandardResponse> UnlockAchievement(
            @Header("Authorization") String authorization,
            @Path("principalId") Integer principalId,
            @Body AchievementLock lock
    );

    @PATCH("principals/{principalId}/achievement/{achievementId}")
    Call<StandardResponse> CompleteAchievement(
            @Header("Authorization") String authorization,
            @Path("principalId") Integer principalId,
            @Path("achievementId") Integer achievementId,
            @Body AchievementComplete complete
    );

    @PATCH("principals/{principalId}")
    Call<PrincipalUpdateResponse> UpdateProfile(
            @Body Principal body,
            @Path("principalId") Integer principalId,
            @Header("Authorization") String authorization
    );

    @PUT("device_token")
    Call<StandardResponse> UpdateToken(
            @Body DeviceToken body,
            @Header("Authorization") String authorization
    );
}
