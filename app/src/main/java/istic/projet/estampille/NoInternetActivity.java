package istic.projet.estampille;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternetActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    //ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        //backButton = findViewById(R.id.backButton);
        //backButton.setOnClickListener(this);
    }

    /*@Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
//            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(otherActivity);
            finish();
        }
    }*/
}
