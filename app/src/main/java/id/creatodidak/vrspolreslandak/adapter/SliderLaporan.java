package id.creatodidak.vrspolreslandak.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.models.SliderItem;
import id.creatodidak.vrspolreslandak.helper.BitmapUtils;

public class SliderLaporan extends
        SliderViewAdapter<SliderLaporan.SliderAdapterVH> {

    private final Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderLaporan(Context context, List<SliderItem> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(List<SliderItem> sliderItems) {
        this.mSliderItems = (sliderItems);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliderlaporanitem, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        SliderItem sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .placeholder(R.drawable.defaultimg)
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtils.expandImage(context, sliderItem.getImageUrl(), "Dokumentasi").show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView image;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.sliderImage);
            this.itemView = itemView;
        }
    }

}