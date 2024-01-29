package id.creatodidak.vrspolreslandak.dashboard.humas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Ldkserver;
import id.creatodidak.vrspolreslandak.dashboard.humas.adapter.NewsAdapter;
import id.creatodidak.vrspolreslandak.dashboard.humas.adapter.NewsAdapter;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.DataItem;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.ResItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Humas extends AppCompatActivity {

    NewsAdapter adp;
    LinearLayoutManager lm;
    News endpoint;
    List<ResItem> data = new ArrayList<>();
    RecyclerView rv;
    SwipeRefreshLayout sw;

    CardView btPublishMenu, btMyNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humas);
        endpoint = Ldkserver.getClient().create(News.class);
        adp = new NewsAdapter(this, data);
        rv = findViewById(R.id.rvBerita);
        lm = new LinearLayoutManager(this);
        sw = findViewById(R.id.swBerita);

        rv.setDrawingCacheEnabled(true);
        rv.setItemViewCacheSize(20);

        rv.setAdapter(adp);
        rv.setLayoutManager(lm);

        btPublishMenu = findViewById(R.id.btPublishMenu);
        btMyNews = findViewById(R.id.btMyNews);

        btPublishMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Humas.this, PublishBerita.class);
                startActivity(i);
            }
        });

        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                adp.notifyDataSetChanged();
                loadData();
            }
        });

    }

    private void loadData() {
        sw.setRefreshing(true);
        String page;
        if(data.isEmpty()){
            page = null;
        }else{
            page = String.valueOf((data.size()/10)+1);
        }

        Call<id.creatodidak.vrspolreslandak.dashboard.humas.model.News> call = endpoint.allnews();
        call.enqueue(new Callback<id.creatodidak.vrspolreslandak.dashboard.humas.model.News>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<id.creatodidak.vrspolreslandak.dashboard.humas.model.News> call, Response<id.creatodidak.vrspolreslandak.dashboard.humas.model.News> response) {
                sw.setRefreshing(false);
                if(response.isSuccessful() && response.body() != null && response.body().getRes() != null){
                    data.addAll(response.body().getRes());
                    if(data.isEmpty()){
                        adp.notifyDataSetChanged();
                    }else{
                        adp.notifyItemInserted(data.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<id.creatodidak.vrspolreslandak.dashboard.humas.model.News> call, Throwable t) {
                sw.setRefreshing(false);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }
}