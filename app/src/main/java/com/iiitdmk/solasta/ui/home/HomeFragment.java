package com.iiitdmk.solasta.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiitdmk.solasta.R;
import com.iiitdmk.solasta.ui.QRPaymentActivity;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public static String FACEBOOK_URL = "https://www.facebook.com/solastaiiitdm";
    public static String FACEBOOK_PAGE_ID = "solastaiiitdm";
    public static String CALL_ADMIN_NUMBER = "+917675816716";

    ImageView ivPaymentQR;
    ImageView ivCallAdmin;
    ImageView ivWebsite;
    ImageView ivFacebook;
    ImageView ivInstagram;
    ImageView ivSolastaMainLogo;
    Button btnCulturalReg;
    Button btnTechnicalReg;
    TextView tvAnnouncements;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ivPaymentQR = (ImageView) root.findViewById(R.id.ivPaymentQR);
        ivCallAdmin = (ImageView) root.findViewById(R.id.ivCallAdmin);
        ivWebsite = (ImageView) root.findViewById(R.id.ivWebsite);
        ivFacebook = (ImageView) root.findViewById(R.id.ivFacebook);
        ivInstagram = (ImageView) root.findViewById(R.id.ivInstagram);
        ivSolastaMainLogo = (ImageView) root.findViewById(R.id.ivSolastaMainLogo);
        btnCulturalReg = (Button) root.findViewById(R.id.btnCulturalReg);
        btnTechnicalReg = (Button) root.findViewById(R.id.btnTechnicalReg);
        tvAnnouncements = (TextView) root.findViewById(R.id.tvAnnouncements);



        ivPaymentQR.setOnClickListener(this);
        ivCallAdmin.setOnClickListener(this);
        ivWebsite.setOnClickListener(this);
        ivFacebook.setOnClickListener(this);
        ivInstagram.setOnClickListener(this);
        ivSolastaMainLogo.setOnClickListener(this);
        btnCulturalReg.setOnClickListener(this);
        btnTechnicalReg.setOnClickListener(this);

        getFirebaseTeamData();
        return root;
    }

    private void getFirebaseTeamData() {

        mFirestore.collection("announcements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("solaoss", document.getId() + " => " + document.getData());
                                String[] ann = document.getData().toString().split("announcement_main=");
//                                Toast.makeText(getContext(), ann[1], Toast.LENGTH_SHORT).show();
                                tvAnnouncements.setText(ann[1]);
                            }
                        } else {
                            Log.d("solaoss", "Error getting documents: ", task.getException());
                        }
                    }
                });

//        Toast.makeText(getContext(), announcementsList.get(0), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPaymentQR) {
            Intent myIntent = new Intent(getContext(), QRPaymentActivity.class);
            startActivity(myIntent);

        } else if (v.getId() == R.id.ivCallAdmin) {

            if(!checkPermission(Manifest.permission.CALL_PHONE, 101)) {
                checkPermission(Manifest.permission.CALL_PHONE, 101);
            }
        }


        else if (v.getId() == R.id.ivWebsite) {
            Uri webpage = Uri.parse("http://iiitk.ac.in/home");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } else if (v.getId() == R.id.ivFacebook) {
            Uri webpage = Uri.parse(getFacebookPageURL(getContext()));
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } else if (v.getId() == R.id.ivInstagram) {
            getInstagramUrlOpen("solasta_iiitdmk");
        } else if (v.getId() == R.id.ivSolastaMainLogo) {
            Uri webpage = Uri.parse("http://www.solasta.org.in/");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        }



        else if (v.getId() == R.id.btnCulturalReg) {
            Uri webpage = Uri.parse("https://forms.gle/1V35agZ3NK8AvVc19");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } else if (v.getId() == R.id.btnTechnicalReg) {
            Uri webpage = Uri.parse("https://forms.gle/hJ2Ct2pZUUQDUx7n9");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        }

    }


    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void getInstagramUrlOpen(String username) {
        String url = "http://instagram.com/_u/" + username;
        String web_url = "http://instagram.com/" + username;
        Uri uri = Uri.parse(url);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if (isIntentAvailable(getContext(), insta)) {
            startActivity(insta);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(web_url)));
        }
    }

 /*   private void getFacebookUrlOpen(String username) {
        String url = "http://facebook.com/_u/" + username;
        String web_url = "http://facebook.com/" + username;
        Uri uri = Uri.parse(url);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.facebook.katana");

        if (isIntentAvailable(getContext(), insta)) {
            startActivity(insta);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(web_url)));
        }
    }*/


    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
            return false;
        }
        else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + CALL_ADMIN_NUMBER));
            startActivity(intent);
            return true;
        }
    }
}