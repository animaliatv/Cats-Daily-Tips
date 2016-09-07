package com.animalia.hassan.catsdailytips.youTube;

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
import com.animalia.hassan.catsdailytips.activties.MainActivity;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.activties.QuesDetailsActivity;
import com.animalia.hassan.catsdailytips.activties.QuesFragActivity;
import com.animalia.hassan.catsdailytips.fragmentsTips.Tips;
import com.animalia.hassan.catsdailytips.activties.TipsFragActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;


public class YouTubeActivityX extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = YouTubeActivityX.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private static final String[] YOUTUBE_PLAYLISTS = {
            "PLXplIEYskEs5wK3mV_jRR7LYdbBT_0WQ0"
            , "PLXplIEYskEs4GfJnfmDOEjixle2FfpoOv"
    };
    private YouTube mYoutubeDataApi;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

  if (savedInstanceState == null) {
            mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        if (ApiKey.YOUTUBE_API_KEY.startsWith("YOUR_API_KEY")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                    .setTitle("Missing API Key")
                    .setNeutralButton("Ok, I got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS))
                    .commit();
        }

    }}
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String open_Daily = "open_Daily";
        String open_Fav = "open_Fav";

        if (id == R.id.nav_home) {
            Intent home = new Intent(YouTubeActivityX.this,MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_daily_tips) {
            Intent home = new Intent(YouTubeActivityX.this,TipsFragActivity.class);
            /*home.setAction("home");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_top_questions) {
            Intent home = new Intent(YouTubeActivityX.this,QuesFragActivity.class);
            /*home.setAction("open_Daily");*/
            home.putExtra("open_Daily", open_Daily);
            startActivity(home);
        } else if (id == R.id.nav_cat_videos) {
            Intent home = new Intent(YouTubeActivityX.this,YouTubeActivityX.class);
            startActivity(home);
        } else if (id == R.id.nav_fav_tips) {
            Intent home = new Intent(YouTubeActivityX.this,TipsFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        }else if (id == R.id.nav_fav_ques) {
            Intent home = new Intent(YouTubeActivityX.this,QuesFragActivity.class);
            /*home.setAction("open_Fav");*/
            home.putExtra("open_Fav", open_Fav);
            startActivity(home);
        } else if (id == R.id.nav_ask_a_question) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(YouTubeActivityX.this,R.style.AppCompatAlertDialogStyle);
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
                                Toast.makeText(YouTubeActivityX.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
                        public void onClick(DialogInterface dialog, int id) {    AlertDialog.Builder builderY = new AlertDialog.Builder(YouTubeActivityX.this,R.style.AppCompatAlertDialogStyle);


                            TextView tv2 = new TextView(YouTubeActivityX.this);
                            TextView tvx2 = new TextView(YouTubeActivityX.this);
                            TextView tvp2 = new TextView(YouTubeActivityX.this);

                            LinearLayout l5=new LinearLayout(YouTubeActivityX.this);
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



                            AlertDialog.Builder builderY = new AlertDialog.Builder( YouTubeActivityX.this  ,R.style.AppCompatAlertDialogStyle);

                            TextView tv2 = new TextView(YouTubeActivityX.this);
                            TextView tvx2 = new TextView(YouTubeActivityX.this);
                            TextView tvp2 = new TextView(YouTubeActivityX.this);

                            LinearLayout l5=new LinearLayout(YouTubeActivityX.this);
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
                                                Toast.makeText(YouTubeActivityX.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(YouTubeActivityX.this,R.style.AppCompatAlertDialogStyle);

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
/*    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tubex);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView recyclerView =
                (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
*//*        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);*//*

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(this);
        SQLiteDatabase sqLiteDatabase = catsTandQHelper.getReadableDatabase();

        Cursor cursor = catsTandQHelper.getTipsInfo(sqLiteDatabase);
        cursor.moveToFirst();
        boolean cursorNdone;
        do {
            try {
                Tips tips = new Tips(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5));
                arrayList.add(tips);
                if (tips.equals(null)){
                    cursorNdone = false;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                cursorNdone = false;
            }

        } while (cursor.moveToNext());
        catsTandQHelper.close();
        Collections.reverse(arrayList);
        adapter = new RecyclerAdapterNorm(arrayList);
        recyclerView.setAdapter(adapter);
        if (cursorNdone = false) {
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        }





    }*/

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
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

}
