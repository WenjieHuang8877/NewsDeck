package com.laioffer.tinnews.network;

import com.laioffer.tinnews.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
// defined two GET methods with relative URL paths, and their query parameters.
// The path and parameter names are from the API doc.
// Retrofit can use the annotations to generate the actual implementations
public interface NewsApi {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(@Query("country") String country,@Query("page") int page);

    @GET("everything")
    Call<NewsResponse> getEverything(
            @Query("q") String query, @Query("pageSize") int pageSize);

}
