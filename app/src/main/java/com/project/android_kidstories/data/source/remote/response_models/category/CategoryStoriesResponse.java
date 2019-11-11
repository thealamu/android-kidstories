package com.project.android_kidstories.data.source.remote.response_models.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.data.model.Story;

import java.util.List;

public class CategoryStoriesResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_name")
    @Expose
    private Object imageName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("stories")
    @Expose
    private List<Story> stories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getImageName() {
        return imageName;
    }

    public void setImageName(Object imageName) {
        this.imageName = imageName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public List<Story> getStories() {
        return stories;
    }

    public List<Story> getStoriestest() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

}

