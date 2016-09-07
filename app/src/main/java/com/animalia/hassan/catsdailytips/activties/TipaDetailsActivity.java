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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
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
import com.animalia.hassan.catsdailytips.alarms.DatatAlarmReceiver;
import com.animalia.hassan.catsdailytips.alarms.PrefDAlarmReceiver;
import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.animalia.hassan.catsdailytips.HeaderImage.getRandomHeaderDrawable;

public class TipaDetailsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    // [START define_variables]
    private static final String TAG = QuesDetailsActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    KenBurnsView mHeaderPicture;
    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleApiClient mGoogleApiClient;
    // [END define_variables]

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipa_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ImageView appInvite = (ImageView) findViewById(R.id.appInvite) ;

        appInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });

        // Create an auto-managed GoogleApiClient with access to App Invites.
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


        mHeaderPicture = (KenBurnsView) findViewById(R.id.image);
        mHeaderPicture.setResourceIds(getRandomHeaderDrawable(),getRandomHeaderDrawable());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton share = (FloatingActionButton) findViewById(R.id.fab1);

        TextView questionT = (TextView) findViewById(R.id.questionT);
        TextView answert = (TextView) findViewById(R.id.answert);
        TextView comment1 = (TextView) findViewById(R.id.comment1);

        Intent intent = getIntent();
        final String question = intent.getExtras().getString("question");
        final String answer = intent.getExtras().getString("answer");
        String favourite = intent.getExtras().getString("favourite");
        final String comment = intent.getExtras().getString("comment");
        final String activityName = intent.getExtras().getString("activityName");
        getSupportActionBar().setTitle(question);
        final int rowID = intent.getExtras().getInt("_id");
        final String idr = String.valueOf(rowID);

        questionT.setText(question);
        answert.setText(answer);
        comment1.setText(comment);
        comment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               /* context.setTheme(R.style.AppCompatAlertDialogStyle);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AppCompatAlertDialogStyle);


                TextView tv = new TextView(v.getContext());
                tv.setText("Add Your Own Note");
                tv.setPadding(40, 40, 40, 40);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                final TextInputEditText input = new TextInputEditText(v.getContext());
                input.setText(comment);
                input.setTextColor(Color.WHITE);
                input.setPadding(20, 10, 20, 0);
                input.setHint("Add your own note or comment here.");
                input.setHintTextColor(Color.WHITE);
                input.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                                InputType.TYPE_TEXT_FLAG_AUTO_CORRECT |
                                InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                final TextInputLayout xyz = new TextInputLayout(v.getContext());

                xyz.setPadding(20, 10, 20, 0);
                xyz.addView(input);

                builder.setCustomTitle(tv)
                        .setView(xyz)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                            }
                        })
                        .setPositiveButton("save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Log.d("Row updated....",idr);
                                CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(v.getContext());
                                String commentStr = input.getText().toString();
                                if (activityName.equals("TipsFragActivity")) {
                                    catsTandQHelper.update_Tip_byID(rowID, null, null, null, commentStr);
                                    Log.d("Row Tip  true....", idr);
                                } else if (activityName.equals("QuesFragActivity")){
                                    catsTandQHelper.update_QUE_byID(rowID, null, null, null, commentStr);
                                    Log.d("Row Ques true....", idr);
                                }
                            }
                        });

                AppCompatDialog alert = builder.create();

                alert.show();
            }
        });
        if (favourite != null && favourite.equals("true")){
            fab.setSelected(true);
        }else {
            fab.setSelected(false);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(view.getContext());
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(view.getContext());
                if (fab.isSelected()) {
                    fab.setSelected(false);
                    if (activityName.equals("TipsFragActivity")) {
                        catsTandQHelper.update_Tip_byID(rowID, null, null, "false", null);
                        Log.d("Row Tip false....", idr);
                    } else if (activityName.equals("QuesFragActivity")){
                        catsTandQHelper.update_QUE_byID(rowID, null, null, "false", null);
                        Log.d("Row Ques false....", idr);
                    }


                } else {
                    fab.setSelected(true);
                    if (activityName.equals("TipsFragActivity")) {
                        catsTandQHelper.update_Tip_byID(rowID, null, null, "true", null);
                        Log.d("Row Tip true....", idr);
                    } else if (activityName.equals("QuesFragActivity")){
                        catsTandQHelper.update_QUE_byID(rowID, null, null, "true", null);
                        Log.d("Row Ques true....", idr);
                    };
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, question);
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, answer);
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "TIP");
                    bundle.putString(FirebaseAnalytics.Param.VALUE, "FAVOURITED");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                }
            }
        });



        Log.d(question,answer);
        Log.d("ACTIVITY CREATED ... ","TipsDET ");



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, question);
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, answer);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "TIP");
                bundle.putString(FirebaseAnalytics.Param.VALUE, "FAVOURITED");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, question);
                sendIntent.putExtra(Intent.EXTRA_TEXT, answer);
                sendIntent.setType("text/plain");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                view.getContext().startActivity(Intent.createChooser(sendIntent, "Share via"));

            }
        });

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
            Intent home = new Intent(TipaDetailsActivity.this,MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_daily_tips) {
            Intent home = new Intent(TipaDetailsActivity.this,TipsFragActivity.class);
            /*home.setAction("home");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_top_questions) {
            Intent home = new Intent(TipaDetailsActivity.this,QuesFragActivity.class);
            /*home.setAction("open_Daily");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_cat_videos) {
            Intent home = new Intent(TipaDetailsActivity.this,YouTubeActivityX.class);
            startActivity(home);
        } else if (id == R.id.nav_fav_tips) {
            Intent home = new Intent(TipaDetailsActivity.this,TipsFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        }else if (id == R.id.nav_fav_ques) {
            Intent home = new Intent(TipaDetailsActivity.this,QuesFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_ask_a_question) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(TipaDetailsActivity.this,R.style.AppCompatAlertDialogStyle);
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
                                Toast.makeText(TipaDetailsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
                        public void onClick(DialogInterface dialog, int id) {    AlertDialog.Builder builderY = new AlertDialog.Builder(TipaDetailsActivity.this,R.style.AppCompatAlertDialogStyle);


                            TextView tv2 = new TextView(TipaDetailsActivity.this);
                            TextView tvx2 = new TextView(TipaDetailsActivity.this);
                            TextView tvp2 = new TextView(TipaDetailsActivity.this);

                            LinearLayout l5=new LinearLayout(TipaDetailsActivity.this);
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



                            AlertDialog.Builder builderY = new AlertDialog.Builder( TipaDetailsActivity.this  ,R.style.AppCompatAlertDialogStyle);

                            TextView tv2 = new TextView(TipaDetailsActivity.this);
                            TextView tvx2 = new TextView(TipaDetailsActivity.this);
                            TextView tvp2 = new TextView(TipaDetailsActivity.this);

                            LinearLayout l5=new LinearLayout(TipaDetailsActivity.this);
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
                                                Toast.makeText(TipaDetailsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(TipaDetailsActivity.this,R.style.AppCompatAlertDialogStyle);

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



/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Do something on back press

            String nameACT = activityName;

            Log.e("made it . . . . . . .  ",nameACT);
 *//*           if (activityName == null) {

            }else*//*
            Log.e("made it . . . . . . .  ",nameACT);
            if (activityName.equals("QuesFragActivity")) {

                Intent intent = new Intent(this, QuesFragActivity.class);
                this.startActivity(intent);
                finish();
                Log.d("BACK Pressed .. open . ", activityName);
                Log.d("BACK Pressed ... ", "QuesFRAG ");
            } else if (activityName.equals("QuesFragActivity")) {
                Intent intent = new Intent(this, TipsFragActivity.class);
                this.startActivity(intent);
                finish();
                Log.d("BACK Pressed .. open . ", activityName);
                Log.d("BACK Pressed ... ", "TipsFRAG ");
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }*/
}
