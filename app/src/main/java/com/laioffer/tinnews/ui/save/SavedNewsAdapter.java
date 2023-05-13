package com.laioffer.tinnews.ui.save;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.SavedNewsItemBinding;
import com.laioffer.tinnews.model.Article;
import com.squareup.picasso.Picasso;

//import java.time.format.DateTimeFormatter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder> implements ItemMoveCallback.ItemTouchHelperContract{

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        Collections.swap(articles, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(SavedNewsViewHolder holder) {
       holder.itemView.setBackgroundColor(Color.RED);
    }

    @Override
    public void onRowClear(SavedNewsViewHolder holder) {
        holder.itemView.setBackgroundColor(Color.WHITE);
    }

    public interface ItemCallback {
        //onOpenDetails is to be implemented for opening
        // a new fragment for article details.
        void onOpenDetails(Article article);
        //onRemoveFavorite is te be implemented
        // to remove articles in the saved database.
        void onRemoveFavorite(Article article);

        void onDelete(Article article);
    }

    private ItemCallback itemCallback;

    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }


    // 1. Supporting data:
    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> newList){
        articles.clear();
        articles.addAll(newList);
        //every time a new list is set, we call
        // notifyDataSetChanged to let the adapter refresh
        // and re-render the data.
        notifyDataSetChanged();
    }
    public List<Article> getArticles() {
        return articles;
    }
    // 3. Adapter overrides:
    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_news_item,parent,false);
        return new SavedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.authorTextView.setText(article.author);
        holder.descriptionTextView.setText(article.description);

        //format the date and time nicer in local timezone
        String dateUTC = article.publishedAt;
        Log.d("date",dateUTC.toString());

        String convertedDate  = "";


       // utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = utcFormat.parse(dateUTC);
            DateFormat convertedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            convertedFormat.setTimeZone(TimeZone.getDefault());
            String time = convertedFormat.format(date);
            //LocalDateTime now = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                now = LocalDateTime.now();
//                convertedFormat.format(now);
//                convertedDate = convertedFormat.format(date);
//            }
            holder.dateTextView.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //holder.dateTextView.setText(dateUTC);


       // holder.favoriteIcon.setOnClickListener(v -> itemCallback.onRemoveFavorite(article));
      //  holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(article));
        if(article.urlToImage !=null){
            Picasso.get().load(article.urlToImage).resize(20,20).into(holder.miniView);
        }

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // 2. SavedNewsViewHolder:
    public static class SavedNewsViewHolder extends RecyclerView.ViewHolder{
        TextView authorTextView;
        TextView descriptionTextView;
        ImageView favoriteIcon;
        TextView dateTextView;
        ImageView miniView;

        public SavedNewsViewHolder(@NonNull View itemView){
            super(itemView);
            SavedNewsItemBinding binding = SavedNewsItemBinding.bind(itemView);
            authorTextView = binding.savedItemAuthorContent;
            descriptionTextView = binding.savedItemDescriptionContent;
            favoriteIcon = binding.savedItemFavoriteImageView;
            dateTextView = binding.savedItemDateContent;
            miniView = binding.savedItemMiniView;

        }
    }
    void deleteArticle(Article article){
        itemCallback.onDelete(article);
    }

}
