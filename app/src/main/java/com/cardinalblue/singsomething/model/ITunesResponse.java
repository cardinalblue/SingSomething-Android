package com.cardinalblue.singsomething.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.util.List;

/**
 * Created by jimytc on 12/24/15.
 */
public class ITunesResponse {
    @Since(1.0) @SerializedName("resultCount")
    private int mResultCount;

    @Since(1.0) @SerializedName("results")
    private List<SongTrack> mTracksList;

    public boolean isEmpty() {
        return mResultCount < 1 || mTracksList.isEmpty();
    }

    public void clear() {
        mResultCount = 0;
        mTracksList.clear();
    }

    public int getResultCount() {
        return mResultCount;
    }

    public void setResultCount(int resultCount) {
        mResultCount = resultCount;
    }

    public List<SongTrack> getTracksList() {
        return mTracksList;
    }

    public void setTracksList(List<SongTrack> tracksList) {
        mTracksList = tracksList;
    }
}
