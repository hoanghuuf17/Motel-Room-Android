package com.example.motelroom.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.example.motelroom.Adapter.PostsAdapter;
import com.example.motelroom.Constant;
import com.example.motelroom.Model.Post;
import com.example.motelroom.Model.User;
import com.example.motelroom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OGhepFragment extends Fragment {

    private OGhepViewModel mViewModel;
    private View view;
    private ImageView imageView, imageView_nhanguyencan, imageView_oghep, imageView_chungcu, imageView_phongtro;
    private RecyclerView recyclerView;
    private ArrayList<Post> arrayList;
    private PostsAdapter postsAdapter;
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;

    public static OGhepFragment newInstance() {
        return new OGhepFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.o_ghep_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recyclerO_Ghep);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getCategorys();

        imageView_chungcu= view.findViewById(R.id.imgview_chungcu);
        imageView_nhanguyencan= view.findViewById(R.id.imgview_nhanguyencan);
        imageView_oghep= view.findViewById(R.id.imgview_oghep);
        imageView_phongtro= view.findViewById(R.id.imgview_phongtro);

        imageView_nhanguyencan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment NhaNguyenCan = new NhaNguyenCanFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, NhaNguyenCan).commit();
            }
        });

        imageView_phongtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment PhongTro = new PhongTroFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, PhongTro).commit();
            }
        });

        imageView_chungcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment ChungCu = new ChungCuFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ChungCu).commit();
            }
        });

        imageView_oghep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment OGhep = new OGhepFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, OGhep).commit();
            }
        });
    }

    private void getCategorys() {
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.CATEGOTY+'2', response -> {
            try { JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONArray array = new JSONArray(object.getString("categorys"));
                    for(int i = 0; i < array.length(); i++){
                        JSONObject postObject = array.getJSONObject(i);
                        JSONObject userObject = postObject.getJSONObject("user");

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
                    postsAdapter = new PostsAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(postsAdapter);
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
        mViewModel = new ViewModelProvider(this).get(OGhepViewModel.class);
        // TODO: Use the ViewModel
    }

}