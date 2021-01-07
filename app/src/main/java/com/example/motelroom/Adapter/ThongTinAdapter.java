package com.example.motelroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motelroom.Constant;
import com.example.motelroom.Model.Post;
import com.example.motelroom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTinAdapter extends RecyclerView.Adapter<ThongTinAdapter.ThongTinHolder>{

    private Context context;
    private ArrayList<Post> list;
    private EditText txt_username, txt_email, txt_phonenumber;
    private CircleImageView circleImageView;
    private View viewl;


    public ThongTinAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ThongTinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_thongtin, parent, false);
        return new ThongTinHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongTinHolder holder, int position) {
        Post post = list.get(position);
        Picasso.get().load(Constant.URL+"uploads/avatars/"+post.getUser().getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"uploads/images/"+post.getPhoto()).into(holder.imgPost);
        holder.txtName.setText(post.getUser().getName());
        holder.txtDate.setText(post.getDate());
        holder.txtTitle.setText(post.getTitle());
        holder.txtPrice.setText(post.getPrice());
        holder.txtAddr.setText(post.getAddr());
        holder.txtView.setText(post.getView()+"views");

        holder.txtDelete.setOnClickListener(v->{
            StringRequest request = new StringRequest(Request.Method.GET, Constant.DELETE+post.getId(), response -> {
                try { JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")){
                        System.out.println("ok");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },error -> {
                error.printStackTrace();
            }){
            };
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ThongTinHolder extends RecyclerView.ViewHolder{

        private TextView txtName, txtDate, txtTitle, txtPrice, txtAddr, txtView, txtDelete;
        private CircleImageView imgProfile;
        private ImageView imgPost;


        public ThongTinHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_UserName);
            txtDate = itemView.findViewById(R.id.txt_Post_Date);
            txtTitle = itemView.findViewById(R.id.txt_Tittle);
            txtPrice = itemView.findViewById(R.id.txt_Price);
            txtAddr = itemView.findViewById(R.id.txt_Address);
            txtView = itemView.findViewById(R.id.txt_Post_View);
            imgProfile = itemView.findViewById(R.id.img_Post_Profile);
            imgPost = itemView.findViewById(R.id.img_Post_Photo);
            txtDelete = itemView.findViewById(R.id.txt_delete_post);


        }
    }
}
