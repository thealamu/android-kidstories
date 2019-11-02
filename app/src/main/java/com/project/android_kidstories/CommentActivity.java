package com.project.android_kidstories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.comment.CommentResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.sharePref.SharePref;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    Api service;
    RecyclerView rv;
    EditText typeComment;
    private String token;
    private int storyId;
    private ImageView sendComment;
    private CommentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        service = RetrofitClient.getInstance().create(Api.class);
        Toolbar addStoryToolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(addStoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        rv = findViewById(R.id.comment_rv);
        typeComment = findViewById(R.id.type_comment);
        token = new SharePref(getApplicationContext()).getMyToken();
        storyId = getIntent().getIntExtra("storyId", -1);
        sendComment = findViewById(R.id.btn_send_comment);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CommentAdapter(SingleStoryActivity.returnComments(), this);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }


    public void sendComment() {
        String userComment = typeComment.getText().toString();
        if (!TextUtils.isEmpty(userComment)) {
            postComment(token, storyId, userComment);
            Toast.makeText(CommentActivity.this, "Sending Comment...", Toast.LENGTH_SHORT).show();
            sendComment.setEnabled(false);
            typeComment.setEnabled(false);
        } else {
            Toast.makeText(CommentActivity.this, "Cannot post empty comment", Toast.LENGTH_SHORT).show();
        }
    }

    public void postComment(String token, int id, String userComment) {
        // RequestBody storyId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
        RequestBody comment = RequestBody.create(okhttp3.MultipartBody.FORM, userComment);
        service.addComment("Bearer " + token, comment, id).enqueue(new Callback<BaseResponse<CommentResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CommentResponse>> call, Response<BaseResponse<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    typeComment.setText("");
                    Toast.makeText(CommentActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();
                    sendComment.setEnabled(true);
                    typeComment.setEnabled(true);
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    Log.d("Response Status ", status + ": " + message + "\n");
                    Log.d("PostedComment: ", response.body().getData().getBody() + "\n");
                    Log.d("PostedComment: ", response.body().getData().getCreatedAt() + "\n");
                } else {
                    Log.d("Success Error ", response.message());
                    Log.d("Code ", response.code() + "");
                    sendComment.setEnabled(true);
                    typeComment.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CommentResponse>> call, Throwable t) {
                Log.e("Response Error ", t.getMessage());
                Toast.makeText(CommentActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                sendComment.setEnabled(true);
                typeComment.setEnabled(true);
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
