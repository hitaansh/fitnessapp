package com.gaur.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    FloatingActionButton uploadVideo;
    RecyclerView recyclerView;
    DatabaseReference approvalReference;
    Boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        uploadVideo = findViewById(R.id.uploadVideo);
        recyclerView = findViewById(R.id.recyclerView);
        approvalReference = FirebaseDatabase.getInstance().getReference("approvals");


        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), uploadVideo.class));
            }
        });

        recyclerView.setLayoutManager((new LinearLayoutManager(this)));

        FirebaseRecyclerOptions<fileModel> options = new FirebaseRecyclerOptions.Builder<fileModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("myVideos"), fileModel.class).build();

        FirebaseRecyclerAdapter<fileModel, viewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<fileModel, viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull fileModel model) {
                holder.prepareExoPlayer(getApplication(), model.getTitle(), model.getvURL());

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String userID = firebaseUser.getUid();
                final String postKey = getRef(position).getKey();

                holder.getApprovalStatus(postKey, userID);

                holder.approveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = true;

                        approvalReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(clicked) {
                                    if(snapshot.child(postKey).hasChild(userID)) {
                                        approvalReference.child(postKey).removeValue();
                                    } else {
                                        approvalReference.child(postKey).child(userID).setValue(true);
                                    }
                                    clicked = false;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
                return new viewHolder(view);

            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(dashboard.this, signUp.class));
    }
}