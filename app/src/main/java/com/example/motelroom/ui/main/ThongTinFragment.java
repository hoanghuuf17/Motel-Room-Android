package com.example.motelroom.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motelroom.Adapter.ThongTinAdapter;
import com.example.motelroom.Constant;
import com.example.motelroom.Model.Post;
import com.example.motelroom.Model.User;
import com.example.motelroom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTinFragment extends Fragment {

    private ThongTinViewModel mViewModel;
    private View view;
    private CircleImageView circleImageViewAvatar;
    private ImageView edit_thongtin;
    private TextView txt_TenNguoiDung, txt_phone_number, txt_email;
    private ArrayList<Post> arrayList;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private ThongTinAdapter thongtinAdapter;
    private static final int GALLERY_AND_PROFILE = 1;
    public int id;

    public static ThongTinFragment newInstance() {
        return new ThongTinFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.thong_tin_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id", 0);
        recyclerView = view.findViewById(R.id.recyclerThongTin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getPosts();

        circleImageViewAvatar = view.findViewById(R.id.circle_avatar_frThongTin);
        edit_thongtin = view.findViewById(R.id.img_edit_thongtin);

        txt_email = view.findViewById(R.id.txt_email_thongtin);
        txt_email.setText(sharedPreferences.getString("email", null));

        txt_phone_number = view.findViewById(R.id.txt_phonenumber_thongtin);
        txt_phone_number.setText(sharedPreferences.getString("phone", null));

        txt_TenNguoiDung = view.findViewById(R.id.txt_infor_name);
        txt_TenNguoiDung.setText(sharedPreferences.getString("name", null));

        String avatar = sharedPreferences.getString("avatar", null);
        Picasso.get().load(Constant.URL+"uploads/avatars/"+avatar).placeholder(R.drawable.user).into(circleImageViewAvatar);

        edit_thongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment ChinhSuaHoSo = new ChinhSuaHoSoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ChinhSuaHoSo).commit();
            }
        });
    }

    private void getPosts() {
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.INFORUSER+id, response -> {
            try { JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONArray array = new JSONArray(object.getString("post"));
                    for(int i = 0; i < array.length(); i++){
                        JSONObject postObject = array.getJSONObject(i);
                        JSONObject userObject = postObject.getJSONObject("user");
                        System.out.println("ookokaod");
                        User user = new User();
                        user.setId(userObject.getInt("id"));
                        user.setName(userObject.getString("name"));
                        user.setPhoto(userObject.getString("avatar"));

                        Post post = new Post();
                        post.setId(postObject.getInt("id"));
                        post.setUser(user);
                        post.setView(postObject.getInt("count_view"));
                        post.setDate(postObject.getString("time"));
                        post.setAddr(postObject.getString("address"));
                        post.setPhoto(postObject.getString("images"));
                        post.setTitle(postObject.getString("title"));
                        post.setPrice(postObject.getString("price"));
                        arrayList.add(post);
                    }
                    thongtinAdapter = new ThongTinAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(thongtinAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
            error.printStackTrace();
        }){
            //provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token", null);
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ThongTinViewModel.class);
        // TODO: Use the ViewModel
    }


}