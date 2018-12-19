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

    //URL donde se encuentra alojada la API
    String BASE_URL = "https://api.github.com/users/";

    //Llamamos a la funció para obtenerla foto, los folowers y repositoris de un usuario a partir su username
    @GET("{username}")
    Call<User> getProfile(@Path("username") String username);

    //LLamamos a la función para obtener la lista (foto y username)
    @GET("{username}/followers")
    Call<List<User>> getFollowers(@Path("username") String username);

    //Creamos la conexión de la API
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
