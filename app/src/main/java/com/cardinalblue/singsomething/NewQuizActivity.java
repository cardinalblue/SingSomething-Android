package com.cardinalblue.singsomething;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cardinalblue.singsomething.model.ITunesResponse;
import com.cardinalblue.singsomething.model.SongTrack;
import com.cardinalblue.singsomething.network.RequestManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewQuizActivity extends AppCompatActivity {
    private static final String LOG_TAG = NewQuizActivity.class.getSimpleName();

    private static final List<String> ITUNES_SEARCH_TERMS =
            Arrays.asList(new String[] {
                    "韋禮安", "Maroon 5", "Katy Perry", "Aerosmith",
                    "張震嶽", "徐佳瑩"
            });

    @Bind(R.id.song_opt_1) View mTrackOpt1;
    @Bind(R.id.song_opt_2) View mTrackOpt2;
    @Bind(R.id.song_opt_3) View mTrackOpt3;

    private TextView mTvTrackOpt1Artist;
    private TextView mTvTrackOpt2Artist;
    private TextView mTvTrackOpt3Artist;
    private TextView mTvTrackOpt1Title;
    private TextView mTvTrackOpt2Title;
    private TextView mTvTrackOpt3Title;
    private ImageButton mBtnTrackOpt1Playback;
    private ImageButton mBtnTrackOpt2Playback;
    private ImageButton mBtnTrackOpt3Playback;

    private final Handler mHandler = new MessageHandler();

    private final ArrayList<SongTrack> mTrackOptions = new ArrayList<>();

    private MediaPlayer mPlayer = null;
    private boolean mIsPlaying = false;
    private String mCurrnetPlaying = "";

    private final Object mLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);
        initOptionViews();
        fetchSongList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @OnClick({ R.id.song_opt_1, R.id.song_opt_2, R.id.song_opt_3 })
    public void pickQuiz(View selected) {
        switch (selected.getId()) {
            case R.id.song_opt_1:
                break;
            case R.id.song_opt_2:
                break;
            case R.id.song_opt_3:
                break;
        }
    }

    //--------------------------------------------------------------------------
    private void initOptionViews() {
        ButterKnife.bind(this);

        mTvTrackOpt1Artist = ButterKnife.findById(mTrackOpt1, R.id.artist_name);
        mTvTrackOpt2Artist = ButterKnife.findById(mTrackOpt2, R.id.artist_name);
        mTvTrackOpt3Artist = ButterKnife.findById(mTrackOpt3, R.id.artist_name);

        mTvTrackOpt1Title = ButterKnife.findById(mTrackOpt1, R.id.track_name);
        mTvTrackOpt2Title = ButterKnife.findById(mTrackOpt2, R.id.track_name);
        mTvTrackOpt3Title = ButterKnife.findById(mTrackOpt3, R.id.track_name);

        mBtnTrackOpt1Playback = ButterKnife.findById(mTrackOpt1, R.id.playback);
        mBtnTrackOpt2Playback = ButterKnife.findById(mTrackOpt2, R.id.playback);
        mBtnTrackOpt3Playback = ButterKnife.findById(mTrackOpt3, R.id.playback);
        mBtnTrackOpt1Playback.setTag(mTrackOpt1.getId());
        mBtnTrackOpt2Playback.setTag(mTrackOpt2.getId());
        mBtnTrackOpt3Playback.setTag(mTrackOpt3.getId());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = -1;
                switch ( (Integer) v.getTag() ) {
                    case R.id.song_opt_1:
                        i = 0;
                        break;
                    case R.id.song_opt_2:
                        i = 1;
                        break;
                    case R.id.song_opt_3:
                        i = 2;
                        break;
                }
                if (i < 0) return;
                String url = mTrackOptions.get(i).getPreviewUrl();
                synchronized (mLock) {
                    boolean start = !mIsPlaying && !url.equalsIgnoreCase(mCurrnetPlaying);
                    onPlay(url, start);
                }
            }
        };
        mBtnTrackOpt1Playback.setOnClickListener(clickListener);
        mBtnTrackOpt2Playback.setOnClickListener(clickListener);
        mBtnTrackOpt3Playback.setOnClickListener(clickListener);
    }

    private void fetchSongList() {
        try {
            String term = URLEncoder.encode(
                    (String) pickNRandom(ITUNES_SEARCH_TERMS, 1).get(0),
                    "UTF-8");
            final String iTunesUrl = String.format(
                    "https://itunes.apple.com/search?media=music&limit=24&term=%s",
                    term);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    iTunesUrl,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            try {
                                Message msg =
                                        mHandler.obtainMessage(MessageHandler.MSG_UPDATE_SONGS);
                                Gson gson = new GsonBuilder().create();
                                ITunesResponse iTunesResponse =
                                        gson.fromJson(object.toString(), ITunesResponse.class);
                                msg.obj = iTunesResponse;
                                mHandler.sendMessage(msg);
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "Cannot get response");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            RequestManager.getInstance(this).addToRequestQueue(jsonRequest);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error on encoding url");
        }
    }

    private void updateTrackOptions(ITunesResponse response) {
        if (response == null) return;
        int size = 3;
        if (size > response.getResultCount()) {
            size = response.getResultCount();
        }
        mTrackOptions.clear();
        List<SongTrack> random = (List<SongTrack>) pickNRandom(response.getTracksList(), size);
        mTrackOptions.addAll(random);
    }

    private void updateTrackUI() {
        SongTrack track1 = mTrackOptions.get(0);
        SongTrack track2 = mTrackOptions.get(1);
        SongTrack track3 = mTrackOptions.get(2);

        mTvTrackOpt1Artist.setText(track1.getArtistName());
        mTvTrackOpt1Title.setText(track1.getTrackName());

        mTvTrackOpt2Artist.setText(track2.getArtistName());
        mTvTrackOpt2Title.setText(track2.getTrackName());

        mTvTrackOpt3Artist.setText(track3.getArtistName());
        mTvTrackOpt3Title.setText(track3.getTrackName());
    }

    @NonNull
    private static List<?> pickNRandom(List<?> lst, int n) {
        List<?> copy = new ArrayList<>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }

    private void onPlay(String url, boolean start) {
        if (start) {
            startPlaying(url);
        } else {
            mCurrnetPlaying = "";
            stopPlaying();
        }
    }

    private void startPlaying(String url) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(url);
            mPlayer.setVolume(100, 100);
            mPlayer.prepare();
            mPlayer.start();
            mIsPlaying = true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        mIsPlaying = false;
    }

    //--------------------------------------------------------------------------
    class MessageHandler extends Handler {
        public static final int MSG_UPDATE_SONGS = 0;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_SONGS:
                    updateTrackOptions((ITunesResponse) msg.obj);
                    updateTrackUI();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
