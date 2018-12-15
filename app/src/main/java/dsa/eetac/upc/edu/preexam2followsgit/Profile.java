package dsa.eetac.upc.edu.preexam2followsgit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Profile extends MainActivity {

    private APIService myAPIRest;
    private Retrofit retrofit;
    public String message;

    ImageView activityProfileIVInternet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);

        activityProfileIVInternet = (ImageView)findViewById(R.id.activityIVInternet);

        myAPIRest = APIService.createAPIService();
        getData();
    }




    public void getData(){
        Call<User> userCall = myAPIRest.getProfile(message);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    Log.i("Login:" + user.login, response.message());
                    Picasso.with(getApplicationContext()).load(user.avatar_url).into(activityProfileIVInternet);

                }
                else{
                    Log.e("No api connection", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("No api connection", t.getMessage());
            }
        });

    }
}
