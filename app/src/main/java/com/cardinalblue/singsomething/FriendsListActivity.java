package com.cardinalblue.singsomething;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cardinalblue.singsomething.adapter.FriendsListAdapter;

public class FriendsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FriendsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.friend_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FriendsListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
