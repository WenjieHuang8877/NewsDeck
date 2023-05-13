package com.laioffer.tinnews.ui.search;

import android.content.ClipData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.SearchNewsItemBinding;
import com.laioffer.tinnews.model.Article;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchNewsAdapter extends RecyclerView.Adapter<SearchNewsAdapter.SearchNewsViewHolder> {
    public interface ItemCallback{
        void onOpenDetails(Article article);
    }

    private ItemCallback itemCallback;

    public void setItemCallback(ItemCallback itemCallback){
        this.itemCallback = itemCallback;
    }



    // 1. Supporting data:
    // TODO
    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> newsList) {
        articles.clear();
        articles.addAll(newsList);
        //every time a new list is set,
        // we call notifyDataSetChanged to let the adapter refresh and re-render the data.
        notifyDataSetChanged();
    }



    // 2. SearchNewsViewHolder:
    // TODO
    //Create an adapter that contains the ViewHolder.
    // for holding the view references.
    public static class SearchNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemTitleTextView;
        TextView itemAuthorTextView;
        TextView itemDateTextView;
        ImageView heartImageView;
        ImageView itemDetailIcon;
        //ImageView filledHeartImageView;

        public SearchNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            SearchNewsItemBinding binding = SearchNewsItemBinding.bind(itemView);
            itemImageView = binding.searchItemImage;
            itemTitleTextView = binding.searchItemTitle;
            itemAuthorTextView = binding.searchItemAuthor;
            itemDateTextView = binding.searchItemDate;
            heartImageView = binding.searchItemHeartView;
            itemDetailIcon = binding.searchItemDetail;
        }
    }

    // 3. Adapter overrides:
    // onCreateViewHolder is for providing the generated item views;
    @NonNull
    @Override
    public SearchNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_news_item, parent, false);
        SearchNewsViewHolder viewHolder = new SearchNewsViewHolder(view);
        Log.d("test", "onCreate " + viewHolder.toString());
        return viewHolder;
    }
    //onBindViewHolder is for binding the data with a view
    //Link the item_view layout to the ViewHolder and finish create/bind ViewHolder process in the recyclerView
    @Override
    public void onBindViewHolder(@NonNull SearchNewsViewHolder holder, int position) {
        Log.d("test", "onCreate " + holder.toString());
        Article article = articles.get(position);
        holder.itemTitleTextView.setText(article.title);
        holder.itemAuthorTextView.setText(article.author);
        holder.itemDateTextView.setText(article.publishedAt);

        holder.itemView.setOnClickListener(view -> itemCallback.onOpenDetails(article));


        //How to set different images to viewHolder’s favoriteImageView.
        // For example: set empty heart icon to odd index, filled heart icon to even index.
        if(position % 2 ==0){
            holder.heartImageView.setImageResource(R.drawable.ic_favorite_24);
//            holder.filledHeartImageView.setVisibility(View.VISIBLE);
        }else{
            holder.heartImageView.setImageResource(R.drawable.ic_favorite_border_24);
//            holder.heartImageView.setVisibility(View.VISIBLE);
//            holder.filledHeartImageView.setVisibility(View.INVISIBLE);

        }

        if( article.urlToImage != null){
            Picasso.get().load(article.urlToImage).resize(200,200)
                    .into(holder.itemImageView);
        }
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(article));
        holder.itemDetailIcon.setOnClickListener(v -> itemCallback.onOpenDetails(article));

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
