package com.laioffer.tinnews.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laioffer.tinnews.TinNewsApplication;
import com.laioffer.tinnews.database.TinNewsDatabase;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.network.NewsApi;
import com.laioffer.tinnews.network.RetrofitClient;
import com.laioffer.tinnews.ui.save.SavedNewsAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//Repository as an intermediate container for providing data;
// The network requests are hidden behind the Repository
public class NewsRepository {
    private final NewsApi newsApi;
    private final TinNewsDatabase database;


    public NewsRepository(){

        newsApi = RetrofitClient.newInstance().create(NewsApi.class);
        database = TinNewsApplication.getDatabase();

    }




//    public LiveData<NewsResponse> getTopHeadlines(String country,) {
//        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
//        //Call<NewsResponse> getTopHeadlines
//        newsApi.getTopHeadlines(country)
//                .enqueue(new Callback<NewsResponse>() {
//                    @Override
//                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
//                        if (response.isSuccessful()) {
//                            topHeadlinesLiveData.setValue(response.body());
//                        } else {
//                            topHeadlinesLiveData.setValue(null);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewsResponse> call, Throwable t) {
//                        topHeadlinesLiveData.setValue(null);
//                    }
//                });
//        return topHeadlinesLiveData;
//    }
public interface OnResult<T> {

    void onSuccess(T rst);

//        void onRemoveFavorite(Article article);
//
//        void onDelete(Article article);
}

    private OnResult<List<Article>> onResult;
    public void setOnResult(OnResult onResult){
        this.onResult = onResult;
    }

    //？？？？删除了下面一条，不需要return，只需要通过接口重写方法，更行home viewmodel的 article list；
    public void getTopHeadlines(String country, Integer page, OnResult<List<Article>> onResult) {

//        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        Call<NewsResponse> responseCall = newsApi.getTopHeadlines(country, page);
        //接口的作用就是传参，所以在获取数据的地方设置callback函数，在需要数据的地方得到数据，重写方法，执行操作
        //生成call方法，call方法在其他线程执行，返回callback方法，里面的class就是返回的结果
        //类似于api生成call在执行时，以后一个callback参数，执行结果就是newsresponse，重写callback函数，对newsponse这个结果进行处理
        // onresult接口中，定义了泛型，在viewmodel的实现中，实例为article list，重写方法，对结果进行处理
        // 所以，在得到数据的位置设置callback接口，在需要数据的位置实现接口。
        //？？？是不是也可以用下一条函数， 把结果发送回到view model，更新数据呢？
        // ：callback的结果如果成功，执行 OnResult方法// 当生成新的newsResponse的时候，更新view model里面livedata
        responseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    //topHeadlinesLiveData.setValue(response.body());
                    Log.d("getTopHeadlines", response.body().toString());
                   // onResult.addAll(response.body().articles);
                    onResult.onSuccess(response.body().articles);
                } else {
                    //topHeadlinesLiveData.setValue(null);
                    Log.d("getTopHeadlines", response.toString());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                //topHeadlinesLiveData.setValue(null);
                Log.d("getTopHeadlines", t.toString());
            }
        });
             //return topHeadlinesLiveData;
    }


    //删除了，不需要return，只需要通过接口重写方法，更行home viewmodel的 article list；
//    public LiveData<NewsResponse> getTopHeadlines(String country, Integer page, OnResult<List<Article>> onResult) {
//
//        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
//        Call<NewsResponse> responseCall = newsApi.getTopHeadlines(country, page);
//        responseCall.enqueue(new Callback<NewsResponse>() {
//            @Override
//            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
//                if (response.isSuccessful()) {
//                    topHeadlinesLiveData.setValue(response.body());
//                    Log.d("getTopHeadlines", response.body().toString());
//                    // onResult.addAll(response.body().articles);
//                    onResult.onSuccess(response.body().articles);
//                } else {
//                    topHeadlinesLiveData.setValue(null);
//                    Log.d("getTopHeadlines", response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewsResponse> call, Throwable t) {
//                topHeadlinesLiveData.setValue(null);
//                Log.d("getTopHeadlines", t.toString());
//            }
//        });
//        return topHeadlinesLiveData;
//    }

    public LiveData<NewsResponse> searchNews(String query){
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if(response.isSuccessful()){
                            everyThingLiveData.setValue(response.body());
                        }else{
                            everyThingLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                            everyThingLiveData.setValue(null);
                    }
                });
        return everyThingLiveData;
    }
    //execute returns immediately. The database operation runs in the background
    // and notifies the result through the resultLiveData at a later time.
    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        new FavoriteAsyncTask(database, resultLiveData).execute(article);
        return resultLiveData;
    }

    public LiveData<List<Article>> getAllSavedArticles() {
        return database.articleDao().getAllArticles();
    }

    public void deleteSavedArticle(Article article) {
        AsyncTask.execute(() -> database.articleDao().deleteArticle(article));
    }



    private static class FavoriteAsyncTask extends AsyncTask<Article, Void, Boolean> {

        private final TinNewsDatabase database;
        private final MutableLiveData<Boolean> liveData;

        private FavoriteAsyncTask(TinNewsDatabase database, MutableLiveData<Boolean> liveData) {
            this.database = database;
            this.liveData = liveData;
        }

        //Everything inside doInBackground would be executed on a separate background thread.
        @Override
        protected Boolean doInBackground(Article... articles) {
            Article article = articles[0];
            try {
                database.articleDao().saveArticle(article);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
//After doInBackground finishes, onPostExecute would be executed back on the main UI thread.
        @Override
        protected void onPostExecute(Boolean success) {
            liveData.setValue(success);
        }
    }


}
