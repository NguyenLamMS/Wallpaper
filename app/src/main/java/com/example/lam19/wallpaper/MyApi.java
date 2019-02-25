package com.example.lam19.wallpaper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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
        @GET("/addfavorite.php?")
        public Call<ResponseBody> Favorite(@Query("id_image") Integer id_image,@Query("id_user") Integer id_user, @Query("image_medium") String image_medium, @Query("name_image") String name_image, @Query("image_original") String image_original);
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
        public Call<ArrListFavorite> GetFavorite(@Query("id_user") Integer id_user);
    }
}

