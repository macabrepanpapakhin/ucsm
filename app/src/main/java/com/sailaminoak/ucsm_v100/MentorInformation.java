package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MentorInformation extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    LoadingDialog loadingDialog;

    FirebaseRecyclerPagingAdapter<Mentor, MentorsViewHolder> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_information);
        DisplayToast("Please Work Please!");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        loadingDialog=new LoadingDialog(MentorInformation.this);
        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        EditText search_filed=findViewById(R.id.search_field);
        search_filed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //DisplayToast("Searching");
                firebaseUserSearch(search_filed.getText().toString().trim());
            }
        });
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        //Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Mentors");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(2)
                .build();

        //Initialize FirebasePagingOptions
        DatabasePagingOptions<Mentor> options = new DatabasePagingOptions.Builder<Mentor>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Mentor.class)
                .build();
        mAdapter = new FirebaseRecyclerPagingAdapter<Mentor, MentorsViewHolder>(options) {
            @NonNull
            @Override
            public MentorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MentorsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull MentorsViewHolder holder,
                                            int position,
                                            @NonNull Mentor model) {
                holder.setDetails(getApplicationContext(),model.name,model.mark,model.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MentorInformation.this,ViewingStudentData.class);
                        intent.putExtra("t",model.getName());
                        startActivity(intent);
                    }
                });
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                        loadingDialog.startAnimationDialog();

                       // DisplayToast("initial loading");

                    case LOADING_MORE:
                        break;

                    case LOADED:
                        loadingDialog.closingAlertDialog();
                        break;

                    case FINISHED:
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                super.onError(databaseError);
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mAdapter.refresh();
            }
        });


    }
    //Start Listening Adapter
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    protected void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

    public static class MentorsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public MentorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String userName, String userStatus, String userImage){
            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            de.hdodenhof.circleimageview.CircleImageView user_image = (de.hdodenhof.circleimageview.CircleImageView) mView.findViewById(R.id.profile_image);
            if(userImage.length()<=100){
                user_image.setImageResource(R.drawable.unknown);
            }else{
                byte[] b = Base64.decode(userImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                user_image.setImageBitmap(bitmap);
            }
            user_name.setText(userName);
            user_status.setText(userStatus);
        }




    }
    public void DisplayToast(String stringToDisplay){
        Toast.makeText(MentorInformation.this,stringToDisplay,Toast.LENGTH_SHORT).show();
    }
    private void firebaseUserSearch(String searchText) {
        Query firebaseSearchQuery =  FirebaseDatabase.getInstance().getReference("Mentors").orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions<Mentor> options=new FirebaseRecyclerOptions.Builder<Mentor>().setQuery(firebaseSearchQuery,Mentor.class).build();

        FirebaseRecyclerAdapter<Mentor, MentorInformation.MentorsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Mentor, MentorsViewHolder>(options) {


            @NonNull
            @Override
            public MentorInformation.MentorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
                return  new MentorInformation.MentorsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MentorInformation.MentorsViewHolder holder, int position, @NonNull Mentor model) {
                holder.setDetails(getApplication(),model.getName(),model.mark,model.getImage());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent( MentorInformation.this,ViewingStudentData.class);
                        intent.putExtra("t",model.getName());
                        startActivity(intent);
                    }

                });
            }
        };
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        loadingDialog.closingAlertDialog();

    }
}