package com.gaur.fitnessapp;

import android.app.Application;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class viewHolder extends RecyclerView.ViewHolder {
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView videoTitleView;
    ImageView approveButton;
    TextView approveText;
    DatabaseReference approvalReference;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        videoTitleView = itemView.findViewById(R.id.title);
        simpleExoPlayerView = itemView.findViewById(R.id.exoPlayerView);
        approveButton = itemView.findViewById(R.id.approveButton);
        approveText = itemView.findViewById(R.id.approveText);

    }

    public void prepareExoPlayer(Application application, String vTitle, String vUrl) {
        try {

            videoTitleView.setText(vTitle);

            // This code below was directly adapted from someone else

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayer =(SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application,trackSelector);

            Uri videoURI = Uri.parse(vUrl);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);

        }catch (Exception E) {
            Log.d("ExoPlayer Crashed", E.getMessage().toString());
        }
    }

    public void getApprovalStatus(final String postKey, final String userID) {
        approvalReference = FirebaseDatabase.getInstance().getReference("approvals");
        approvalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int approvalCount = (int)snapshot.child(postKey).getChildrenCount();
                approveText.setText(approvalCount + " approvals");

                if (snapshot.child(postKey).hasChild(userID)) {
                    approveButton.setImageResource(R.drawable.approve_green);
                } else {
                    approveButton.setImageResource(R.drawable.approve_black);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
