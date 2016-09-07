package com.animalia.hassan.catsdailytips.activties;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animalia.hassan.catsdailytips.KenBurnsView;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.fragmentsQues.QuestionsViewPagerAdpater;
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

public class QuesFragActivity extends AppCompatActivity
        implements  GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {
    // [START define_variables]
    private static final String TAG = QuesDetailsActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private GoogleApiClient mGoogleApiClient;
    ViewPager pager;
    QuestionsViewPagerAdpater adapter;
    CharSequence Titles[]={"Top Questions","My Fav. Questions"};
    int Numboftabs =2;

    // [END define_variables]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_frag);
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

 /*       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new QuestionsViewPagerAdpater(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.container1);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {

            return;
        }else
        if(extras.containsKey("open_Fav")) { // Open Tab
            pager.setCurrentItem(1,false);
        } if(extras.containsKey("open_Daily")) {            // Open Tab
            pager.setCurrentItem(0,false);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                adapter.getItem(position);

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
/*        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {*/

        /*}*/
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
            Intent home = new Intent(QuesFragActivity.this,MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_daily_tips) {
            Intent home = new Intent(QuesFragActivity.this,TipsFragActivity.class);
            /*home.setAction("home");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_top_questions) {
            Intent home = new Intent(QuesFragActivity.this,QuesFragActivity.class);
            /*home.setAction("open_Daily");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_cat_videos) {
            Intent home = new Intent(QuesFragActivity.this,YouTubeActivityX.class);
            startActivity(home);
        } else if (id == R.id.nav_fav_tips) {
            Intent home = new Intent(QuesFragActivity.this,TipsFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        }else if (id == R.id.nav_fav_ques) {
            Intent home = new Intent(QuesFragActivity.this,QuesFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_ask_a_question) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(QuesFragActivity.this,R.style.AppCompatAlertDialogStyle);
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
                                Toast.makeText(QuesFragActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
                        public void onClick(DialogInterface dialog, int id) {    AlertDialog.Builder builderY = new AlertDialog.Builder(QuesFragActivity.this,R.style.AppCompatAlertDialogStyle);


                            TextView tv2 = new TextView(QuesFragActivity.this);
                            TextView tvx2 = new TextView(QuesFragActivity.this);
                            TextView tvp2 = new TextView(QuesFragActivity.this);

                            LinearLayout l5=new LinearLayout(QuesFragActivity.this);
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



                            AlertDialog.Builder builderY = new AlertDialog.Builder( QuesFragActivity.this  ,R.style.AppCompatAlertDialogStyle);

                            TextView tv2 = new TextView(QuesFragActivity.this);
                            TextView tvx2 = new TextView(QuesFragActivity.this);
                            TextView tvp2 = new TextView(QuesFragActivity.this);

                            LinearLayout l5=new LinearLayout(QuesFragActivity.this);
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
                                                Toast.makeText(QuesFragActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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

        }else if (id == R.id.nav_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(QuesFragActivity.this,R.style.AppCompatAlertDialogStyle);

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

/*        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


}
