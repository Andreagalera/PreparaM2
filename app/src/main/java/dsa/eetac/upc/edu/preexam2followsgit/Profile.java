package dsa.eetac.upc.edu.preexam2followsgit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Profile extends MainActivity {

    private APIService myAPIRest;
    private Retrofit retrofit;

    private Recycler recycler;
    private RecyclerView recyclerView;

    public String message;
    private TextView numrepostxt;
    private TextView numfollowstxt;
    private String token;
    ImageView activityProfileIVInternet;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Creado
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Recycler(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Obtiene el Intent paraempieza la actividad y extraigo el string
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //TextView donde muestra el numero de repositorios y el numero de seguidores
        numrepostxt = (TextView) findViewById(R.id.repostxt);
        numfollowstxt = (TextView) findViewById(R.id.followtxt);

        TextView textView = findViewById(R.id.nametxt);
        textView.setText(message);

        activityProfileIVInternet = (ImageView) findViewById(R.id.activityIVInternet);


        //Progress loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        myAPIRest = APIService.createAPIService();
        getData();

        getFollowers();
    }


    public void getData() {
        Call<User> userCall = myAPIRest.getProfile(message);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    Log.i("Login:" + user.login, response.message());

                    Picasso.with(getApplicationContext()).load(user.avatar_url).into(activityProfileIVInternet);

                    Log.i("Repos:" + user.public_repos, response.message());
                    numrepostxt.setText(" "+user.public_repos);

                    Log.i("Followers:" + user.followers, response.message());
                    numfollowstxt.setText(" "+user.followers);

                    progressDialog.hide();

                } else {
                    //Log.e("Response failure", response.message());
                    Log.e("Response failure", String.valueOf(response.errorBody()));

                    //Alert dialog
                    //Establece
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", ((dialog, which) -> finish()));
                    //Crea
                    AlertDialog alertDialog=alertDialogBuilder.create();
                    //Enseña
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("No api connection", t.getMessage());

                //Alert dialog
                //Establece
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", ((dialog, which) -> finish() ));
                //Crea
                AlertDialog alertDialog=alertDialogBuilder.create();
                //Enseña
                alertDialog.show();
            }
        });

    }

    public void getFollowers() {
        Call<List<User>> followerCall = myAPIRest.getFollowers(message);
        followerCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    List<User> newList =response.body();
                    recycler.addFollowers(newList);
                    for(int i = 0; i < newList.size(); i++) {
                        Log.i("Login: " + newList.get(i).login, response.message());
                        Log.i("Size of the list: " +newList.size(), response.message());
                    }

                    progressDialog.hide();

                }
                else {
                    Log.e("No api connection", response.message());

                    //Alert dialog
                    //Establece
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", ((dialog, which) -> finish() ));
                    //Crea
                    AlertDialog alertDialog=alertDialogBuilder.create();
                    //Enseña
                    alertDialog.show();
                }
            }

            //Alert dialog, titulo meensaje, parametros adicionales error servidor
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("No api connection", t.getMessage());

                //Alert dialog
                //Establece
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", ((dialog, which) -> finish() ));
                //Crea
                AlertDialog alertDialog=alertDialogBuilder.create();
                //Enseña
                alertDialog.show();

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (token != null) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            token = data.getStringExtra("token");
        }
    }
}