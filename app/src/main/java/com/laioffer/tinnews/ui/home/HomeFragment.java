package com.laioffer.tinnews.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.FragmentHomeBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.repository.NewsViewModelFactory;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements CardStackListener {
    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;
    private CardStackLayoutManager layoutManager;
    private CardSwipeAdapter swipeAdapter;
    List<Article> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup CardStackView
        swipeAdapter = new CardSwipeAdapter();
        layoutManager = new CardStackLayoutManager(requireContext(), this);

        layoutManager.setStackFrom(StackFrom.Top);
        binding.homeCardStackView.setLayoutManager(layoutManager);
        binding.homeCardStackView.setAdapter(swipeAdapter);

        // Handle like unlike button clicks
        binding.homeLikeButton.setOnClickListener(v -> swipeCard(Direction.Right));
        binding.homeUnlikeButton.setOnClickListener(v -> swipeCard(Direction.Left));

        binding.homeReplayButton.setOnClickListener(v -> {
            RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .build();
            layoutManager.setRewindAnimationSetting(setting);
            binding.homeCardStackView.rewind();
        });

        binding.homeUnlikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeCard(Direction.Left);
            }
        });

        //生成view model
        NewsRepository repository = new NewsRepository();
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository)).get(HomeViewModel.class);

        //viewModel.setCountryInput("us");
        //get the first page of result first
        viewModel.getTopHeadlines();

//        viewModel
//                .getTopHeadlines()
//                .observe(
//                        getViewLifecycleOwner(),
//                        response -> {
//                            if (response != null) {
//                                Log.d("HomeFragment", response.toString());
//                                swipeAdapter.setArticles(response.articles);
//                            }
//                        }
//                );

      // obverve the livedata<articles> , if it changed, update the adapter
        viewModel.articles.observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null) {
                    Log.d("HomeFragment", articles.toString());
                   // list = newsResponse.articles;
                    swipeAdapter.setArticles(articles);
                }
            }
        });


        //adapter里面的callback方法，重写了adpter里面的callback方法
        //！！！
        //一般情况下是一个class实现接口，重写方法，因为这个callback方法定义在类里面，可以通过该方法来实现callback？
//        swipeAdapter.setPageCallback(new CardSwipeAdapter.PageCallback() {
//            @Override
//            public void onUpdate() {
//                viewModel.getTopHeadlines();
//            }
//        });

    }


    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Normal.duration)
                .build();
        layoutManager.setSwipeAnimationSetting(setting);
        binding.homeCardStackView.swipe();
    }


    @Override
    public void onCardDragging(Direction direction, float v) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Left) {
            Log.d("CardStackView", "Unliked " + layoutManager.getTopPosition());
        } else if (direction == Direction.Right) {
            Log.d("CardStackView", "Liked " + layoutManager.getTopPosition());
            //we wire-up the UI swipe interaction with the data persistence operation.
            Article article = swipeAdapter.getArticles().get(layoutManager.getTopPosition() - 1);
            viewModel.setFavoriteArticleInput(article);
            //show a toast when you successfully save the image
            Context context = requireContext();
            CharSequence text = "Article Saved!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if(layoutManager.getTopPosition() == swipeAdapter.getArticles().size() - 1){
            viewModel.getTopHeadlines();
        }

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int i) {

    }

    @Override
    public void onCardDisappeared(View view, int i) {

    }
}