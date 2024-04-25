package com.abarrotescasavargas.convencion.FunGenerales;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.abarrotescasavargas.convencion.R;

public class CustomProgressBar {
    private ProgressBarDialogFragment progressBarDialogFragment;

    public CustomProgressBar() {
        // Constructor vacío
    }

    public void showProgressBar(Context context, String message) {
        progressBarDialogFragment = ProgressBarDialogFragment.newInstance(message);

        if (context instanceof AppCompatActivity) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            progressBarDialogFragment.show(fragmentManager, "ProgressBarDialog");
        }
    }

    public void setMessage(String message) {
        if (progressBarDialogFragment != null) {
            progressBarDialogFragment.setMessage(message);
        }
    }

    public void dismissProgressBar() {
        if (progressBarDialogFragment != null) {
            progressBarDialogFragment.dismiss();
        }
    }

    public static class ProgressBarDialogFragment extends DialogFragment {

        private ProgressBar progressBar;
        private TextView textViewMessage;
        private String message;

        public static ProgressBarDialogFragment newInstance(String message) {
            ProgressBarDialogFragment fragment = new ProgressBarDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", message);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.progressbar, null);
            progressBar = view.findViewById(R.id.barra);
            textViewMessage = view.findViewById(R.id.textProgressBar);

            if (getArguments() != null) {
                message = getArguments().getString("message");
                if (textViewMessage != null) {
                    textViewMessage.setText(message);
                }
            }

            Dialog dialog = new Dialog(requireContext(), R.style.TransparentDialog);


            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

        public void setMessage(String message) {
            this.message = message;
            if (textViewMessage != null) {
                textViewMessage.setText(message);
            }
        }
    }
}
