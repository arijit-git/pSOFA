package com.ray.apps.psofa.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.fragments.HomeFragment;
import com.ray.apps.psofa.fragments.PatientsViewFragment;
import com.ray.apps.psofa.fragments.RatingFragment;
import com.ray.apps.psofa.fragments.SettingsFragment;
import com.ray.apps.psofa.other.SharedPrefs;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class NavActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "pSOFAHome";
    private static String userName, userEmail;
    TextView userNameText, userEmailText, phoneNumberText;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleApiClient mGoogleApiClient;
    private String mUsername, mEmail;
    private final Context mContext = this;
    private ImageView mProfileImageView;
    private TextView btnEdit;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.design_default_color_secondary));

        setTitle("Home");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userNameText = header.findViewById(R.id.userName);
        mProfileImageView = header.findViewById(R.id.imageView);
        userEmailText = header.findViewById(R.id.userEmail);
        phoneNumberText = header.findViewById(R.id.text_phone_number);
        btnEdit = header.findViewById(R.id.btnEdit);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NavActivity.this, "Opening your profile to edit !",
                       // Toast.LENGTH_LONG).show();
                // call the activity
                startActivity(new Intent(NavActivity.this, ProfileActivity.class));
            }
        });
        // create an object of sharedPreferenceManager and get stored user data
        /*sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        String uri = sharedPrefManager.getPhoto();
        if(!uri.isEmpty() || uri != null) {
            Uri mPhotoUri = Uri.parse(uri);

            Picasso.get()
                    .load(mPhotoUri)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(mProfileImageView);
        }*/


        //Select Home by default
        navigationView.setCheckedItem(R.id.nav_home);
        Fragment fragment = new HomeFragment();
        displaySelectedFragment(fragment);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    /**
     *
     * @param id
     */
    public void displaySelectedScreen(int id){

        //initializing the fragment object which is selected

        Fragment newFragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        switch (id) {
            case R.id.nav_home:
                // default ui
                newFragment = new HomeFragment();
                //hideProgressDialog();
                displaySelectedFragment(newFragment);
                break;
            case R.id.nav_patient:
                // patient ui
                navigationView.setCheckedItem(R.id.nav_patient);
                newFragment = new PatientsViewFragment();
               // hideProgressDialog();
                displaySelectedFragment(newFragment);
                //updateUI(currentUser);
                break;
            case R.id.nav_settings:
                navigationView.setCheckedItem(R.id.nav_settings);
                newFragment = new SettingsFragment();
                displaySelectedFragment(newFragment);
                break;

            /*case R.id.nav_share:
                shareApplication();
                break;*/

            case R.id.nav_help:
                navigationView.setCheckedItem(R.id.nav_help);
                newFragment = new RatingFragment();
                displaySelectedFragment(newFragment);
                break;
            /*case R.id.nav_dash:
                newFragment = new DashboardFragment();
                //hideProgressDialog();
                displaySelectedFragment(newFragment);
                break;
*/
            case R.id.nav_logout:
                signOut();
                break;
        }

        /*//replacing the fragment
        if (newFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, newFragment);
            ft.commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    /**
     * Loads the specified fragment to the frame
     *
     * @param fragment
     */
    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment).addToBackStack("new_fragment");
        fragmentTransaction.commit();
    }

    //method to logout
    private void signOut(){
        new SharedPrefs(mContext).clear();
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(NavActivity.this, SignInActivity.class));
                        finish();
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        // get ref to auth
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateUI(currentUser);

            Log.d(TAG, "Current user : " + currentUser.getUid() + " userName :" + currentUser.getDisplayName() +
                    " Phone: " + currentUser.getPhoneNumber());
        }
        //checkConnection();
    }
    public void updateUI(FirebaseUser user){
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String phone = user.getPhoneNumber();
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();
                Log.d(TAG, "Provider : " + providerId);
                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = profile.getPhotoUrl();
                phone = profile.getPhoneNumber();
            }

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
           // Log.d(TAG, "Email Verified : " + emailVerified);
            userNameText.setText(name);
            userEmailText.setText(email);
            phoneNumberText.setText(phone);

            Picasso.get()
                    .load(photoUrl)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(mProfileImageView);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
