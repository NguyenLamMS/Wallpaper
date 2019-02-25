package com.example.lam19.wallpaper;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabTwo extends android.support.v4.app.Fragment {
    private GridView myGridView;
    ArrayList<String> listImageURLs;
    ArrayList<String> listName;
    CustomAdapterCategories customAdapter;

    public TabTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_tab_two,container,false);
        myGridView = (GridView) layout.findViewById(R.id.gridViewCategories);
        listImageURLs = new ArrayList<>();
        listName = new ArrayList<>();
        listImageURLs.add("https://images.pexels.com/photos/207983/pexels-photo-207983.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Funny");
        listImageURLs.add("https://images.pexels.com/photos/682933/pexels-photo-682933.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Games");
        listImageURLs.add("https://images.pexels.com/photos/714899/pexels-photo-714899.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Holidays");
        listImageURLs.add("https://images.pexels.com/photos/207962/pexels-photo-207962.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Love");
        listImageURLs.add("https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Music");
        listImageURLs.add("https://images.pexels.com/photos/33109/fall-autumn-red-season.jpg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Nature");
        listImageURLs.add("https://images.pexels.com/photos/860379/pexels-photo-860379.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("News and Politics");
        listImageURLs.add("https://images.pexels.com/photos/417142/pexels-photo-417142.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Animals");
        listImageURLs.add("https://images.pexels.com/photos/164839/pexels-photo-164839.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Anime");
        listImageURLs.add("https://images.pexels.com/photos/9816/pexels-photo-9816.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("People");
        listImageURLs.add("https://images.pexels.com/photos/1572348/pexels-photo-1572348.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Girls");
        listImageURLs.add("https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Sport");
        listImageURLs.add("https://images.pexels.com/photos/908284/pexels-photo-908284.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Technology");
        listImageURLs.add("https://images.pexels.com/photos/266246/pexels-photo-266246.jpeg?auto=compress&cs=tinysrgb&h=350");
        listName.add("Logos");
        customAdapter = new CustomAdapterCategories(getActivity().getApplicationContext(),R.layout.custom_categories,listImageURLs);
        myGridView.setAdapter(customAdapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),Search.class);
                intent.putExtra("valueSearch",listName.get(i));
                startActivity(intent);
            }
        });
        return layout;
    }
    private class CustomAdapterCategories extends BaseAdapter {
        private Context context;
        private int layout;
        private List<String> listImageURLs;

        public CustomAdapterCategories(Context context, int layout, List<String> images) {
            this.context = context;
            this.layout = layout;
            this.listImageURLs = images;
        }

        @Override
        public int getCount() {
            return listImageURLs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderCategories viewHolder ;
            if(convertView == null){
                viewHolder = new ViewHolderCategories();
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout,null);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.imagesCategories);
                viewHolder.txtNameCategori = (TextView) convertView.findViewById(R.id.textCategories);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolderCategories)  convertView.getTag();
            }
            viewHolder.txtNameCategori.setText(listName.get(position));
            //load image
            Glide.with(context)
                    .load(listImageURLs.get(position))
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .override(200,300)
                            .dontAnimate()
                            .dontTransform())
                    .into(viewHolder.image);
            return convertView;
        }

        public class ViewHolderCategories{
            ImageView image;
            TextView txtNameCategori;
        }

    }
}
