package com.example.lam19.wallpaper;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class Api {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.pexels.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getRegister() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://lam108ln.000webhostapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public interface API{
        @GET("v1/curated?")
        public Call<resulltFeatured> searchImage(@Header("Authorization") String auth, @Query("per_page") Integer query,@Query("page") Integer page);
    }
    public interface APISearch{
        @GET("v1/search?")
        public Call<resulltFeatured> searchImageQuery(@Header("Authorization") String auth, @Query("query") String query,@Query("per_page") Integer per_page,@Query("page") Integer page);
    }
    public interface APIInfoImage{
        @GET("v1/photos/{id}")
        public Call<arrUrlImage> getInfoImage(@Header("Authorization") String auth, @Path("id") Integer id);
    }
}
