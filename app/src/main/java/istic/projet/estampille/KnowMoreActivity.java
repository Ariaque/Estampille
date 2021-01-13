package istic.projet.estampille;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.projet.estampille.models.APICertification;
import istic.projet.estampille.models.APIDenreeAnimale;
import istic.projet.estampille.models.APIFermePartenaire;
import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.models.APILabel;
import istic.projet.estampille.utils.Constants;

public class KnowMoreActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    private Context context;
    private APIInfosTransformateur apiInfosTransformateur;
    private String description;
    private String companyName;
    private List<Object> videoUrls;
    private String websiteUrl;
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private List<APICertification> certifications;
    private List<APILabel> labels;
    private List<APIFermePartenaire> fermePartenaires;
    private List<APIDenreeAnimale> denreeAnimales;
    private TextView textViewTitle;
    private TextView textViewTitleDescription;
    private TextView textViewDescription;
    private TextView texViewTitleLabels;
    private TextView textViewTitleAnimalProducts;
    private ImageSlider imageSliderPictures;
    private GradientDrawable insGradientColor;
    private GradientDrawable greyHover;
    private ImageButton websiteImageButton;
    private ImageButton videoImageButton;
    private ImageButton fbImageButton;
    private ImageButton insImageButton;
    private ImageButton twImageButton;
    private Button showMoreButton;
    private Button partnersButton;
    private Button rawMaterialsButton;
    private WrappingGridView labelsGridView;
    private FragmentManager fm;
    private ImageButton backButton;
    private ListView denreesAnimalesListView;
    private List<String> uris;
    private String FTP_ADDRESS;
    private String FTP_USERNAME;
    private String FTP_PASSWORD;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);
        Intent intent = getIntent();
        fm = this.getSupportFragmentManager();
        apiInfosTransformateur = (APIInfosTransformateur) intent.getSerializableExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR);
        if (apiInfosTransformateur != null) {
            recoverInformation();
            setupComponents();
            chooseComponentsToDisplay();
            //textViewTitle.setText(Html.fromHtml(getResources().getString(R.string.name, description)));
            /*List<SlideModel> slideModels = new ArrayList<>();
            imageSliderPictures.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);*/
        }
    }

    private void recoverInformation() {
        FTP_ADDRESS = getString(R.string.ftp_address);
        FTP_USERNAME = getString(R.string.ftp_username);
        FTP_PASSWORD = getString(R.string.ftp_password);
        companyName = apiInfosTransformateur.getTransformateur().getRaisonSociale();
        websiteUrl = apiInfosTransformateur.getUrlSite();
        videoUrls = apiInfosTransformateur.getUrls();
        facebookUrl = apiInfosTransformateur.getUrlFacebook();
        instagramUrl = apiInfosTransformateur.getUrlInstagram();
        twitterUrl = apiInfosTransformateur.getUrlTwitter();
        description = apiInfosTransformateur.getDescription();
        certifications = apiInfosTransformateur.getCertifications();
        labels = apiInfosTransformateur.getLabels();
        fermePartenaires = apiInfosTransformateur.getFermesP();
        denreeAnimales = apiInfosTransformateur.getDenreesA();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupComponents() {
        backButton = findViewById(R.id.backButton);
        textViewTitle = findViewById(R.id.textViewTitle);
        imageSliderPictures = findViewById(R.id.imageSlider);
        websiteImageButton = findViewById(R.id.imageButtonWebSite);
        videoImageButton = findViewById(R.id.imageButtonVideo);
        fbImageButton = findViewById(R.id.imageButtonFb);
        insImageButton = findViewById(R.id.imageButtonIns);
        twImageButton = findViewById(R.id.imageButtonTw);
        textViewDescription = findViewById(R.id.textViewDescription);
        showMoreButton = findViewById(R.id.buttonSeeMore);
        labelsGridView = findViewById(R.id.gridViewLabels);
        partnersButton = findViewById(R.id.buttonPartners);
        rawMaterialsButton = findViewById(R.id.buttonOrigins);
        textViewTitleDescription = findViewById(R.id.textViewTitleDescription);
        texViewTitleLabels = findViewById(R.id.textViewTitleLabels);

        backButton.setOnClickListener(this);
        websiteImageButton.setOnTouchListener(this);
        websiteImageButton.setOnClickListener(this);
        videoImageButton.setOnTouchListener(this);
        videoImageButton.setOnClickListener(this);
        fbImageButton.setOnTouchListener(this);
        fbImageButton.setOnClickListener(this);
        insImageButton.setOnTouchListener(this);
        insImageButton.setOnClickListener(this);
        twImageButton.setOnTouchListener(this);
        twImageButton.setOnClickListener(this);
        partnersButton.setOnClickListener(this);
        rawMaterialsButton.setOnClickListener(this);
        websiteImageButton.setBackgroundResource(R.color.FoodOriginOrangeYellow);
        videoImageButton.setBackgroundResource(R.color.YoutubeRed);
        fbImageButton.setBackgroundResource(R.color.FacebookBlue);
        twImageButton.setBackgroundResource(R.color.TwitterBlue);
        insGradientColor = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.InstagramYellow),
                        ContextCompat.getColor(this, R.color.InstagramOrange),
                        ContextCompat.getColor(this, R.color.InstagramPink),
                        ContextCompat.getColor(this, R.color.InstagramPurple),
                        ContextCompat.getColor(this, R.color.InstagramBlue)});
        greyHover = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.FoodOriginGrey2),
                        ContextCompat.getColor(this, R.color.FoodOriginGrey2)});
        insImageButton.setBackground(insGradientColor);
        loadCompanyPicturesImageSlider();
    }

    private void chooseComponentsToDisplay() {
        if (companyName == null || companyName.isEmpty()) {
            companyName = getResources().getString(R.string.txt_company_name_placeholder);
        } else {
            textViewTitle.setText(companyName);
        }
        if (websiteUrl == null || websiteUrl.isEmpty()) {
            websiteImageButton.setVisibility(View.GONE);
        }
        if (videoUrls == null || videoUrls.isEmpty()) {
            videoImageButton.setVisibility(View.GONE);
        }
        if (facebookUrl == null || facebookUrl.isEmpty()) {
            fbImageButton.setVisibility(View.GONE);
        }
        if (instagramUrl == null || instagramUrl.isEmpty()) {
            insImageButton.setVisibility(View.GONE);
        }
        if (twitterUrl == null || twitterUrl.isEmpty()) {
            twImageButton.setVisibility(View.GONE);
        }
        if (description == null || description.isEmpty()) {
            textViewTitleDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
            showMoreButton.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(description);
            textViewDescription.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = textViewDescription.getLineCount();
                    System.out.println(lineCount);
                    if (lineCount < 5) {
                        showMoreButton.setVisibility(View.GONE);
                    }
                }
            });

        }
        if(fermePartenaires == null || fermePartenaires.isEmpty()) {
            partnersButton.setVisibility(View.GONE);
        }
        if(denreeAnimales == null || denreeAnimales.isEmpty()) {
            rawMaterialsButton.setVisibility(View.GONE);
        }
        if ((certifications == null && labels == null) || certifications.isEmpty() && labels.isEmpty()) {
            texViewTitleLabels.setVisibility(View.GONE);
            labelsGridView.setVisibility(View.GONE);
        }
        else {
            loadLabelsCerificationsLogos();
            labelsGridView.setAdapter(new ImageAdapterGridView(this, uris));
        }
        //Faire le check pour les labels et certifications

    }

    private void loadCompanyPicturesImageSlider() {
        int idTransformateur = apiInfosTransformateur.getTransformateur().getId();
        ArrayList<SlideModel> imageList = new ArrayList<>();
        FTPClient conn = null;
        try {
            conn = ftpConnexion();
            if (conn.login(FTP_USERNAME, FTP_PASSWORD)) {
                conn.enterLocalPassiveMode();
                conn.setFileType(FTP.BINARY_FILE_TYPE);
                conn.changeWorkingDirectory("/images/" + idTransformateur);
                FTPFile[] files = conn.listFiles();
                ftpLogout(conn);
                if (files != null) {
                    for (FTPFile file : files) {
                        if (!file.getName().equals(".") && !file.getName().equals("..")) {
                            imageList.add(new SlideModel("http://" + FTP_ADDRESS + "/images/" + idTransformateur + "/" + file.getName(), ScaleTypes.CENTER_INSIDE));
                        }
                    }
                } else {
                    imageList.add(new SlideModel("http://" + FTP_ADDRESS + "/images/placeholder/foodorigintransp.png", ScaleTypes.CENTER_INSIDE));
                }
                imageSliderPictures.setBackgroundResource(R.color.FoodOriginBlack);
                imageSliderPictures.setImageList(imageList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLabelsCerificationsLogos() {
        FTPClient conn = null;
        uris = new ArrayList<>();
        try {
            conn = ftpConnexion();
            if (conn.login(FTP_USERNAME, FTP_PASSWORD)) {
                conn.enterLocalPassiveMode();
                conn.setFileType(FTP.BINARY_FILE_TYPE);
                conn.changeWorkingDirectory("/images/labels_certifs");
                FTPFile[] files = conn.listFiles();
                ftpLogout(conn);
                if (files != null) {
                    for (FTPFile file : files) {
                        if (!file.getName().equals(".") && !file.getName().equals("..")) {
                            for (APILabel label : labels) {
                                if(file.getName().contains(label.getLibelle())) {
                                    uris.add("http://" + FTP_ADDRESS + "/images/labels_certifs/" + file.getName());
                                }
                            }
                            for(APICertification certification : certifications) {
                                if(file.getName().contains(certification.getLibelle())) {
                                    uris.add("http://" + FTP_ADDRESS + "/images/labels_certifs/" + file.getName());
                                    System.out.println(uris.get(uris.size() -1)); }
                            }
                        }
                    }
                    System.out.println(uris.size());
                    Collections.sort(uris);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FTPClient ftpConnexion() {
        FTPClient conn = null;
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                TrafficStats.setThreadStatsTag(2124);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                conn = new FTPClient();
                conn.connect(FTP_ADDRESS);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private void ftpLogout(FTPClient conn) {
        try {
            conn.logout();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            /*Intent otherActivity = new Intent(getApplicationContext(), DisplayMapActivity.class);
            startActivity(otherActivity);*/
            fm.popBackStack();
            finish();
        }
        else if (view.getId() == R.id.imageButtonWebSite) {
            openBrowserPage(websiteUrl);
        }
        else if (view.getId() == R.id.imageButtonVideo) {
            //openVideosDialog();
        }
        else if (view.getId() == R.id.imageButtonFb) {
            openBrowserPage(facebookUrl);
        }
        else if(view.getId() == R.id.imageButtonIns) {
            openBrowserPage(instagramUrl);
        }
        else if (view.getId() == R.id.imageButtonTw) {
            openBrowserPage(twitterUrl);
        }
        else if (view.getId() == R.id.buttonPartners) {
            //openPartnersDialog();
        }
        else if (view.getId() == R.id.buttonOrigins) {
            System.out.println(("ORIGINS"));
            openOriginsDialog();
        }
    }

    private void openBrowserPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void openOriginsDialog() {
        final Dialog dialog = new Dialog(KnowMoreActivity.this);
        dialog.setContentView(R.layout.listview_dialog);
        dialog.setTitle(getString(R.string.txt_origins));
        dialog.show();
        textViewTitleAnimalProducts = dialog.findViewById(R.id.title_animal_products);
        denreesAnimalesListView = dialog.findViewById(R.id.listAnimalProducts);
        textViewTitleAnimalProducts.setText(getString(R.string.title_animal_products));
        denreesAnimalesListView.setAdapter(new ListViewAnimalProductAdapter(KnowMoreActivity.this, denreeAnimales));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.imageButtonWebSite:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginDarkOrangeYellow))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    websiteImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginDarkOrangeYellow)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    websiteImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                }
                break;
            case R.id.imageButtonVideo:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2)), new ColorDrawable(ContextCompat.getColor(this, R.color.YoutubeRed))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    videoImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.YoutubeRed)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    videoImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                }
                break;
            case R.id.imageButtonFb:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2)), new ColorDrawable(ContextCompat.getColor(this, R.color.FacebookBlue))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    fbImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FacebookBlue)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    fbImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                }
                break;
            case R.id.imageButtonIns:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    GradientDrawable[] gradientDrawables = {greyHover, insGradientColor};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(gradientDrawables);
                    insImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    GradientDrawable[] gradientDrawables = {insGradientColor, greyHover};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(gradientDrawables);
                    insImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(100);
                }
                break;
            case R.id.imageButtonTw:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2)), new ColorDrawable(ContextCompat.getColor(this, R.color.TwitterBlue))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    twImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.TwitterBlue)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    twImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
