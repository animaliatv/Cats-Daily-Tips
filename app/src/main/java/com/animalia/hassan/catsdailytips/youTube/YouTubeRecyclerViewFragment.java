package com.animalia.hassan.catsdailytips.youTube;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.youTube.model.PlaylistVideos;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.animalia.hassan.catsdailytips.BuildConfig;
import com.animalia.hassan.catsdailytips.KenBurnsView;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistListResponse;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class YouTubeRecyclerViewFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    // the fragment initialization parameter
    private static final String ARG_YOUTUBE_PLAYLIST_IDS = "YOUTUBE_PLAYLIST_IDS";
    private static final int SPINNER_ITEM_LAYOUT_ID = android.R.layout.simple_spinner_item;
    private static final int SPINNER_ITEM_DROPDOWN_LAYOUT_ID = android.R.layout.simple_spinner_dropdown_item;

    private String[] mPlaylistIds;
    private ArrayList<String> mPlaylistTitles;
    private RecyclerView mRecyclerView;
    private PlaylistVideos mPlaylistVideos;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner mPlaylistSpinner;
    private PlaylistCardAdapter mPlaylistCardAdapter;
    private YouTube mYouTubeDataApi;
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private static final String TAG = YouTubeRecyclerViewFragment.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private GoogleApiClient mGoogleApiClient;
    // [END define_variables]

    public static YouTubeRecyclerViewFragment newInstance(YouTube youTubeDataApi, String[] playlistIds) {
        YouTubeRecyclerViewFragment fragment = new YouTubeRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_YOUTUBE_PLAYLIST_IDS, playlistIds);
        fragment.setArguments(args);
        fragment.setYouTubeDataApi(youTubeDataApi);
        return fragment;
    }

    public YouTubeRecyclerViewFragment() {
        // Required empty public constructor
    }

    public void setYouTubeDataApi(YouTube api) {
        mYouTubeDataApi = api;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mPlaylistIds = getArguments().getStringArray(ARG_YOUTUBE_PLAYLIST_IDS);
        }

        // start fetching the playlist titles
        new GetPlaylistTitlesAsyncTask(mYouTubeDataApi) {
            @Override
            protected void onPostExecute(PlaylistListResponse playlistListResponse) {
                super.onPostExecute(playlistListResponse);
                mPlaylistTitles = new ArrayList();
                for (com.google.api.services.youtube.model.Playlist playlist : playlistListResponse.getItems()) {
                    mPlaylistTitles.add(playlist.getSnippet().getTitle());
                }
                // update the spinner adapter with the titles of the playlists
                ArrayAdapter<List<String>> spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mPlaylistTitles);
                spinnerAdapter.setDropDownViewResource(SPINNER_ITEM_DROPDOWN_LAYOUT_ID);
                mPlaylistSpinner.setAdapter(spinnerAdapter);
            }
        }.execute(mPlaylistIds);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set the Picasso debug indicator only for debug builds
        Picasso.with(getActivity()).setIndicatorsEnabled(BuildConfig.DEBUG);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.youtube_fragment1, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.youtube_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        Resources resources = getResources();

        mLayoutManager = new LinearLayoutManager(getActivity());

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ImageView appInvite = (ImageView) rootView.findViewById(R.id.appInvite);
        KenBurnsView headerImage = (KenBurnsView) rootView.findViewById(R.id.back_item);
        headerImage.setResourceIds(R.drawable.videosbg, R.drawable.videosbg);


        appInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });


        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        /*activity.setTitle("Cat Videos");*/

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(AppInvite.API)
                .enableAutoManage(getActivity(), this)
                .build();

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, getActivity(), autoLaunchDeepLink)
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


        mRecyclerView.setLayoutManager(mLayoutManager);

        mPlaylistSpinner = (Spinner)rootView.findViewById(R.id.youtube_playlist_spinner);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // if we have a playlist in our retained fragment, use it to populate the UI
        if (mPlaylistVideos != null) {
            // reload the UI with the existing playlist.  No need to fetch it again
            reloadUi(mPlaylistVideos, false);
        } else {
            // otherwise create an empty playlist using the first item in the playlist id's array
            mPlaylistVideos = new PlaylistVideos(mPlaylistIds[0]);
            // and reload the UI with the selected playlist and kick off fetching the playlist content
            reloadUi(mPlaylistVideos, true);
        }

        ArrayAdapter<List<String>> spinnerAdapter;
        // if we don't have the playlist titles yet
        if (mPlaylistTitles == null || mPlaylistTitles.isEmpty()) {
            // initialize the spinner with the playlist ID's so that there's something in the UI until the GetPlaylistTitlesAsyncTask finishes
            spinnerAdapter = new ArrayAdapter(getContext(), SPINNER_ITEM_LAYOUT_ID, Arrays.asList(mPlaylistIds));
        } else {
            // otherwise use the playlist titles for the spinner
            spinnerAdapter = new ArrayAdapter(getContext(), SPINNER_ITEM_LAYOUT_ID, mPlaylistTitles);

        }

        spinnerAdapter.setDropDownViewResource(SPINNER_ITEM_DROPDOWN_LAYOUT_ID);
        mPlaylistSpinner.setAdapter(spinnerAdapter);

        // set up the onItemSelectedListener for the spinner
        mPlaylistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // reload the UI with the playlist video list of the selected playlist
                mPlaylistVideos = new PlaylistVideos(mPlaylistIds[position]);
                reloadUi(mPlaylistVideos, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void reloadUi(final PlaylistVideos playlistVideos, boolean fetchPlaylist) {
        // initialize the cards adapter
        initCardAdapter(playlistVideos);

        if (fetchPlaylist) {
            // start fetching the selected playlistVideos contents
            new GetPlaylistAsyncTask(mYouTubeDataApi) {
                @Override
                public void onPostExecute(Pair<String, List<Video>> result) {
                    handleGetPlaylistResult(playlistVideos, result);
                }
            }.execute(playlistVideos.playlistId, playlistVideos.getNextPageToken());
        }
    }

    private void initCardAdapter(final PlaylistVideos playlistVideos) {
        // create the adapter with our playlistVideos and a callback to handle when we reached the last item
        mPlaylistCardAdapter = new PlaylistCardAdapter(playlistVideos, new LastItemReachedListener() {
            @Override
            public void onLastItem(int position, String nextPageToken) {
                new GetPlaylistAsyncTask(mYouTubeDataApi) {
                    @Override
                    public void onPostExecute(Pair<String, List<Video>> result) {
                        handleGetPlaylistResult(playlistVideos, result);
                    }
                }.execute(playlistVideos.playlistId, playlistVideos.getNextPageToken());
            }
        });
        mRecyclerView.setAdapter(mPlaylistCardAdapter);
    }

    private void handleGetPlaylistResult(PlaylistVideos playlistVideos, Pair<String, List<Video>> result) {
        if (result == null) return;
        final int positionStart = playlistVideos.size();
        playlistVideos.setNextPageToken(result.first);
        playlistVideos.addAll(result.second);
        mPlaylistCardAdapter.notifyItemRangeInserted(positionStart, result.second.size());
    }

    /**
     * Interface used by the {@link PlaylistCardAdapter} to inform us that we reached the last item in the list.
     */
    public interface LastItemReachedListener {
        void onLastItem(int position, String nextPageToken);
    }



    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    private int parseInt(String text, int defaultValue) {
        if (!TextUtils.isEmpty(text)) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                // fall through
            }
        }
        return defaultValue;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog((getActivity()), 0).show();
            } else {
                String errorMessage =
                        String.format(getString(R.string.error_player), errorReason.toString());
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
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





    public class PlaylistCardAdapter extends RecyclerView.Adapter<PlaylistCardAdapter.ViewHolder> {
        private final DecimalFormat sFormatter = new DecimalFormat("#,###,###");
        private final PlaylistVideos mPlaylistVideos;
        private final YouTubeRecyclerViewFragment.LastItemReachedListener mListener;
        Context mContext;
        private static final int REQ_START_STANDALONE_PLAYER = 1;
        private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final Context mContext;
            public final TextView mTitleText;
            public final TextView mDescriptionText;
            public final ImageView mThumbnailImage;
            public final ImageView mShareIcon;
            public final TextView mShareText;
            public final TextView mDurationText;
            public final TextView mViewCountText;
            public final TextView mLikeCountText;
            public final TextView mDislikeCountText;

            public ViewHolder(View v) {
                super(v);
                mContext = v.getContext();
                mTitleText = (TextView) v.findViewById(R.id.video_title);
                mDescriptionText = (TextView) v.findViewById(R.id.video_description);
                mThumbnailImage = (ImageView) v.findViewById(R.id.video_thumbnail);
                mShareIcon = (ImageView) v.findViewById(R.id.video_share);
                mShareText = (TextView) v.findViewById(R.id.video_share_text);
                mDurationText = (TextView) v.findViewById(R.id.video_dutation_text);
                mViewCountText= (TextView) v.findViewById(R.id.video_view_count);
                mLikeCountText = (TextView) v.findViewById(R.id.video_like_count);
                mDislikeCountText = (TextView) v.findViewById(R.id.video_dislike_count);
            }
        }

        public PlaylistCardAdapter(PlaylistVideos playlistVideos, YouTubeRecyclerViewFragment.LastItemReachedListener lastItemReachedListener) {
            mPlaylistVideos = playlistVideos;
            mListener = lastItemReachedListener;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            // inflate a card layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_video_card, parent, false);
            // populate the viewholder
            //
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (mPlaylistVideos.size() == 0) {

                return;
            }

            final Video video = mPlaylistVideos.get(position);
            final VideoSnippet videoSnippet = video.getSnippet();
            final VideoContentDetails videoContentDetails = video.getContentDetails();
            final VideoStatistics videoStatistics = video.getStatistics();

            holder.mTitleText.setText(videoSnippet.getTitle());
            holder.mDescriptionText.setText(videoSnippet.getDescription());

            // load the video thumbnail image
            Picasso.with(holder.mContext)
                    .load(videoSnippet.getThumbnails().getHigh().getUrl())
                    .placeholder(R.drawable.both_in_one)
                    .into(holder.mThumbnailImage);





            // set the click listener to play the video
            holder.mThumbnailImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + item.videoId)));*/
                    boolean autoplay =true;
                    boolean lightboxMode = false;
                    int startTimeMillis = 0;

                    Intent intent = null;
                    intent = YouTubeStandalonePlayer.createVideoIntent(getActivity()
                            , DeveloperKey.DEVELOPER_KEY, video.getId(), startTimeMillis, autoplay, lightboxMode);
                    if (intent != null) {
                        if (canResolveIntent(intent)) {
                            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                        } else {
                            // Could not resolve the intent - must need to install or update the YouTube API service.
                            YouTubeInitializationResult.SERVICE_MISSING
                                    .getErrorDialog((getActivity()), REQ_RESOLVE_SERVICE_MISSING).show();
                            holder.mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getId())));
                        }
                    }

                }
            });





            // create and set the click listener for both the share icon and share text
            View.OnClickListener shareClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Watch \"" + videoSnippet.getTitle() + "\" on YouTube");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + video.getId());
                    sendIntent.setType("text/plain");
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    holder.mContext.startActivity(Intent.createChooser(sendIntent, "Share via"));
                }
            };
            holder.mShareIcon.setOnClickListener(shareClickListener);
            holder.mShareText.setOnClickListener(shareClickListener);

            // set the video duration text
            holder.mDurationText.setText(parseDuration(videoContentDetails.getDuration()));
            // set the video statistics
            holder.mViewCountText.setText(sFormatter.format(videoStatistics.getViewCount()));
            holder.mLikeCountText.setText(sFormatter.format(videoStatistics.getLikeCount()));
            holder.mDislikeCountText.setText(sFormatter.format(videoStatistics.getDislikeCount()));

            if (mListener != null) {
                // get the next playlist page if we're at the end of the current page and we have another page to get
                final String nextPageToken = mPlaylistVideos.getNextPageToken();
                if (!isEmpty(nextPageToken) && position == mPlaylistVideos.size() - 1) {
                    holder.itemView.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onLastItem(position, nextPageToken);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return mPlaylistVideos.size();
        }

        private boolean isEmpty(String s) {
            if (s == null || s.length() == 0) {
                return true;
            }
            return false;
        }

        private String parseDuration(String in) {
            boolean hasSeconds = in.indexOf('S') > 0;
            boolean hasMinutes = in.indexOf('M') > 0;

            String s;
            if (hasSeconds) {
                s = in.substring(2, in.length() - 1);
            } else {
                s = in.substring(2, in.length());
            }

            String minutes = "0";
            String seconds = "00";

            if (hasMinutes && hasSeconds) {
                String[] split = s.split("M");
                minutes = split[0];
                seconds = split[1];
            } else if (hasMinutes) {
                minutes = s.substring(0, s.indexOf('M'));
            } else if (hasSeconds) {
                seconds = s;
            }

            // pad seconds with a 0 if less than 2 digits
            if (seconds.length() == 1) {
                seconds = "0" + seconds;
            }

            return minutes + ":" + seconds;
        }
        private boolean canResolveIntent(Intent intent) {
            List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
            return resolveInfo != null && !resolveInfo.isEmpty();
        }
    }


}
