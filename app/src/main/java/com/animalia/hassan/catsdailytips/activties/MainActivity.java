package com.animalia.hassan.catsdailytips.activties;

import android.app.AlarmManager;
import android.app.Dialog;
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
import com.animalia.hassan.catsdailytips.youTube.YouTubeActivityX;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements  GoogleApiClient.OnConnectionFailedListener , NavigationView.OnNavigationItemSelectedListener {

    // [START define_variables]
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final String TAG = QuesDetailsActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    // [END define_variables]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefFirstStart = getApplicationContext().getSharedPreferences("Pref_First_Start", MODE_PRIVATE);
        int goFirst = prefFirstStart.getInt("goFirst", 0);
        if (goFirst == 0) {
            goFirst =  1;
            SharedPreferences.Editor editor = prefFirstStart.edit();
            editor.putInt("goFirst", goFirst);        // Saving integer
            editor.apply();
            Intent intent = new Intent(this, FirstStartActivity.class);
            startActivity(intent);

        }else {
        }
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

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



/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        KenBurnsView headerImage = (KenBurnsView) findViewById(R.id.back_item);
        headerImage.setResourceIds(R.drawable.world_of_cats, R.drawable.world_of_cats);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // set up alarms
        setAlarmData();
        setAlarmPref();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
/*                if (position == 0) {
                    headerImage.setImageResource(R.drawable.world_of_cats);
                } else if (position == 1)
                {
                    headerImage.setImageResource(R.drawable.world_of_cats);
                }
                else if (position == 2)
                {
                    headerImage.setImageResource(R.drawable.world_of_cats);
                }*/


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            Intent home = new Intent(MainActivity.this, MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_daily_tips) {
            Intent home = new Intent(MainActivity.this, TipsFragActivity.class);
            /*home.setAction("home");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_top_questions) {
            Intent home = new Intent(MainActivity.this, QuesFragActivity.class);
            /*home.setAction("open_Daily");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_cat_videos) {
            Intent home = new Intent(MainActivity.this, YouTubeActivityX.class);
            startActivity(home);
        } else if (id == R.id.nav_fav_tips) {
            Intent home = new Intent(MainActivity.this, TipsFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_fav_ques) {
            Intent home = new Intent(MainActivity.this, QuesFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_ask_a_question) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
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
                                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
                        public void onClick(DialogInterface dialog, int id) {    AlertDialog.Builder builderY = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);


                            TextView tv2 = new TextView(MainActivity.this);
                            TextView tvx2 = new TextView(MainActivity.this);
                            TextView tvp2 = new TextView(MainActivity.this);

                            LinearLayout l5=new LinearLayout(MainActivity.this);
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



                            AlertDialog.Builder builderY = new AlertDialog.Builder( MainActivity.this  ,R.style.AppCompatAlertDialogStyle);

                            TextView tv2 = new TextView(MainActivity.this);
                            TextView tvx2 = new TextView(MainActivity.this);
                            TextView tvp2 = new TextView(MainActivity.this);

                            LinearLayout l5=new LinearLayout(MainActivity.this);
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
                                        public void onClick(DialogInterface dialog, int id) {

                                            String contact_us_title = getString(R.string.contact_us_title);
                                            String Contact_us_subject = getString(R.string.Contact_us_subject);
                                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ask_a_question@catsdailytips.co.uk", null));
                                            intent.putExtra(Intent.EXTRA_SUBJECT, contact_us_title);
                                            intent.putExtra(Intent.EXTRA_TEXT   ,Contact_us_subject);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                                            try {
                                                startActivity(Intent.createChooser(intent, "Send mail..."));
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String open_Daily = "open_Daily";
        String open_Fav = "open_Fav";

        if (id == R.id.nav_home) {
            Intent home = new Intent(MainActivity.this, MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_daily_tips) {
            Intent home = new Intent(MainActivity.this, TipsFragActivity.class);
            /*home.setAction("home");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_top_questions) {
            Intent home = new Intent(MainActivity.this, QuesFragActivity.class);
            /*home.setAction("open_Daily");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_cat_videos) {
            Intent home = new Intent(MainActivity.this, YouTubeActivityX.class);
            startActivity(home);
        } else if (id == R.id.nav_fav_tips) {
            Intent home = new Intent(MainActivity.this, TipsFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_fav_ques) {
            Intent home = new Intent(MainActivity.this, QuesFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_ask_a_question) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
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
                        public void onClick(DialogInterface dialog, int id) {         String emailTitle = getString(R.string.email_title);
                            String emailHeader = getString(R.string.email_header);
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ask_a_question@catsdailytips.co.uk", null));
                            intent.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
                            intent.putExtra(Intent.EXTRA_TEXT   ,emailHeader);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                            try {
                                startActivity(Intent.createChooser(intent, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
                        public void onClick(DialogInterface dialog, int id) {    AlertDialog.Builder builderY = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);


                            TextView tv2 = new TextView(MainActivity.this);
                            TextView tvx2 = new TextView(MainActivity.this);
                            TextView tvp2 = new TextView(MainActivity.this);

                            LinearLayout l5=new LinearLayout(MainActivity.this);
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



                            AlertDialog.Builder builderY = new AlertDialog.Builder( MainActivity.this  ,R.style.AppCompatAlertDialogStyle);

                            TextView tv2 = new TextView(MainActivity.this);
                            TextView tvx2 = new TextView(MainActivity.this);
                            TextView tvp2 = new TextView(MainActivity.this);

                            LinearLayout l5=new LinearLayout(MainActivity.this);
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
                                                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://catsdailytips.co.uk/"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_main, container, true);
           /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) // if the position is 0 we are returning the First tab
            {
                return new FragmentChoiceOne();

            }
            if (position == 1) // if the position is 1 we are returning the Second tab
            {
                return new FragmentChoiceTwo();
            } else {
                return new FragmentChoiceThree();
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Top Daily Tips";
                case 1:
                    return "Top Questions";
                case 2:
                    return "Cat Videos";
            }
            return null;
        }
    }


    protected void onPause() {
        super.onPause();

        unbindDrawables(findViewById(R.id.fab));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    private void setAlarmData() {


        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentA = new Intent(this, DatatAlarmReceiver.class);
        if (PendingIntent.getBroadcast(this, 0, intentA, PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intentA, 0);

            // Set the alarm to start at approximately 2:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            Log.d("reciver .....Data", " NEW is set");
        } else {
            Log.d("reciver .....Data", "is already set");
        }
    }

    private void setAlarmPref() {
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentA = new Intent(this, PrefDAlarmReceiver.class);
        if (PendingIntent.getBroadcast(this, 1, intentA, PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, intentA, 0);
            // Set the alarm to start at approximately 8:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            Log.d("reciver .....Pref", " NEW is set");
        } else {
            Log.d("reciver .....Pref", "is already set");
        }
    }

    private void unbindDrawables(View view) {
/*        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }*/
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
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

        final int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS)
        {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();
            startActivityForResult(intent, REQUEST_INVITE);

        }

        Log.e("", "Google Play Services not available: " + GoogleApiAvailability.getInstance().getErrorString(status));

        if (GoogleApiAvailability.getInstance().isUserResolvableError(status))
        {
            final Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, status,
                    PLAY_SERVICES_RESOLUTION_REQUEST);
            if (errorDialog != null)
            {
                errorDialog.show();
            }
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play service Not Available ", Toast.LENGTH_SHORT).show();
    }


}
