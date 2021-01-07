package istic.projet.estampille;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;

/**
 * Custom Adapter for R.layout.list_item_layout.
 */
public class HistoryAdapter extends ArrayAdapter<APITransformateur> {

    private final Context context;
    private final ArrayList<APITransformateur> transformateurs;

    public HistoryAdapter(Context context, ArrayList<APITransformateur> transformateurs) {
        super(context, 0, transformateurs);
        this.context = context;
        this.transformateurs = transformateurs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        APITransformateur transformateur = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        // Lookup view for data population
        TextView item1 = convertView.findViewById(R.id.item1);
        TextView item2 = convertView.findViewById(R.id.item2);

        // Populate the data into the template view using the data object
        assert transformateur != null;
        item1.setText(transformateur.getRaisonSociale());
        item2.setText(transformateur.getAdresse());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayMapActivity.class);
                intent.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, transformateurs.get(position));
                context.startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
