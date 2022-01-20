package com.jingyu.recipe.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName(value="id", alternate = "recipe_id")
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "ingredients")
    private String[] ingredients;

    @ColumnInfo(name = "image_url")
    @SerializedName(value="imageUrl", alternate = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "social_rank")
    @SerializedName(value="socialUrl", alternate = "social_rank")
    private float socialUrl;

    @ColumnInfo(name = "timestamp")
    private int timestamp;

    public Recipe(@NonNull String id, String title, String publisher, String[] ingredients, String imageUrl, float socialUrl, int timestamp) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.socialUrl = socialUrl;
        this.timestamp = timestamp;
    }

    public Recipe() {
    }


    protected Recipe(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        ingredients = in.createStringArray();
        id = in.readString();
        imageUrl = in.readString();
        socialUrl = in.readFloat();
        timestamp = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getSocialUrl() {
        return socialUrl;
    }

    public void setSocialUrl(float socialUrl) {
        this.socialUrl = socialUrl;
    }

    public static Creator<Recipe> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", imageUrl='" + imageUrl + '\'' +
                ", socialUrl=" + socialUrl +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(publisher);
        parcel.writeStringArray(ingredients);
        parcel.writeString(id);
        parcel.writeString(imageUrl);
        parcel.writeFloat(socialUrl);
        parcel.writeInt(timestamp);
    }
}
