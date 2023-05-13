package com.laioffer.tinnews.ui.search;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.laioffer.tinnews.databinding.FragmentSearchBinding;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.repository.NewsViewModelFactory;
import com.laioffer.tinnews.ui.search.SearchViewModel;

public class SearchFragment extends Fragment {
    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
       // return the grid number for item?
        //make the first item take the width of the entire screen
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
               return position == 0 ? 2:1;
            }
        });
        binding.newsResultsRecyclerView.setLayoutManager(gridLayoutManager);
        binding.newsResultsRecyclerView.setAdapter(newsAdapter);

        //set the search input in the onQueryTextSubmit callback.
        binding.newsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    viewModel.setSearchInput(query);
                }
                binding.newsSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        NewsRepository newsRepository = new NewsRepository();

        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(newsRepository))
                .get(SearchViewModel.class);

        viewModel.searchNews().observe(getViewLifecycleOwner(), newsResponse -> {
            if (newsResponse != null) {
                Log.d("SearchFragment", newsResponse.toString());
                newsAdapter.setArticles(newsResponse.articles);

            }
        });


        newsAdapter.setItemCallback(article -> {
            // get the navigation direction, provide the required argument article,
            // and navigate to the direction.
            SearchFragmentDirections.ActionNavigationSearchToNavigationDetails direction
                    = SearchFragmentDirections.actionNavigationSearchToNavigationDetails(article);
            NavHostFragment.findNavController(SearchFragment.this).navigate(direction);

        });
    }

}

