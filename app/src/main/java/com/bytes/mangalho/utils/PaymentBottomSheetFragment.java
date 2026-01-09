package com.bytes.mangalho.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bytes.mangalho.Constant.PaymentModeItemClick;
import com.bytes.mangalho.R;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment;

public class PaymentBottomSheetFragment extends RoundedBottomSheetDialogFragment {
    private View rootView;
    private final PaymentModeItemClick mListener;
    public static final String TAG = "PaymentBottomSheetFragment";


    public PaymentBottomSheetFragment(PaymentModeItemClick mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.payment_mode, container, false);

        init();
        return rootView;
    }

    private void init() {
        RelativeLayout rlPhonePE = rootView.findViewById(R.id.rlPhonePE);
        rlPhonePE.setOnClickListener(v -> {
            mListener.onModeClick("p");
            dismiss();
        });
        RelativeLayout rlRazorPe = rootView.findViewById(R.id.rlRazorPe);
        rlRazorPe.setOnClickListener(v -> {
            mListener.onModeClick("r");
            dismiss();
        });

    }

}
