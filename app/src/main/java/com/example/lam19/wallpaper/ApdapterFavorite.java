package com.example.lam19.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Random;


public class ApdapterFavorite extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<arrUrlImage> arrListImage = new ArrayList<>();
    Integer[] totalLike = {20,30,40,50};
    Integer[] totalDownload = {50,33,34,67};
    Integer countTotal = 0;
    public ApdapterFavorite(Context context,ArrayList<arrUrlImage> arrListImage) {
        this.context = context;
        this.arrListImage = arrListImage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_gridview,null);
            return new ViewHoler(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHoler, final int i) {
       if(viewHoler instanceof ViewHoler){
           populateItemRows((ViewHoler) viewHoler,i);
       }
    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    @Override
    public int getItemCount() {
        return arrListImage.size();
    }

    class ViewHoler extends RecyclerView.ViewHolder{
        ImageView image;
        TextView txtName;
        TextView txtLike;
        TextView txtDownload;
        RelativeLayout relativeLayout ;
        LinearLayout linearLayout;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.images);
            txtName = itemView.findViewById(R.id.txtNameImage);
            txtLike = itemView.findViewById(R.id.totalLike);
            txtDownload = itemView.findViewById(R.id.totalDownload);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutItem);
            linearLayout = itemView.findViewById(R.id.containerGrid);
        }
    }

    private void populateItemRows(ViewHoler viewHoler, final int i) {
        viewHoler.txtLike.setText(totalLike[countTotal].toString());
        viewHoler.txtDownload.setText(totalDownload[countTotal].toString());
        if(countTotal > 2){
            countTotal = 0;
        }else{
            countTotal += 1;
        }
        viewHoler.txtName.setText(arrListImage.get(i).getNamePhoto());
        viewHoler.linearLayout.setBackgroundColor(getRandomColor());
        Glide.with(context)
                .load(arrListImage.get(i).src.medium)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .override(200,800)
                        .dontAnimate()
                        .dontTransform())
                .into(viewHoler.image);
        viewHoler.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ItemGridView.class);
                intent.putExtra("image_original",arrListImage.get(i).src.original);
                intent.putExtra("image_large",arrListImage.get(i).src.large);
                intent.putExtra("image_portrait",arrListImage.get(i).src.portrait);
                intent.putExtra("image_medium",arrListImage.get(i).src.medium);
                intent.putExtra("id_image",arrListImage.get(i).getId());
                intent.putExtra("name_image", arrListImage.get(i).getNamePhoto());
                v.getContext().startActivity(intent);
            }
        });

    }
}

