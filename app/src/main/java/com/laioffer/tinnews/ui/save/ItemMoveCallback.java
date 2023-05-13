package com.laioffer.tinnews.ui.save;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.tinnews.model.Article;

public class ItemMoveCallback extends ItemTouchHelper.Callback {
    private SavedNewsAdapter adapter;

    public ItemMoveCallback(SavedNewsAdapter adapter){
        this.adapter = adapter;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        switch (direction) {
            case ItemTouchHelper.LEFT:
                int position = viewHolder.getAdapterPosition();
                //Log.d("wenjie", "pass position: " + i + ", position from viewhholder: " + position);
                Article article = adapter.getArticles().get(position);
                adapter.deleteArticle(article);
            case ItemTouchHelper.RIGHT:
                break;
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 只能允许左滑
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        //拖拽滑动
        adapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof RecyclerView.ViewHolder) {
                SavedNewsAdapter.SavedNewsViewHolder holder=
                        (SavedNewsAdapter.SavedNewsViewHolder) viewHolder;
                adapter.onRowSelected(holder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof RecyclerView.ViewHolder) {
            SavedNewsAdapter.SavedNewsViewHolder holder=
                    (SavedNewsAdapter.SavedNewsViewHolder) viewHolder;
            adapter.onRowClear(holder);
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(SavedNewsAdapter.SavedNewsViewHolder holder);
        void onRowClear(SavedNewsAdapter.SavedNewsViewHolder holder);

    }


}
