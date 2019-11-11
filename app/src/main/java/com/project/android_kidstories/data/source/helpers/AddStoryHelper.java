package com.project.android_kidstories.data.source.helpers;

import android.util.Log;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.story.reaction.ReactionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoryHelper {
    public static final String TOKEN_KEY = "token";
    private static final String TAG = "kidstories";

    public static SharePref sharePref;

    public static SharePref getSharePref() {
        return sharePref;
    }

    public static String token = getSharePref().getUserToken();

    private static boolean isStoryAdded = false;
    private static Integer likesCount;

//
//    public static boolean addOrUpdateStory(Story story, String imageUri, boolean isANewStory) {
//        File imageFile = new File(Uri.decode(imageUri));
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
//
//        MultipartBody.Part photo = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestFile);
//        RequestBody title = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
//        RequestBody body = RequestBody.create(okhttp3.MultipartBody.FORM, story.getBody());
////        RequestBody category = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(story.getCategoryId()));
//        RequestBody ageInrange = RequestBody.create(okhttp3.MultipartBody.FORM, story.getAge());
//        RequestBody author = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
//        RequestBody duration = RequestBody.create(okhttp3.MultipartBody.FORM, story.getStoryDuration());
//
//        MultipartBody.Part category = MultipartBody.Part.createFormData("title", String.valueOf(story.getCategoryId()));
//
//        if(isANewStory){
//            return addStory(title, body, category, photo, ageInrange, author);
//        } else {
////            return updateStory(story.getId(), title, body, category, ageInrange, author, duration, photo);
//        }
//
//    }
//
//    private static boolean addStory(RequestBody title, RequestBody  body, MultipartBody.Part category,  MultipartBody.Part photo, RequestBody ageInrange, RequestBody author) {
//        RetrofitClient.getInstance().create(Api.class).addStory(token, title, body, category, photo, ageInrange, author)
//                .enqueue(new Callback<BaseResponse<Story>>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse<Story>> call, Response<BaseResponse<Story>> response) {
//                        if (response.isSuccessful()) {
//                            Log.d(TAG, "onResponse: " + response.message());
//                            isStoryAdded=true;
//                        } else {
//                            isStoryAdded=false;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaseResponse<Story>> call, Throwable t) {
//                        isStoryAdded=false;
//                        Log.d(TAG, "onFailure: "+t.getMessage());
//                    }
//                });
//        return isStoryAdded;
//    }
//
//    private static boolean updateStory(Integer id, RequestBody title, RequestBody body, MultipartBody.Part category, MultipartBody.Part image, RequestBody ageInrange, RequestBody author, RequestBody duration) {
//        RetrofitClient.getInstance().create(Api.class).updateStory(token, id, title, body, category, ageInrange, author, duration, image)
//                .enqueue(new Callback<BaseResponse<Story>>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse<Story>> call, Response<BaseResponse<Story>> response) {
//                        if (response.isSuccessful()) {
//                            Log.d(TAG, "onResponse: " + response.message());
//                            isStoryAdded=true;
//                        } else {
//                            isStoryAdded=false;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaseResponse<Story>> call, Throwable t) {
//                        isStoryAdded=false;
//                        Log.d(TAG, "onFailure: "+t.getMessage());
//                    }
//                });
//        return isStoryAdded;
//    }

    public static Integer likeStory(int storyId) {
        RetrofitClient.getInstance().create(Api.class).likeStory(token, storyId).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.message());
                    likesCount = response.body().getLikesCount();
                } else {
                    likesCount = -1;
                }
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                likesCount = -1;
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return likesCount;
    }

    public static Integer dislikeStory(int storyId) {
        RetrofitClient.getInstance().create(Api.class).dislikeStory(token, storyId).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.message());
                    likesCount = response.body().getLikesCount();
                } else {
                    likesCount = -1;
                }
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                likesCount = -1;
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return likesCount;
    }
}
