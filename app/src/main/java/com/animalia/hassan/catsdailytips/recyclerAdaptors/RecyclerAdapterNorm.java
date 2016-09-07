package com.animalia.hassan.catsdailytips.recyclerAdaptors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.animalia.hassan.catsdailytips.database.CatsTandQHelper;
import com.animalia.hassan.catsdailytips.R;
import com.animalia.hassan.catsdailytips.activties.TipaDetailsActivity;
import com.animalia.hassan.catsdailytips.fragmentsTips.Tips;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

/**
 * Created by Hassan on 6/6/2016.
 */

public class RecyclerAdapterNorm extends RecyclerView.Adapter<RecyclerAdapterNorm.RecyclerViewHolder> {
    static ArrayList<Tips> arrayList = new ArrayList<>();
    RecyclerAdapterNorm thisAdapter = this;
    public RecyclerAdapterNorm(ArrayList<Tips> arrayList) {
        this.arrayList = arrayList;
    }
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        Tips ques = arrayList.get(position);
        final int _id = ques.get_id();
        String favValue = ques.getFavourite();
        holder.Question.setText(ques.getTipTitle());
        holder.Answer.setText(ques.getTipBody());
        holder.Comment.setText(ques.getTipComment());


        if (favValue != null && favValue.equals("true")){
            holder.Favourite.setSelected(true);
            holder.Favourite.setTag(arrayList.get(position));
        }else if (favValue != null && favValue.equals("false")) {
            holder.Favourite.setSelected(false);
            holder.Favourite.setTag(arrayList.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView Question, Answer, Comment;
        CheckBox Favourite;
        ImageButton Share;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            /*final RecyclerView mRecyclerView = null;*/
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(itemView.getContext());
            final String activityName= itemView.getContext().getClass().getSimpleName();

            Question = (TextView) itemView.findViewById(R.id.quesView);
            Answer = (TextView) itemView.findViewById(R.id.tipView);
            Comment = (TextView) itemView.findViewById(R.id.commentView);
            Favourite = (CheckBox) itemView.findViewById(R.id.favButton);
            Share = (ImageButton) itemView.findViewById(R.id.shareButton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    String question = arrayList.get(position).getTipTitle();
                    String answer = arrayList.get(position).getTipBody();
                    String favourite = arrayList.get(position).getFavourite();
                    String comment = arrayList.get(position).getTipComment();
                    int rowID = arrayList.get(position).get_id();

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, question );
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, answer);
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "TIP");
                    bundle.putString(FirebaseAnalytics.Param.VALUE, "CLICKED");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    Intent i = new Intent(v.getContext(), TipaDetailsActivity.class);
                    i.putExtra("question", question);
                    i.putExtra("answer", answer);
                    i.putExtra("favourite", favourite);
                    i.putExtra("comment", comment);
                    i.putExtra("_id",rowID);
                    i.putExtra("activityName",activityName);
                    v.getContext().startActivity(i);
                    Log.e("ADAPT TIP . . . . . .  ",activityName);

                   /* ((Activity)v.getContext()).finish();*/
                }
            });


            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String question = arrayList.get(position).getTipTitle();
                    String answer = arrayList.get(position).getTipBody();
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

            Comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
               /* context.setTheme(R.style.AppCompatAlertDialogStyle);*/
                    final int position = getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AppCompatAlertDialogStyle);
                    String comment = arrayList.get(position).getTipComment();

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
                                    Tips tips = arrayList.get(position);
                                    final String t_id = String.valueOf(tips.get_id());
                                    final int _id = tips.get_id();
                                    Log.d("Row updated....",t_id);
                                    CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(v.getContext());
                                    String commentStr = input.getText().toString();
                                    if (activityName.equals("TipsFragActivity")) {
                                        catsTandQHelper.update_Tip_byID(_id, null, null, null, commentStr);
                                        Log.d("Row Tip  true....", t_id);
                                    } else if (activityName.equals("QuesFragActivity")){
                                        catsTandQHelper.update_QUE_byID(_id, null, null, null, commentStr);
                                        Log.d("Row Ques true....", t_id);
                                    }
                                    arrayList.get(position).setTipComment(commentStr);
                                    notifyItemChanged(position);
                                }
                            });

                    AppCompatDialog alert = builder.create();

                    alert.show();
                }
            });

            Favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int position = getAdapterPosition();
                    Tips tips = arrayList.get(position);
                    final String id = String.valueOf(tips.get_id());
                    final int _id = tips.get_id();
                    Log.d("Row updated....",id);
                    CatsTandQHelper catsTandQHelper = CatsTandQHelper.getInstance(v.getContext());

                    if (v.getId() == R.id.favButton) {
                        if (Favourite.isSelected()) {
                            Favourite.setSelected(false);
                            if (activityName.equals("TipsFragActivity")) {
                                catsTandQHelper.update_Tip_byID(_id, null, null, "false", null);
                                Log.d("Row Tip false....", id);
                            } else if (activityName.equals("QuesFragActivity")){
                                catsTandQHelper.update_QUE_byID(_id, null, null, "false", null);
                                Log.d("Row Ques false....", id);
                            }
                            arrayList.get(position).setFavourite("false");
                            notifyItemChanged(position);
                            notifyDataSetChanged();

                        } else {
                            Favourite.setSelected(true);
                            if (activityName.equals("TipsFragActivity")) {
                                catsTandQHelper.update_Tip_byID(_id, null, null, "true", null);
                                Log.d("Row Tip true....", id);
                            } else if (activityName.equals("QuesFragActivity")){
                                catsTandQHelper.update_QUE_byID(_id, null, null, "true", null);
                                Log.d("Row Ques true....", id);
                            }
                            arrayList.get(position).setFavourite("true");
                            String question = arrayList.get(position).getTipTitle();
                            String answer = arrayList.get(position).getTipBody();
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, question);
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, answer);
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "TIP");
                            bundle.putString(FirebaseAnalytics.Param.VALUE, "FAVOURITED");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                            notifyItemChanged(position);
                            notifyDataSetChanged();
                        }

                    }
                }
            });

        }


    }
}
