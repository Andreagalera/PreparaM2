package dsa.eetac.upc.edu.preexam2followsgit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    //Para recojer el texto del TextView al cliclarle boton
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Definimos el nombre del layput relacionado con este activity
        setContentView(R.layout.activity_main);

        //Declarar el boton del layout del Main y lo identificamos con el nombre que tenga el boton en el xml
        final Button button = (Button)findViewById(R.id.button);
        //Asignamos al boton el evento click y definimos que hay que hacer cuando cliquemos
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creamos un intent para abrir el nuevo activity, le ponemos el nombre de la clase relacionada
                Intent intent =  new Intent(view.getContext(), Profile.class);
                //Creamos una variable y la identificamos con el nombre donde esta la información del TextView
                EditText editText = (EditText) findViewById(R.id.editText);
                //Guardamos el valor del editText una variable tipo String
                String message = editText.getText().toString();
                //Para pasar el string de un a otro activity se lo defines de esta manera
                intent.putExtra(EXTRA_MESSAGE, message);
                //Se abre la actividad
                startActivity(intent);
            }
        });

    }
}
