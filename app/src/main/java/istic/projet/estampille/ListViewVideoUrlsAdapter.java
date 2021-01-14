package istic.projet.estampille;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.mlkit.vision.text.Text;

import java.util.List;

import istic.projet.estampille.models.APIVideo;


public class ListViewVideoUrlsAdapter extends BaseAdapter {
    private Context context;
    private List<APIVideo> videos;
    private LayoutInflater inflater;

    public ListViewVideoUrlsAdapter(Context context, List<APIVideo> videos) {
        this.context = context;
        this.videos = videos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view;
        APIVideo result = this.videos.get(position);
        if(convertView == null) {
            view = (LinearLayout) inflater.inflate(R.layout.simple_dialog_listview_item, null, false);
            view.setOrientation(LinearLayout.HORIZONTAL);
            ImageView playButton = view.findViewById(R.id.play_button);
            playButton.setVisibility(View.VISIBLE);
            TextView textViewItemTitle = view.findViewById(R.id.item_title);
            textViewItemTitle.setText(result.getTitre());
            TextView textViewItemText = view.findViewById(R.id.item_text);
            textViewItemText.setVisibility(View.GONE);
        }
        else {
            view = (LinearLayout) convertView;
        }

        return view;
    }
}
