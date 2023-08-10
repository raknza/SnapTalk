package com.example.snapchat.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogContactBinding;
import com.example.snapchat.data.model.Contact;

public class ContactDialogFragment extends DialogFragment {

    Contact contact;
    DialogContactBinding binding;
    private OnDismissListener dismissListener;

    public ContactDialogFragment(Contact contact) {
        super();
        this.contact = contact;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogContactBinding.inflate(getLayoutInflater());
        binding.setContact(contact);
        binding.setLifecycleOwner(this);
        Glide.with(binding.avatar.getContext())
                .load(R.drawable.avatar_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.avatar);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }
    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }

}
