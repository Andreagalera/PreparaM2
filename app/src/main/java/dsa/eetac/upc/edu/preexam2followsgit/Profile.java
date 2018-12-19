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

    //Declaar API
    private APIService myAPIRest;
    //Declarar Retrofit
    private Retrofit retrofit;
    //Declarar/Crear Recycler
    private Recycler recycler;
    private RecyclerView recyclerView;
    //Declarar EXTRA MESSAGE
    public String message;
    //Declarar textview y imageview que aparecen en el layout para pasar valor
    private TextView numrepostxt;
    private TextView numfollowstxt;
    ImageView activityProfileIVInternet;
    //Declarar spinner de cargando donde estamos esperando los datos
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Identificamos el recyclerview cerado antes con el nombre que tenga en el xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Recycler(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        //Asignamos a cada liena del recyclerview el lienarlayout de itemfollowe
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Obtiene el Intent para empiezar la actividad y extraigo el string
        //Recogemos el intento para empezar la actividad, y coges el mensaje que te ha paso el otro layout
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Asignamos variable de de los diferents textview y image view a sus variables
        numrepostxt = (TextView) findViewById(R.id.repostxt);
        numfollowstxt = (TextView) findViewById(R.id.followtxt);
        activityProfileIVInternet = (ImageView) findViewById(R.id.activityIVInternet);

        //Texto andreagalera
        TextView textView = findViewById(R.id.nametxt);
        textView.setText(message);



        //Progress loading. Justo al abrir esta actividad ponemos el spinner de cargando
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Creamos API( vigilar que API este bien)
        myAPIRest = APIService.createAPIService();
        //Llamar función dame info del usuario(foto, repos, follows)
        getData();
        //Llamar función dame info de la lista de usuarios (nombre y foto)
        getFollowers();
    }

    //Funcion get 1
    public void getData() {
        //Desarrollamos función del API//Call<User> getProfile(@Path("username") String username);
        Call<User> userCall = myAPIRest.getProfile(message);
        //Siempre igual
        userCall.enqueue(new Callback<User>() {
            //Si entra en el response esque la concexion ha ido bien
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    //Creamos usuario, body ya que tiene más de un campo (meteremos iamgen, follows y respos)
                    User user = response.body();
                    //Logcat
                    Log.i("Login:" + user.login, response.message());
                    //Metodo para añadir la foto a imageview
                    Picasso.with(getApplicationContext()).load(user.avatar_url).into(activityProfileIVInternet);
                    //Logcat
                    Log.i("Repos:" + user.public_repos, response.message());
                    //Asignamos el numero de repos a numrepostxt(Vigila error " "+user.public_repos)
                    numrepostxt.setText(" "+user.public_repos);
                    //Logcat
                    Log.i("Followers:" + user.followers, response.message());
                    //Asignamos el numero de follows a numfollowstxt(Vigila error " "+user.public_repos)
                    numfollowstxt.setText(" "+user.followers);
                    //Si la extracción de datos ha ido bien y cada campo ya tiene sus parameytros desparecera el progressDialog
                    progressDialog.hide();

                    //Si no se ha podido recoger bien la información( Siempre igual, cambiar response failure)
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

            //Si no se ha podido connectar con la API
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("GITHUB", t.getMessage());

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

    //Función que recoge la lista de datos de los follows con foto y nombre
    public void getFollowers() {
        //Desarrollamos función del API// Call<List<User>> getFollowers(@Path("username") String username);
        Call<List<User>> followerCall = myAPIRest.getFollowers(message);
        followerCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    //Creamos lista ususario con los datos de List<User> response en new lIst (tipo body), y ahí iremos añadiendo los diferentes usuarios con sus datos
                    List<User> newList =response.body();
                    recycler.addFollowers(newList);
                    for(int i = 0; i < newList.size(); i++) {
                        Log.i("Login: " + newList.get(i).login, response.message());
                        Log.i("Size of the list: " +newList.size(), response.message());
                    }
                    //Si va bien desaparece progressDialog
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

}