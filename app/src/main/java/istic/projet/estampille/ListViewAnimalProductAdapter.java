package istic.projet.estampille;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import istic.projet.estampille.models.APIDenreeAnimale;

public class ListViewAnimalProductAdapter extends BaseAdapter {

    private final Context context;
    private final List<APIDenreeAnimale> denreeAnimales;
    private final LayoutInflater inflater;
    private APIDenreeAnimale currentDenreeAnimale;
    private TextView textViewDenreeType;
    private TextView textViewDenreeAnimal;
    private TextView textViewDenreeTypeInfos;
    private TextView textViewDenreeOrigin;
    private TextView textViewDenreePays;
    private TextView textViewDenreeOriginInfos;

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
        currentDenreeAnimale = this.denreeAnimales.get(position);
        if (convertView == null) {
            view = (LinearLayout) inflater.inflate(R.layout.animal_product_list_item, null, false);
            textViewDenreeType = view.findViewById(R.id.denree_type_title);
            textViewDenreeType.setText(R.string.type_denree_title);
            textViewDenreeAnimal = view.findViewById(R.id.denree_type_details);
            textViewDenreeAnimal.setText(currentDenreeAnimale.getTypeDenree().getAnimal() + ", " + currentDenreeAnimale.getTypeDenree().getEspece() + ", " + currentDenreeAnimale.getTypeDenree().getNom());
            textViewDenreeTypeInfos = view.findViewById(R.id.denree_type_infos);
            textViewDenreeTypeInfos.setText(currentDenreeAnimale.getInfosTypeDenree());
            textViewDenreeOrigin = view.findViewById(R.id.denree_origin_title);
            textViewDenreeOrigin.setText(R.string.origin_denree_title);
            textViewDenreePays = view.findViewById(R.id.denree_origine_details);
            textViewDenreePays.setText(currentDenreeAnimale.getOrigineDenree().getPays() + ", " + currentDenreeAnimale.getOrigineDenree().getRegion());
            textViewDenreeOriginInfos = view.findViewById(R.id.denree_origine_infos);
            textViewDenreeOriginInfos.setText(currentDenreeAnimale.getInfosOrigineDenree());
            chooseComponentsToDisplay();
        } else {
            view = (LinearLayout) convertView;
        }

        return view;
    }

    private void chooseComponentsToDisplay() {
        if (currentDenreeAnimale.getInfosTypeDenree() == null || currentDenreeAnimale.getInfosTypeDenree().isEmpty()) {
            textViewDenreeTypeInfos.setVisibility(View.GONE);
        }
        if (currentDenreeAnimale.getInfosOrigineDenree() == null || currentDenreeAnimale.getInfosOrigineDenree().isEmpty()) {
            textViewDenreeOriginInfos.setVisibility(View.GONE);
        }
    }
}
