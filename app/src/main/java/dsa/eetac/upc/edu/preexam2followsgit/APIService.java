package dsa.eetac.upc.edu.preexam2followsgit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    String BASE_URL = "https://api.github.com/users/";

    @GET("{username}")
    Call<User> getProfile(@Path("username") String username);

    @GET("{username}/followers")
    Call<Follower> getFollowers(@Path("username") String username);


    static APIService createAPIService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(APIService.class);
    }

}
