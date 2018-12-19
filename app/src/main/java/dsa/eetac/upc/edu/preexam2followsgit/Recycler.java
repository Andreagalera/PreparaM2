package dsa.eetac.upc.edu.preexam2followsgit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    //Creamos lista con la estrcutura de user para recoger todos los followes de u usuario. Llamlo data
    private List<User> data;
    //Necesario paar el constructor del recycler
    private Context context;

    //Gestionamos el RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Asignamos el etxto del TextView al TextView del layout destino
        public TextView text;

        //DEcalramos los textviews y imageview del recycler
        private ImageView followerImageView;
        private TextView nametxt;
        private LinearLayout linearLayout;

        //Con esta funcion a asignamos las variables anteriores a los nombres del textview y imageview del item
        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(android.R.id.text1);
            followerImageView=v.findViewById(R.id.imageuser);
            nametxt=v.findViewById(R.id.usernametxt);
            linearLayout =v.findViewById(R.id.linearLayout);
        }
    }

    //Añade a la lista data de la estructura de un usuario sus followers. ???
    public void addFollowers (List<User> followerList){
        data.addAll(followerList);
        notifyDataSetChanged();
    }

    //Constructor, utilizar context. Declaramos la lista data iniciandola
    public Recycler(Context context) {

        this.data = new ArrayList<>();
        this.context=context;
    }

    //Inflamos el reccylerView con las filas del item_follower
    @Override
    public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(v);
    }

    //Donse se muestra las cosas
    @Override
    public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
        //Creanos variable user y vamos guardando la información de la lista data
        User userdata = ((User) data.get(position));

        //Añade los diferentes campos que querras mostrar de la clase usuario ( nombre y imagen)
        //El nokbre de la variable cfreada para el TextView le asiganmos la información de username (login)
        holder.nametxt.setText(userdata.login);
        //Metodo para picaaso, avatar_url =atributo e la foto usuario, followerImageView= variable creada paar imageview
        Picasso.with(context).load(userdata.avatar_url).into(holder.followerImageView);

    }

    //Devuelce el tamaño de la información de Item. Poner
    @Override
    public int getItemCount() {
        return data.size();
    }
}
