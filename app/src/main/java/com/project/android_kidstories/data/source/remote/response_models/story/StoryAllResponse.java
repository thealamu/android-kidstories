package com.project.android_kidstories.data.source.remote.response_models.story;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.data.model.Story;

import java.util.List;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbo@gmail.com
 * @created : 11/10/19
 */

public class StoryAllResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Story> stories;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setData(List<Story> stories) {
        this.stories = stories;
    }
}
