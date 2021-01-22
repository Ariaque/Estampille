package istic.projet.estampille;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import istic.projet.estampille.models.APIFermePartenaire;

/**
 * Adapter for the partner's dialog.
 */
public class ListViewPartnersAdapter extends BaseAdapter {

    private final Context context;
    private final List<APIFermePartenaire> partenaires;
    private final LayoutInflater inflater;

    /**
     * Constructor.
     *
     * @param context
     * @param partenaires list of {@link APIFermePartenaire} to display.
     */
    public ListViewPartnersAdapter(Context context, List<APIFermePartenaire> partenaires) {
        this.context = context;
        this.partenaires = partenaires;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return partenaires.size();
    }

    @Override
    public Object getItem(int position) {
        return partenaires.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;
        APIFermePartenaire result = this.partenaires.get(position);
        if (convertView == null) {
            view = (LinearLayout) inflater.inflate(R.layout.simple_dialog_listview_item, null, false);
            TextView textViewItemTitle = view.findViewById(R.id.item_title);
            textViewItemTitle.setText(result.getNom());
            TextView textViewItemText = view.findViewById(R.id.item_text);
            textViewItemText.setText(result.getDescription());
        } else {
            view = (LinearLayout) convertView;
        }

        return view;
    }
}
