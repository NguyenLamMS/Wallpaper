package com.example.lam19.wallpaper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class MyApi {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://lam108ln.000webhostapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public interface ApiRegister{
        @GET("/createacc.php?")
        public Call<ResponseBody> Register(@Query("email") String email, @Query("password") String password);
    }
    public interface ApiLogin {
        @GET("/login.php?")
        public Call<dataApiLogin> Login(@Query("email") String email, @Query("password") String password);
    }
    public interface ApiFavorite{
        @POST("/addfavorite.php?")
        @FormUrlEncoded
        public Call<ResponseBody> Favorite(@Field("id_image") Integer id_image, @Field("id_user") Integer id_user, @Field("image_medium") String image_medium, @Field("name_image") String name_image, @Field("image_original") String image_original, @Field("image_large") String image_large, @Field("image_portrait") String image_portrait);
    }
    public interface ApiRemoveFavorite{
        @GET("/removefavorite.php?")
        public  Call<ResponseBody> RemoveFavorite(@Query("id_image") Integer id_image, @Query("id_user") Integer id_user);
    }
    public interface ApiCheckFavorite{
        @GET("/checkfavorite.php?")
        public Call<ResponseBody> CheckFavorite(@Query("id_image") Integer id_image, @Query("id_user") Integer id_user);
    }
    public interface ApiGetFavorite{
        @GET("/getlistfavorite.php?")
        public Call<resulltFeatured> GetFavorite(@Query("id_user") Integer id_user);
    }
}

