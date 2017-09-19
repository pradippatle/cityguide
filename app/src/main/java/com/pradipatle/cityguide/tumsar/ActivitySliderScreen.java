package com.pradipatle.cityguide.tumsar;

        import android.Manifest;
        import android.app.Dialog;
        import android.content.ContentProviderOperation;
        import android.content.ContentProviderResult;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.graphics.drawable.Drawable;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.AccessToken;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.HttpMethod;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;
        import com.google.firebase.analytics.FirebaseAnalytics;

        import org.json.JSONObject;

        import java.util.ArrayList;

/**
 * Created by Pradip on 12-Jul-16.
 */
public class ActivitySliderScreen extends AppCompatActivity {

    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = false;
    LoginButton loginFacebook;
    CallbackManager callbackManager;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String ProfilePicUrl = "profilePicUrl";
    public static final String isLoggedIn = "isLoggedIn";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView skipLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_slider);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter());
        loginFacebook = (LoginButton) findViewById(R.id.login_button);
        skipLogin = (TextView)findViewById(R.id.txt_skip);
        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedpreferences.edit();
                editor.putString(isLoggedIn, "skip");
                editor.apply();
                Intent intent = new Intent(ActivitySliderScreen.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginFacebook.setReadPermissions("email");
        // Callback registration
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LoginResult", "onSuccess");
                if (ContextCompat.checkSelfPermission(ActivitySliderScreen.this, Manifest.permission.WRITE_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(ActivitySliderScreen.this, Manifest.permission.READ_PHONE_STATE) !=
                                PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ActivitySliderScreen.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    // addcontact();
                    Bundle params = new Bundle();
                    params.putString("fields", "id,name,email,gender,cover,picture.type(large)");
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                            new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse response) {
                                    if (response != null) {
                                        Log.i("Splash Screen", "Response from Facebook : " + response);
                                        try {
                                            JSONObject data = response.getJSONObject();
                                            if (data.has("picture")) {
                                                String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                Log.i("LoginResult", "profilePicUrl : " + profilePicUrl);
                                                Intent intent = new Intent(ActivitySliderScreen.this, HomeScreenActivity.class);
                                                if (data.has("name")) {
                                                    String profilename = data.getString("name");
                                                    Log.i("LoginResult", "profilename : " + profilename);
                                                    editor = sharedpreferences.edit();
                                                    editor.putString(Name, profilename);
                                                    editor.putString(isLoggedIn, "yes");
                                                    editor.putString(ProfilePicUrl, profilePicUrl);
                                                    editor.apply();
                                                }
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(ActivitySliderScreen.this, "Something went Wrong, Please try after some time", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Log.i("LoginResult", "onCancel");
                final Dialog dialog = new Dialog(ActivitySliderScreen.this, R.style.CustomDialogTheme);
                LayoutInflater layoutInflater = LayoutInflater.from(ActivitySliderScreen.this);
                View vv = layoutInflater.inflate(R.layout.dialog_login, null);
                dialog.getWindow().setBackgroundDrawableResource((android.R.color.transparent));
                dialog.setContentView(vv);
                dialog.setCanceledOnTouchOutside(true);
                final EditText password_field = (EditText) vv.findViewById(R.id.password_edt);
                vv.findViewById(R.id.alert_ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //    String password = password_field.getText().toString();
                        if (password_field.getText().toString().trim().equalsIgnoreCase("441912")) {
                            Intent intent = new Intent(ActivitySliderScreen.this, HomeScreenActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivitySliderScreen.this, "Please enter correct password", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("LoginResult", "onError");
            }
        });

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        viewPager.setPageTransformer(true, new CustomPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                View landingBGView = findViewById(R.id.landing_backgrond);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);


                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);
                landingBGView.setBackgroundColor(shades.generate());
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private void addcontact() {
        ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT)
                .build());

        // first and last names
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "Pradip")
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "Patle")
                .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "9021124687")
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, "pradip_patle2009@ymail.com")
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter() {

            this.iconResId = R.array.icons;
            this.titleArrayResId = R.array.titles;
            this.hintArrayResId = R.array.hints;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];
            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);
            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);
            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 + position);
                    setAlpha(txt_title, 1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    setAlpha(imageView, 1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 - position);
                    setAlpha(txt_title, 1 - position);

                }
                if (imageView != null) {
                    // Fade the image out
                    setAlpha(imageView, 1 - position);
                }

            }
        }
    }

    /**
     * Sets the alpha for the view. The alpha will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view  - view to which alpha to be applied.
     * @param alpha - alpha value.
     */
    private void setAlpha(View view, float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Sets the translationX for the view. The translation value will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view         - view to which alpha to be applied.
     * @param translationX - translationX value.
     */
    private void setTranslationX(View view, float translationX) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setTranslationX(translationX);
        }
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }

        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {

        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Downloads Fragment ", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            // addcontact();
            Bundle params = new Bundle();
            params.putString("fields", "id,name,email,gender,cover,picture.type(large)");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            if (response != null) {
                                Log.i("Splash Screen", "Response from Facebook : " + response);
                                try {
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {

                                        String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Log.i("LoginResult", "profilePicUrl : " + profilePicUrl);
                                        Intent intent = new Intent(ActivitySliderScreen.this, HomeScreenActivity.class);
                                        if (data.has("name")) {
                                            String profilename = data.getString("name");
                                            Log.i("LoginResult", "profilename : " + profilename);
                                            editor = sharedpreferences.edit();
                                            editor.putString(Name, profilename);
                                            editor.putString(isLoggedIn, "yes");
                                            editor.putString(ProfilePicUrl, profilePicUrl);
                                            editor.apply();
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(ActivitySliderScreen.this, "Something went Wrong, Please try after some time", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).executeAsync();

        } else {
            ActivityCompat.requestPermissions(ActivitySliderScreen.this,
                    new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
