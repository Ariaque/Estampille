package istic.projet.estampille;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import istic.projet.estampille.models.APICertification;
import istic.projet.estampille.models.APIDenreeAnimale;
import istic.projet.estampille.models.APIFermePartenaire;
import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.models.APILabel;
import istic.projet.estampille.models.APIVideo;
import istic.projet.estampille.utils.Constants;

/**
 * Activity for the page "En savoir plus".
 */
public class KnowMoreActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {


    private final int COLLAPSED_MAX_LINES = 5;
    private Context context;
    private APIInfosTransformateur apiInfosTransformateur;
    private String description;
    private String companyName;
    private List<APIVideo> videoUrls;
    private String websiteUrl;
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private List<APICertification> certifications;
    private List<APILabel> labels;
    private List<APIFermePartenaire> fermePartenaires;
    private List<APIDenreeAnimale> denreeAnimales;
    private APIFermePartenaire partnerItem;
    private APIVideo videoItem;
    private TextView textViewTitle;
    private TextView textViewTitleDescription;
    private TextView textViewDescription;
    private TextView texViewTitleLabels;
    private TextView textViewDialogTitle;
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
    private Button partnerWebsiteButton;
    private WrappingGridView labelsGridView;
    private FragmentManager fm;
    private ImageButton backButton;
    private ListView dialogListView;
    private Dialog dialog;
    private List<String> uris;
    private String FTP_ADDRESS;
    private String FTP_USERNAME;
    private String FTP_PASSWORD;
    private int collapsedHeight;
    private int expandedHeight;

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

    /**
     * Recover data from the received intent, instance of {@link APIInfosTransformateur}.
     */
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
        showMoreButton.setOnClickListener(this);
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

