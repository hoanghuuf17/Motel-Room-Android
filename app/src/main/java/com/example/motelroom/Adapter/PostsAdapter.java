package com.example.motelroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motelroom.Constant;
import com.example.motelroom.DetailPost;
import com.example.motelroom.Model.Post;
import com.example.motelroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder>{

    private Context context;
    private ArrayList<Post> list;

    public PostsAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Post post = list.get(position);
        Picasso.get().load(Constant.URL+"uploads/avatars/"+post.getUser().getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"uploads/images/"+post.getPhoto()).into(holder.imgPost);
        holder.txtName.setText(post.getUser().getName());
        holder.txtDate.setText(post.getDate());
        holder.txtTitle.setText(post.getTitle());
        holder.txtPrice.setText(post.getPrice());
        holder.txtAddr.setText(post.getAddr());
        holder.txtView.setText(post.getView()+"views");

        holder.imgPost.setOnClickListener(v->{
            Intent intent = new Intent().setClass(v.getContext(), DetailPost.class);
            intent.putExtra("postId", post.getId());
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class PostsHolder extends RecyclerView.ViewHolder{

        private TextView txtName, txtDate, txtTitle, txtPrice, txtAddr, txtView;
        private CircleImageView imgProfile;
        private ImageView imgPost;


        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPostName);
            txtDate = itemView.findViewById(R.id.txtPostDate);
            txtTitle = itemView.findViewById(R.id.txtPostTittle);
            txtPrice = itemView.findViewById(R.id.txtPostPrice);
            txtAddr = itemView.findViewById(R.id.txtPostAddr);
            txtView = itemView.findViewById(R.id.txtPostView);
            imgProfile = itemView.findViewById(R.id.imgPostProfile);
            imgPost = itemView.findViewById(R.id.imgPostPhoto);
        }
    }
}
