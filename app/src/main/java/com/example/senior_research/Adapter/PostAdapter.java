package com.example.senior_research.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.senior_research.Model.Post;
import com.example.senior_research.Model.User;
import com.example.senior_research.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);
        String workoutTitle = post.getTitle();
        String[] parseWorkout = post.getWorkout().split(",");
        String uploadWorkout = workoutTitle + "\n";
        for(int i=0; i<parseWorkout.length; i++){
            if(i%3==0){
                uploadWorkout+="\n";
            }
            uploadWorkout+=(parseWorkout[i] + " ");
            if(i%3==1){
                uploadWorkout+="x";
            }
        }

        holder.workout.setText(uploadWorkout);
        if(post.getNotes().equals("")){
            holder.notes.setVisibility(View.GONE);
        } else{
            holder.notes.setVisibility(View.VISIBLE);
            holder.notes.setText(post.getNotes());
        }

        publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile, like, comment, save;
        public TextView title, username, likes, publisher, notes, comments, workout;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            title = itemView.findViewById(R.id.title);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            notes = itemView.findViewById(R.id.notes);
            comments = itemView.findViewById(R.id.comments);
            workout = itemView.findViewById(R.id.post_workout);
        }
    }

    private void publisherInfo(ImageView image_profile, final TextView username, final TextView publisher, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
