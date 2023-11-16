package id.creatodidak.vrspolreslandak.dashboard.humas;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Ldkserver;
import id.creatodidak.vrspolreslandak.dashboard.humas.adapter.BeritaAdapter;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.DataItem;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.Mberita;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Humas extends AppCompatActivity {

    BeritaAdapter adp;
    LinearLayoutManager lm;
    Berita endpoint;
    List<DataItem> data = new ArrayList<>();
    RecyclerView rv;
    SwipeRefreshLayout sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humas);
        endpoint = Ldkserver.getClient().create(Berita.class);
        adp = new BeritaAdapter(this, data);
        rv = findViewById(R.id.rvBerita);
        lm = new LinearLayoutManager(this);
        sw = findViewById(R.id.swBerita);

        rv.setDrawingCacheEnabled(true);
        rv.setItemViewCacheSize(20);

        rv.setAdapter(adp);
        rv.setLayoutManager(lm);

        loadData();
    }

    private void loadData() {
        sw.setRefreshing(true);
        String page;
        if(data.isEmpty()){
            page = null;
        }else{
            page = String.valueOf((data.size()/10)+1);
        }

        Call<Mberita> call = endpoint.berita(page);
        call.enqueue(new Callback<Mberita>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Mberita> call, Response<Mberita> response) {
                sw.setRefreshing(false);
                if(response.isSuccessful() && response.body() != null && response.body().getBerita() != null){
                    data.addAll(response.body().getBerita().getData());
                    if(data.isEmpty()){
                        adp.notifyDataSetChanged();
                    }else{
                        adp.notifyItemInserted(data.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<Mberita> call, Throwable t) {
                sw.setRefreshing(false);
            }
        });
    }
}