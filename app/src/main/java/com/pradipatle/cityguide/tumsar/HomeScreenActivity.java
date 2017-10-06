package com.pradipatle.cityguide.tumsar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pradipatle.cityguide.tumsar.AppConstants.Constant;
import com.pradipatle.cityguide.tumsar.Chat.ChatActivity;
import com.pradipatle.cityguide.tumsar.Common.CommonMethods;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button EmergencyNo, VisitPlaces, SchoolCollege, ATM;
    private ImageView LogoutApp, profilePicImageView, nav_actionlogo;
    private TextView profilenameTV;
    private String profilePicUrl, profilename;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private LinearLayout MenuChat, MenuGovSchemes, MenuFarmersHelp, MenuBusTime;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String ProfilePicUrl = "profilePicUrl";
    public static final String isLoggedIn = "isLoggedIn";
    private String loginStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        setUIL();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loginStatus = sharedpreferences.getString(isLoggedIn, null);
        profilePicUrl = sharedpreferences.getString(ProfilePicUrl, "");
        profilename = sharedpreferences.getString(Name, null);
        Log.i("HomeScreenActivity ", "profilePicUrl : " + profilePicUrl);
        EmergencyNo = (Button) findViewById(R.id.btn_emergency);
        VisitPlaces = (Button) findViewById(R.id.btn_places);
        SchoolCollege = (Button) findViewById(R.id.btn_school);
        ATM = (Button) findViewById(R.id.btn_atm);
        LogoutApp = (ImageView) findViewById(R.id.logout_iv);
        nav_actionlogo = (ImageView) findViewById(R.id.nav_actionlogo);
        MenuChat = (LinearLayout) findViewById(R.id.id_chat);
        MenuGovSchemes = (LinearLayout) findViewById(R.id.id_gov_scheme);
        MenuFarmersHelp = (LinearLayout) findViewById(R.id.ic_farmershelp);
        MenuBusTime = (LinearLayout)findViewById(R.id.id_bus_menu);
        LogoutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeScreenActivity.this);
                alertDialogBuilder
                        .setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    LoginManager.getInstance().logOut();
                                    editor = sharedpreferences.edit();
                                    editor.putString(isLoggedIn, "no");
                                    editor.apply();
                                    Intent intent = new Intent(HomeScreenActivity.this, ActivitySliderScreen.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        VisitPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ActivityPlacesToVisit.class);
                startActivity(intent);
            }
        });

        EmergencyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ActivityContactNumbers.class);
                startActivity(intent);
            }
        });
        ATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ActivityMaps.class);
                startActivity(intent);
            }
        });

        MenuBusTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, AcivityViewMore_Webview.class);
                intent.putExtra("Action","BusTimings");
                startActivity(intent);
            }
        });

        SchoolCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ActivitySchoolsCollege.class);
                startActivity(intent);
            }
        });
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        // TextView nav_user = (TextView)hView.findViewById(R.id.nav_name);
        profilePicImageView = (ImageView) headerView.findViewById(R.id.profilePicImageView);
        profilenameTV = (TextView) headerView.findViewById(R.id.profilenametv);
        if (profilename != null && !profilename.equalsIgnoreCase("null")) {
            profilenameTV.setText("Hi, " + profilename);
        }
        if (profilePicUrl != null && !profilePicUrl.equalsIgnoreCase("")) {
            LoadingImage(profilePicUrl);
        } else {
            profilePicImageView.setImageResource(R.mipmap.ic_launcher);
        }
        nav_actionlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    drawer.openDrawer(GravityCompat.START);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MenuFarmersHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginStatus != null) {
                    if (loginStatus.equalsIgnoreCase("yes")) {
                        Intent newIntent = new Intent(HomeScreenActivity.this, ActivityFacebookFeeds.class);
                        newIntent.putExtra("page","Help");
                        startActivity(newIntent);
                    } else {
                        CommonMethods.DisplayToastLengthLong(HomeScreenActivity.this,"This feature requires Facebook Login. Please login with facebook");
                        LoginManager.getInstance().logOut();
                        editor = sharedpreferences.edit();
                        editor.putString(isLoggedIn, "no");
                        editor.apply();
                        Intent intent = new Intent(HomeScreenActivity.this, ActivitySliderScreen.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CommonMethods.DisplayToastLengthLong(HomeScreenActivity.this,"This feature requires Facebook Login. Please login with facebook");
                }
            }
        });

        MenuGovSchemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginStatus != null) {
                    if (loginStatus.equalsIgnoreCase("yes")) {
                        Intent newIntent = new Intent(HomeScreenActivity.this, ActivityFacebookFeeds.class);
                        newIntent.putExtra("page","Schemes");
                        startActivity(newIntent);
                    } else {
                        CommonMethods.DisplayToastLengthLong(HomeScreenActivity.this,"This feature requires Facebook Login. Please login with facebook");
                        LoginManager.getInstance().logOut();
                        editor = sharedpreferences.edit();
                        editor.putString(isLoggedIn, "no");
                        editor.apply();
                        Intent intent = new Intent(HomeScreenActivity.this, ActivitySliderScreen.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    CommonMethods.DisplayToastLengthLong(HomeScreenActivity.this,"This feature requires Facebook Login. Please login with facebook");
                }
            }
        });

        MenuChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent newIntent = new Intent(HomeScreenActivity.this, ChatActivity.class);
                        startActivity(newIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeScreenActivity.this);
            alertDialogBuilder
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.icon_about) {
            Intent intent = new Intent(HomeScreenActivity.this, ActivityInfo.class);
            startActivity(intent);
        } else if (id == R.id.icon_weather) {
            Intent intent = new Intent(HomeScreenActivity.this, ActivityWeatherReport.class);
            startActivity(intent);
        } else if (id == R.id.icon_facebook_feeds) {
            String facebookId = "fb://page/638908022928017";
            String urlPage = "https://www.facebook.com/pg/ilovetumsar/posts/?ref=page_internal";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
            } catch (Exception e) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(HomeScreenActivity.this, Uri.parse(urlPage));
            }
        } else if (id == R.id.nav_feedback) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (id == R.id.nav_shareapp) {
            try {
                String msg = Constant.REFER_MSG_TITLE;
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);

                Intent openInChooser = Intent.createChooser(sendIntent, "Refer this App");
                startActivity(openInChooser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_name) {
            try {
                String number = "9021124687";
                Uri uri = Uri.parse("smsto:" + number);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_mob) {
            try {
                String number = "9021124687";
                Uri uri = Uri.parse("smsto:" + number);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_email) {
            try {
                String number = "9021124687";
                Uri uri = Uri.parse("smsto:" + number);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void LoadingImage(String strImagePath) {

        Log.i("android", "image path is " + strImagePath);

        imageLoader.displayImage(strImagePath, profilePicImageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        CommonMethods.DisplayToastLengthShort(HomeScreenActivity.this,"Unable to load your profile picture");
                        break;
                    case DECODING_ERROR:
                        CommonMethods.DisplayToastLengthShort(HomeScreenActivity.this,"Unable to load your profile picture");
                        break;
                    case NETWORK_DENIED:
                        CommonMethods.DisplayToastLengthShort(HomeScreenActivity.this,"Plwase check your Internet. Unable to load your profile picture");
                        break;
                    case OUT_OF_MEMORY:
                        CommonMethods.DisplayToastLengthShort(HomeScreenActivity.this,"Due to low memory, unable to load your profile picture");
                        break;
                    case UNKNOWN:
                        CommonMethods.DisplayToastLengthShort(HomeScreenActivity.this,"Unable to load your profile picture");
                        break;
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }
        });
    }

    private void setUIL() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(HomeScreenActivity.this));
        options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.account_profile)
                .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(1500)).build();

    }


}