    /**
     * Choose components to display according to the values
     * that are in the received intent, instance of {@link APIInfosTransformateur}.
     */
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
            textViewDescription.setText(R.string.txt_no_description);
            showMoreButton.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(description);
            textViewDescription.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = textViewDescription.getLineCount();
                    if (lineCount < COLLAPSED_MAX_LINES) {
                        showMoreButton.setVisibility(View.GONE);
                    }
                }
            });

        }
        if (fermePartenaires == null || fermePartenaires.isEmpty()) {
            partnersButton.setVisibility(View.GONE);
        }
        if (denreeAnimales == null || denreeAnimales.isEmpty()) {
            rawMaterialsButton.setVisibility(View.GONE);
        }
        if ((certifications == null && labels == null) || certifications.isEmpty() && labels.isEmpty()) {
            texViewTitleLabels.setVisibility(View.GONE);
            labelsGridView.setVisibility(View.GONE);
        } else {
            loadLabelsCertificationsLogos();
            labelsGridView.setAdapter(new ImageAdapterGridView(this, uris));
        }
        //Faire le check pour les labels et certifications

    }

    /**
     * Loading pictures from the FTP server.
     */
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
                int returnCode = conn.getReplyCode();
                System.out.println(returnCode);
                if(returnCode == 550) {
                    conn.changeWorkingDirectory("/images/placeholder");
                }
                FTPFile[] files = conn.listFiles();
                System.out.println(conn.printWorkingDirectory());
                ftpLogout(conn);
                List<FTPFile> filesList = null;
                if (files != null && files.length != 0) {
                    filesList = new LinkedList<>(Arrays.asList(files));
                    for (FTPFile file : files) {
                        if (file.getName().equals(".") || file.getName().equals("..")) {
                            filesList.remove(file);
                        }
                    }
                }
                if (returnCode == 550) {
                    imageList.add(new SlideModel("http://" + FTP_ADDRESS + "/images/placeholder/foodorigintransp.png", ScaleTypes.CENTER_INSIDE));
                    imageSliderPictures.setBackgroundResource(R.color.FoodOriginGrey2);
                } else {
                    for (FTPFile file : filesList) {
                        System.out.println(file.getName());
                        imageList.add(new SlideModel("http://" + FTP_ADDRESS + "/images/" + idTransformateur + "/" + file.getName(), ScaleTypes.CENTER_INSIDE));

                    }
                    imageSliderPictures.setBackgroundResource(R.color.FoodOriginBlack);
                }
                imageSliderPictures.setImageList(imageList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading labels and certifications logos from the FTP server.
     */
    private void loadLabelsCertificationsLogos() {
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
                                if (file.getName().contains(label.getLibelle())) {
                                    uris.add("http://" + FTP_ADDRESS + "/images/labels_certifs/" + file.getName());
                                }
                            }
                            for (APICertification certification : certifications) {
                                if (file.getName().contains(certification.getLibelle())) {
                                    uris.add("http://" + FTP_ADDRESS + "/images/labels_certifs/" + file.getName());
                                }
                            }
                        }
                    }
                    Collections.sort(uris);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to the FTP server.
     *
     * @return
     */
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

    /**
     * Disconnect from the FTP server.
     *
     * @param conn
     */
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
            fm.popBackStack();
            finish();
        } else if (view.getId() == R.id.buttonSeeMore) {
            if (this.textViewDescription.getMaxLines() == COLLAPSED_MAX_LINES) {
                this.expandTextView();
                this.showMoreButton.setText(R.string.txt_see_less);
            } else {
                this.collapseTextView();
                this.showMoreButton.setText(R.string.txt_see_more);
            }
        } else if (view.getId() == R.id.buttonPartners) {
            openPartnersDialog();
        } else if (view.getId() == R.id.buttonOrigins) {
            openAnimalProductsDialog();
        } else if (view.getId() == R.id.buttonPartnerWebsite) {
            openBrowserPage(partnerItem.getUrl());
        }
    }

    /**
     * Opening browser page.
     *
     * @param url
     */
    private void openBrowserPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Opening a dialog to show the transformator's videos.
     */
    private void openVideosDialog() {
        dialog = new Dialog(KnowMoreActivity.this);
        dialog.setContentView(R.layout.listview_dialog);
        dialog.setTitle(getString(R.string.txt_videos));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        textViewDialogTitle = dialog.findViewById(R.id.listViewTitle);
        dialogListView = dialog.findViewById(R.id.listView);
        dialogListView.setId(R.id.listViewVideos);
        dialogListView.setOnItemClickListener(this);
        textViewDialogTitle.setText(getString(R.string.txt_videos));
        dialogListView.setAdapter(new ListViewVideoUrlsAdapter(KnowMoreActivity.this, videoUrls));
    }

    /**
     * Opening a dialog to show the transformator's partners.
     */
    private void openPartnersDialog() {
        dialog = new Dialog(KnowMoreActivity.this);
        dialog.setContentView(R.layout.listview_dialog);
        dialog.setTitle(getString(R.string.txt_partners));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        textViewDialogTitle = dialog.findViewById(R.id.listViewTitle);
        dialogListView = dialog.findViewById(R.id.listView);
        dialogListView.setId(R.id.listViewPartners);
        dialogListView.setOnItemClickListener(this);
        textViewDialogTitle.setText(getString(R.string.txt_partners));
        dialogListView.setAdapter(new ListViewPartnersAdapter(KnowMoreActivity.this, fermePartenaires));

    }

    /**
     * Opening a dialog to show the transformator's "denrées".
     */
    private void openAnimalProductsDialog() {
        dialog = new Dialog(KnowMoreActivity.this);
        dialog.setContentView(R.layout.listview_dialog);
        dialog.setTitle(getString(R.string.txt_origins));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        textViewDialogTitle = dialog.findViewById(R.id.listViewTitle);
        dialogListView = dialog.findViewById(R.id.listView);
        dialogListView.setSelector(R.color.FoodOriginTransparent);
        textViewDialogTitle.setText(getString(R.string.txt_origins));
        dialogListView.setAdapter(new ListViewAnimalProductAdapter(KnowMoreActivity.this, denreeAnimales));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.listViewPartners) {
            partnerItem = (APIFermePartenaire) dialogListView.getItemAtPosition(position);


            if ((partnerItem.getDescription() == null || partnerItem.getDescription().isEmpty()) && (partnerItem.getUrl() == null || partnerItem.getUrl().isEmpty())) {
                Toast.makeText(this, R.string.txt_partner_no_info, Toast.LENGTH_SHORT).show();
            } else {
                dialog.setContentView(R.layout.partner_dialog_layout);
                TextView partnerLayoutTitleTextView = dialog.findViewById(R.id.partnerLayoutTitle);
                TextView partnerLayoutDescriptionTextView = dialog.findViewById(R.id.partnerLayoutDescription);
                partnerWebsiteButton = dialog.findViewById(R.id.buttonPartnerWebsite);
                partnerLayoutTitleTextView.setText(partnerItem.getNom());
                if (partnerItem.getDescription() == null || partnerItem.getDescription().trim().isEmpty()) {
                    partnerLayoutDescriptionTextView.setVisibility(View.GONE);
                } else {
                    partnerLayoutDescriptionTextView.setText(partnerItem.getDescription());
                }
                if (partnerItem.getUrl() == null || partnerItem.getUrl().isEmpty()) {
                    partnerWebsiteButton.setVisibility(View.GONE);
                } else {
                    partnerWebsiteButton.setOnClickListener(this);
                }

            }
        } else if (parent.getId() == R.id.listViewVideos) {
            videoItem = (APIVideo) dialogListView.getItemAtPosition(position);
            if (videoItem.getLibelle() != null && !videoItem.getLibelle().isEmpty()) {
                openBrowserPage(videoItem.getLibelle());
            }
        }
    }

    /**
     * Expand the textView which contains the description.
     */
    private void expandTextView() {
        collapsedHeight = textViewDescription.getMeasuredHeight();
        textViewDescription.setMaxLines(Integer.MAX_VALUE);
        textViewDescription.measure(View.MeasureSpec.makeMeasureSpec(textViewDescription.getMeasuredWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED));
        expandedHeight = textViewDescription.getMeasuredHeight();
        ObjectAnimator animation = ObjectAnimator.ofInt(textViewDescription, "height", collapsedHeight, expandedHeight);
        animation.setDuration(200).start();
    }

    /**
     * Collapse the textView which contains the description.
     */
    private void collapseTextView() {
        ObjectAnimator animation = ObjectAnimator.ofInt(this.textViewDescription, "height", expandedHeight, collapsedHeight);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                textViewDescription.setMaxLines(COLLAPSED_MAX_LINES);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animation.setDuration(200).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
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
                    openBrowserPage(websiteUrl);
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
                    openVideosDialog();
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
                    openBrowserPage(facebookUrl);
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
                    openBrowserPage(instagramUrl);
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
                    openBrowserPage(twitterUrl);
                }
                break;
            default:
                break;
        }
        return false;
    }

    public APIInfosTransformateur getApiInfosTransformateur() {
        return apiInfosTransformateur;
    }

    public List<APIVideo> getVideoUrls() {
        return videoUrls;
    }

    public List<APICertification> getCertifications() {
        return certifications;
    }

    public List<APILabel> getLabels() {
        return labels;
    }

    public List<APIFermePartenaire> getFermePartenaires() {
        return fermePartenaires;
    }

    public List<APIDenreeAnimale> getDenreeAnimales() {
        return denreeAnimales;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public APIVideo getVideoItem() {
        return videoItem;
    }

    public ImageButton getBackButton() {
        return backButton;
    }

    public List<String> getUris() {
        return uris;
    }
}
