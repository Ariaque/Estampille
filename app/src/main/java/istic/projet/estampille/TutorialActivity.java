package istic.projet.estampille;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        fm = this.getSupportFragmentManager();
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
//            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(otherActivity);
            fm.popBackStack();
            finish();
        }
    }
}
