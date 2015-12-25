package com.cardinalblue.singsomething.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * Created by jimytc on 12/24/15.
 */
public class SongTrack {

    @Since(1.0) @SerializedName("artistName")
    private String mArtistName;

    @Since(1.0) @SerializedName("collectionName")
    private String mCollectionName; // Album

    @Since(1.0) @SerializedName("trackName")
    private String mTrackName;

    @Since(1.0) @SerializedName("previewUrl")
    private String mPreviewUrl;

    @Override
    public SongTrack clone() {
        SongTrack clone = new SongTrack();
        clone.setArtistName(getArtistName());
        clone.setCollectionName(getCollectionName());
        clone.setTrackName(getTrackName());
        clone.setPreviewUrl(getPreviewUrl());
        return clone;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getCollectionName() {
        return mCollectionName;
    }

    public void setCollectionName(String collectionName) {
        mCollectionName = collectionName;
    }

    public String getTrackName() {
        return mTrackName;
    }

    public void setTrackName(String trackName) {
        mTrackName = trackName;
    }

    public String getPreviewUrl() {
        return mPreviewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        mPreviewUrl = previewUrl;
    }
}
