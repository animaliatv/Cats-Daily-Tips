package com.animalia.hassan.catsdailytips.activties;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animalia.hassan.catsdailytips.KenBurnsView;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.alarms.DatatAlarmReceiver;
import com.animalia.hassan.catsdailytips.alarms.PrefDAlarmReceiver;
import com.animalia.hassan.catsdailytips.firstStart.FirstStartActivity;
import com.animalia.hassan.catsdailytips.fragmentsChoices.FragmentChoiceOne;
import com.animalia.hassan.catsdailytips.fragmentsChoices.FragmentChoiceThree;
import com.animalia.hassan.catsdailytips.fragmentsChoices.FragmentChoiceTwo;
import com.animalia.hassan.catsdailytips.fragmentsTips.FragmentTips;
import com.animalia.hassan.catsdailytips.fragmentsTips.FragmentTipsFav;
import com.animalia.hassan.catsdailytips.youTube.YouTubeActivityX;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import static com.animalia.hassan.catsdailytips.HeaderImage.getRandomHeaderDrawable;

public class TipsFragActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // [START define_variables]
    private static final String TAG = QuesDetailsActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private GoogleApiClient mGoogleApiClient;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    // [END define_variables]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_frag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView appInvite = (ImageView) findViewById(R.id.appInvite) ;

        appInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .build();

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                if (result.getStatus().isSuccess()) {
                                    // Extract information from the intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String invitationId = AppInviteReferral.getInvitationId(intent);

                                    // Because autoLaunchDeepLink = true we don't have to do anything
                                    // here, but we could set that to false and manually choose
                                    // an Activity to launch to handle the deep link here.
                                    // ...
                                }
                            }
                        });



        final KenBurnsView headerImage = (KenBurnsView)findViewById(R.id.image);
        headerImage.setResourceIds(getRandomHeaderDrawable(),getRandomHeaderDrawable());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new TipsFragActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {

            return;
        }else
        if(extras.containsKey("open_Fav")) { // Open Tab
            mViewPager.setCurrentItem(1,false);
        } if(extras.containsKey("open_Daily")) {            // Open Tab
            mViewPager.setCurrentItem(0,false);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                mSectionsPagerAdapter.getItem(position);

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    headerImage.setResourceIds(getRandomHeaderDrawable(),getRandomHeaderDrawable());


                } else
                {
                    headerImage.setResourceIds(getRandomHeaderDrawable(),getRandomHeaderDrawable());

                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


    }
    @Override
    protected void onPause() {
        super.onPause();

        /*unbindDrawables(findViewById(android.R.id.list));*/

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String open_Daily = "open_Daily";
        String open_Fav = "open_Fav";

        if (id == R.id.nav_home) {
            Intent home = new Intent(TipsFragActivity.this,MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_daily_tips) {
            Intent home = new Intent(TipsFragActivity.this,TipsFragActivity.class);
            /*home.setAction("home");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_top_questions) {
            Intent home = new Intent(TipsFragActivity.this,QuesFragActivity.class);
            /*home.setAction("open_Daily");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_cat_videos) {
            Intent home = new Intent(TipsFragActivity.this,YouTubeActivityX.class);
            startActivity(home);
        } else if (id == R.id.nav_fav_tips) {
            Intent home = new Intent(TipsFragActivity.this,TipsFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        }else if (id == R.id.nav_fav_ques) {
            Intent home = new Intent(TipsFragActivity.this,QuesFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_ask_a_question) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(TipsFragActivity.this,R.style.AppCompatAlertDialogStyle);
            TextView tv1 = new TextView(this);
            TextView tvx1 = new TextView(this);
            TextView tvp1 = new TextView(this);

            LinearLayout lll=new LinearLayout(this);
            lll.setOrientation(LinearLayout.VERTICAL);
            lll.addView(tv1);
            lll.addView(tvx1);
            lll.addView(tvp1);

            tv1.setText(R.string.ask_a_question);
            tv1.setPadding(40, 40, 40, 40);
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(40);
            tv1.setTextColor(Color.WHITE);

            tvx1.setText(R.string.ask_doggy_q);
            tv1.setTextSize(30);
            tvx1.setPadding(35, 35, 35, 3);
            tvx1.setGravity(Gravity.CENTER);
            tvx1.setTextColor(Color.WHITE);


            builder1.setView(lll)
                    .setPositiveButton("ASK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String emailTitle = getString(R.string.email_title);
                            String emailHeader = getString(R.string.email_header);
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ask_a_question@catsdailytips.co.uk", null));
                            intent.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
                            intent.putExtra(Intent.EXTRA_TEXT   ,emailHeader);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                            try {
                                startActivity(Intent.createChooser(intent, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(TipsFragActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AppCompatDialog alert = builder1.create();

            alert.show();
            return true;
        } else if (id == R.id.nav_app_invite) {
            onInviteClicked();

        } else if (id == R.id.nav_rate_app) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);

            TextView tv2 = new TextView(this);
            TextView tvx2 = new TextView(this);
            TextView tvp2 = new TextView(this);

            LinearLayout llll=new LinearLayout(this);
            llll.setOrientation(LinearLayout.VERTICAL);
            llll.addView(tv2);
            llll.addView(tvx2);
            llll.addView(tvp2);

            tv2.setText(R.string.enjoying);
            tv2.setPadding(40, 40, 40, 15);
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextSize(20);
            tv2.setTextColor(Color.WHITE);


            builder2.setView(llll)
                    .setPositiveButton("Yes, it's Great!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {    AlertDialog.Builder builderY = new AlertDialog.Builder(TipsFragActivity.this,R.style.AppCompatAlertDialogStyle);


                            TextView tv2 = new TextView(TipsFragActivity.this);
                            TextView tvx2 = new TextView(TipsFragActivity.this);
                            TextView tvp2 = new TextView(TipsFragActivity.this);

                            LinearLayout l5=new LinearLayout(TipsFragActivity.this);
                            l5.setOrientation(LinearLayout.VERTICAL);
                            l5.addView(tv2);
                            l5.addView(tvx2);
                            l5.addView(tvp2);

                            tv2.setText(R.string.apprating);
                            tv2.setPadding(40, 40, 40, 15);
                            tv2.setGravity(Gravity.CENTER);
                            tv2.setTextSize(20);
                            tv2.setTextColor(Color.WHITE);


                            builderY.setView(l5)
                                    .setPositiveButton("Ok,sure", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            String url = "https://play.google.com/store/apps/details?id=com.animalia.hassan.catsdailytips";
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    })
                                    .setNegativeButton("No,thanks", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AppCompatDialog alertY = builderY.create();

                            alertY.show();

                        }
                    })
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {



                            AlertDialog.Builder builderY = new AlertDialog.Builder( TipsFragActivity.this  ,R.style.AppCompatAlertDialogStyle);

                            TextView tv2 = new TextView(TipsFragActivity.this);
                            TextView tvx2 = new TextView(TipsFragActivity.this);
                            TextView tvp2 = new TextView(TipsFragActivity.this);

                            LinearLayout l5=new LinearLayout(TipsFragActivity.this);
                            l5.setOrientation(LinearLayout.VERTICAL);
                            l5.addView(tv2);
                            l5.addView(tvx2);
                            l5.addView(tvp2);

                            tv2.setText("Sorry to hear That!");
                            tv2.setPadding(40, 40, 40, 40);
                            tv2.setGravity(Gravity.CENTER);
                            tv2.setTextSize(25);
                            tv2.setTextColor(Color.WHITE);

                            tvx2.setText(R.string.not_happy);
                            tvx2.setTextSize(15);
                            tvx2.setPadding(35, 35, 35, 3);
                            tvx2.setGravity(Gravity.CENTER);
                            tvx2.setTextColor(Color.WHITE);

                            builderY.setView(l5)
                                    .setPositiveButton("Contact Us", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {      String contact_us_title = getString(R.string.contact_us_title);
                                            String Contact_us_subject = getString(R.string.Contact_us_subject);
                                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ask_a_question@catsdailytips.co.uk", null));
                                            intent.putExtra(Intent.EXTRA_SUBJECT, contact_us_title);
                                            intent.putExtra(Intent.EXTRA_TEXT   ,Contact_us_subject);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                                            try {
                                                startActivity(Intent.createChooser(intent, "Send mail..."));
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(TipsFragActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("No,thanks", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AppCompatDialog alertY = builderY.create();

                            alertY.show();
                        }
                    });

            AppCompatDialog alert2 = builder2.create();

            alert2.show();

        } else if (id == R.id.nav_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(TipsFragActivity.this,R.style.AppCompatAlertDialogStyle);

            TextView tv = new TextView(this);
            TextView tvx = new TextView(this);
            TextView tvp = new TextView(this);

            LinearLayout ll=new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(tv);
            ll.addView(tvx);
            ll.addView(tvp);

            tv.setText(R.string.cats_daily_tips);
            tv.setPadding(40, 40, 40, 40);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            tv.setTextColor(Color.WHITE);

            tvx.setText(R.string.about_dialog_text);
            tvx.setPadding(30, 30, 30, 30);
            tvx.setGravity(Gravity.CENTER);
            tvx.setTextSize(20);
            tvx.setTextColor(Color.WHITE);


            builder.setView(ll)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AppCompatDialog alert = builder.create();

            alert.show();
            return true;

        }


        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_tips_frag, container, true);
/*            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    FragmentTips one = new FragmentTips();
                    Log.d("Frag ONE ", " Created...");
                    return one;

                case 1:
                    FragmentTipsFav two = new FragmentTipsFav() ;
                    Log.d("Frag TWO ", " Created...");
                    return two;
                default:
                    return null;

            }




        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Daily Tips";
                case 1:
                    return "Favourite Tips";

            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

/*        unbindDrawables(findViewById(R.id.frame1));
        unbindDrawables(findViewById(R.id.frame2));
        unbindDrawables(findViewById(R.id.frame3));*/

    }
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
