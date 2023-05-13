package com.laioffer.tinnews.ui.save;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.tinnews.model.Article;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private SavedNewsAdapter adapter;
    public SwipeToDeleteCallback(SavedNewsAdapter adapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        adapter = adapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        //Log.d("wenjie", "pass position: " + i + ", position from viewhholder: " + position);
        Article article = adapter.getArticles().get(position);
        adapter.deleteArticle(article);

    }


}
