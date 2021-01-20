package istic.projet.estampille;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import istic.projet.estampille.models.APIDenreeAnimale;

public class ListViewAnimalProductAdapter extends BaseAdapter {

    private Context context;
    private List<APIDenreeAnimale> denreeAnimales;
    private LayoutInflater inflater;

    public ListViewAnimalProductAdapter(Context context, List<APIDenreeAnimale> denreeAnimales) {
        this.context = context;
        this.denreeAnimales = denreeAnimales;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return denreeAnimales.size();
    }

    @Override
    public Object getItem(int position) {
        return denreeAnimales.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;
        APIDenreeAnimale result = this.denreeAnimales.get(position);
        if(convertView == null) {
            view = (LinearLayout) inflater.inflate(R.layout.simple_dialog_listview_item, null, false);
            //TODO
            System.out.println("DENREES");
            System.out.println(result.getInfosOrigineDenree());
            System.out.println(result.getInfosTypeDenree());
            System.out.println(result.getTypeDenree().getAnimal());
            System.out.println(result.getTypeDenree().getEspece());
            System.out.println(result.getTypeDenree().getNom());
            System.out.println(result.getOrigineDenree().getPays());
            System.out.println(result.getOrigineDenree().getRegion());
            TextView textViewProductName = view.findViewById(R.id.item_title);
//            textViewProductName.setText(result.getNom());
            TextView textViewProductOrigin = view.findViewById(R.id.item_text);
//            textViewProductOrigin.setText(result.getOrigine());
        }
        else {
            view = (LinearLayout) convertView;
        }

        return view;
    }
}
