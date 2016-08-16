package com.gamik.pastify.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kutigbolahan on 27/07/2016.
 */
public class User implements Parcelable {
    String id;
    String pictureUrl;
    String email;

    public User(String id, String pictureUrl, String email) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pictureUrl);
        dest.writeString(email);
    }

    private User(Parcel in) {
        this.id = in.readString();
        this.pictureUrl = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
