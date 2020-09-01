package com.poojitha.itunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poojitha.itunes.model.Ituns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ItunesAdapter extends RecyclerView.Adapter<ItunesAdapter.MovieViewHolder>  implements Filterable {

    private List<Ituns> itunes;
    private int rowLayout;
    private Context context;
    private List<Ituns> itunsListFiltered;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView artistName;
        TextView trackName;
        TextView collectionName;
        TextView collectionPrice;
        TextView releaseDate;
        ImageView artworkUrl100;
        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            artistName = (TextView) v.findViewById(R.id.artistName);
            trackName = (TextView) v.findViewById(R.id.trackName);
            collectionName = (TextView) v.findViewById(R.id.collectionName);
            collectionPrice = (TextView) v.findViewById(R.id.collectionPrice);
            releaseDate = (TextView) v.findViewById(R.id.releaseDate);
            artworkUrl100 = (ImageView) v.findViewById(R.id.previewUrl);
        }
    }
    public ItunesAdapter(List<Ituns> itunes, int rowLayout, Context context) {
        this.itunes = itunes;
        this.rowLayout = rowLayout;
        this.context = context;
        this.itunsListFiltered = itunes;
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        final Ituns itunItem = itunsListFiltered.get(position);
        holder.artistName.setText(itunItem.getArtistName());
        holder.trackName.setText(itunItem.getTrackName());
        holder.collectionName.setText(itunItem.getCollectionName());
        holder.releaseDate.setText(DateFormat.getDateInstance().format(getDateFormat(itunItem.getReleaseDate()))  );
        holder.collectionPrice.setText(itunItem.getCollectionPrice().toString());
        Glide.with(context)
                .load(itunItem.getArtworkUrl30())
                .into(holder.artworkUrl100);
    }
    public Date getDateFormat(String dtStart)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dtStart);
            System.out.println(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return itunsListFiltered.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itunsListFiltered = itunes;
                } else {
                    List<Ituns> filteredList = new ArrayList<>();
                    for (Ituns row : itunes) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCollectionPrice().toString().contains(charString.toLowerCase())||
                                row.getArtistName().toLowerCase().contains(charString.toLowerCase()) ||row.getTrackName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itunsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itunsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itunsListFiltered = (ArrayList<Ituns>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}