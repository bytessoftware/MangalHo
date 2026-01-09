package com.bytes.mangalho.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytes.mangalho.Models.LoginDTO;
import com.bytes.mangalho.Models.UserDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.OneTwoOneChat;
import com.bytes.mangalho.activity.profile_other.ProfileOther;
import com.bytes.mangalho.databinding.AdapterSentInterestBinding;
import com.bytes.mangalho.fragment.SendInterest;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.sharedprefrence.SharedPrefrence;
import com.bytes.mangalho.utils.ProjectUtils;
import com.bytes.mangalho.utils.SpinnerDialog;
import com.bytes.mangalho.view.CustomButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterSentInterest extends RecyclerView.Adapter<AdapterSentInterest.MatchesHolder> {

    private Context context;
    private ArrayList<UserDTO> sentInterestList;
    private SendInterest sendInterest;
    String dob = "";
    String[] arrOfStr;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private String TAG = AdapterSentInterest.class.getSimpleName();
    private HashMap<String, String> parmsSendInterest = new HashMap<>();
    private SpinnerDialog spinnerDialog;
    int CALL_PERMISSION = 101;
    int lastPosition = -1;
    private LayoutInflater layoutInflater;

    public AdapterSentInterest(ArrayList<UserDTO> sentInterestList, SendInterest sendInterest) {
        this.sentInterestList = sentInterestList;
        this.sendInterest = sendInterest;
        this.context = sendInterest.getActivity();
        prefrence = SharedPrefrence.getInstance(context);
        loginDTO = prefrence.getLoginResponse(Consts.LOGIN_DTO);
        parms.put(Consts.USER_ID, loginDTO.getUser_id());


    }

    @NonNull
    @Override
    public MatchesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterSentInterestBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_sent_interest, parent, false);
        MatchesHolder holder = new MatchesHolder(binding);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesHolder holder, final int position) {

        holder.binding.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileOther.class);
                intent.putExtra(Consts.LOGIN_DTO, sentInterestList.get(position));
                context.startActivity(intent);

            }
        });
        if (sentInterestList.get(position).getGender().equalsIgnoreCase("Male")) {
            holder.binding.tvjoinedstatus.setText("He was joined " + ProjectUtils.changeDateFormate(sentInterestList.get(position).getCreated_at()));

        } else {
            holder.binding.tvjoinedstatus.setText("She was joined " + ProjectUtils.changeDateFormate(sentInterestList.get(position).getCreated_at()));
        }

        if (sentInterestList.get(position).getGender().equalsIgnoreCase("Male")) {
            Glide.with(context).
                    load(sentInterestList.get(position).getUser_avtar())
                    .placeholder(R.drawable.dummy_m)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivProfileImage);

        } else {
            Glide.with(context).
                    load(sentInterestList.get(position).getUser_avtar())
                    .placeholder(R.drawable.dummy_f)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivProfileImage);

        }

        try {
            dob = sentInterestList.get(position).getDob();
            arrOfStr = dob.split("-", 3);
            holder.binding.tvName.setText(sentInterestList.get(position).getName() + " (" + ProjectUtils.calculateAge(sentInterestList.get(position).getDob()) + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.tvCity.setText(sentInterestList.get(position).getCity());

        if (sentInterestList.get(position).getShortlisted() == 0) {
            holder.binding.ivShortList.setImageDrawable(sendInterest.getResources().getDrawable(R.drawable.ic_shortlist));
        } else {
            holder.binding.ivShortList.setImageDrawable(sendInterest.getResources().getDrawable(R.drawable.ic_shortlist_done));
        }
        holder.binding.llShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shortListed(holder, position, sentInterestList.get(position).getShortlisted());


            }
        });

            if (sentInterestList.get(position).getRequest() == 1) {
                holder.binding.ivInterest.setImageDrawable(sendInterest.getResources().getDrawable(R.drawable.ic_already_sent));
            } else if (sentInterestList.get(position).getRequest() == 2) {
                holder.binding.ivInterest.setImageDrawable(sendInterest.getResources().getDrawable(R.drawable.ic_send_interest));
            } else {
                holder.binding.ivInterest.setImageDrawable(sendInterest.getResources().getDrawable(R.drawable.ic_send_interest));
            }


        holder.binding.llInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInterest(holder, position);
            }
        });
        holder.binding.llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sentInterestList.get(position).getMobile2().equalsIgnoreCase("")) {
                    ProjectUtils.showToast(context, "Mobile number not available");
                } else if (prefrence.getBooleanValue(Consts.IS_SUBSCRIBE)) {
                    dialogshow(position);
                } else {
                    spinnerDialog = new SpinnerDialog(sendInterest.getActivity(), sentInterestList.get(position).getName(), sentInterestList.get(position).getUser_avtar(), R.style.DialogAnimations_SmileWindow);
                    spinnerDialog.showConatactDialog();
                }

            }

        });
        holder.binding.llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefrence.getBooleanValue(Consts.IS_SUBSCRIBE)) {
                    Intent intent = new Intent(context, OneTwoOneChat.class);
                    intent.putExtra(Consts.USER_ID, sentInterestList.get(position).getUser_id());
                    intent.putExtra(Consts.NAME, sentInterestList.get(position).getName());
                    intent.putExtra(Consts.IMAGE, sentInterestList.get(position).getUser_avtar());
                    context.startActivity(intent);
                } else {
                    spinnerDialog = new SpinnerDialog(sendInterest.getActivity(), sentInterestList.get(position).getName(), sentInterestList.get(position).getUser_avtar(), R.style.DialogAnimations_SmileWindow);
                    spinnerDialog.showConatactDialog();
                }

            }

        });
    }

    public void dialogshow(final int pos) {
        final Dialog dialog = new Dialog(context/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_call);

        CustomButton cbOk = (CustomButton) dialog.findViewById(R.id.cbOk);
        CustomButton cbCancel = (CustomButton) dialog.findViewById(R.id.cbCancel);


        dialog.show();
        dialog.setCancelable(false);
        cbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cbOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProjectUtils.hasPermissionInManifest(context, CALL_PERMISSION, Manifest.permission.CALL_PHONE)) {
                    try {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + sentInterestList.get(pos).getMobile2()));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        sendInterest.startActivity(callIntent);
                        dialog.dismiss();
                    } catch (Exception e) {
                        Log.e("Exception", "" + e);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sentInterestList.size();
    }


    public class MatchesHolder extends RecyclerView.ViewHolder {
        AdapterSentInterestBinding binding;

        public MatchesHolder(AdapterSentInterestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public void shortListed(final MatchesHolder holder, final int position, final int shortList) {
        parms.put(Consts.SHORT_LISTED_ID, sentInterestList.get(position).getUser_id());
        new HttpsRequest(Consts.SET_SHORTLISTED_API, parms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(context, msg);
                    if (shortList == 1) {
                        UserDTO userDTO = sentInterestList.get(position);
                        userDTO.setShortlisted(0);
                        sentInterestList.set(position,userDTO);
                        notifyDataSetChanged();
                    } else {
                        UserDTO userDTO = sentInterestList.get(position);
                        userDTO.setShortlisted(1);
                        sentInterestList.set(position,userDTO);
                        notifyDataSetChanged();

                    }
                } else {
                    ProjectUtils.showToast(context, msg);
                }
            }
        });
    }



    public void sendInterest(final MatchesHolder holder, int pos) {
        parmsSendInterest.put(Consts.USER_ID, loginDTO.getUser_id());
        parmsSendInterest.put(Consts.REQUESTED_ID, sentInterestList.get(pos).getUser_id());
        new HttpsRequest(Consts.SEND_INTEREST_API, parmsSendInterest, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(context, msg);
                    holder.binding.ivInterest.setImageDrawable(sendInterest.getResources().getDrawable(R.drawable.ic_already_sent));
                } else {
                    ProjectUtils.showToast(context, msg);
                }
            }
        });
    }

}
