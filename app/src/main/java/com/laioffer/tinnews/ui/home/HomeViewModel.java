package com.laioffer.tinnews.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final NewsRepository repository;
   // private final MutableLiveData<String> countryInput = new MutableLiveData<>();
    //MutableLiveData<Boolean> addFavResult = new MutableLiveData<>();
    MutableLiveData<List<Article>> articles = new MutableLiveData<>();
    int page = 1;

    public HomeViewModel(NewsRepository newsRepository) {
        this.repository = newsRepository;
    }

    //public void setCountryInput(String country) {
//        getTopHeadlines();
//    }

    //https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
    //The difference this time is that we don’t need to expose the observing result.
    // So we don’t have to do the Transformations.switchMap trick.
//    public LiveData<NewsResponse> getTopHeadlines() {
//        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
//    }

    //
    public void getTopHeadlines() {
        // 函数中传进去了三个参数，其中包括结构的实现类，
        repository.getTopHeadlines("us", page, new NewsRepository.OnResult<List<Article>>(){
            @Override
            public void onSuccess (List<Article> result) {

                List<Article> reload = articles.getValue();
                if(reload == null){
                    reload = new ArrayList<>();
                }
                reload.addAll(result);

                articles.setValue(reload);
//                List<Article> reload = new ArrayList<>();
//                List<Article> origin = articles.getValue();
//                List<Article> res = new ArrayList<>();
//                res.addAll(origin);
//                res.addAll(reload);
//                articles.setValue(res);

                //nullpointerexception
//                List<Article> reload = articles.getValue();
//                reload.addAll(result);
             ///   reload.addAll(result);
//                for(Article article:result){
//                    reload.add(article);
//                }

                //articles.setValue(reload);
                page += 1;
                //addFavResult.setValue(true);
            }
        });
    }

    public void setFavoriteArticleInput(Article article) {
        repository.favoriteArticle(article);
    }


}

