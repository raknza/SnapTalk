package com.example.snapchat.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.databinding.DialogUserSearchBinding;
import com.example.snapchat.ui.contact.ContactViewModel;

public class UserSearchDialogFragment extends DialogFragment {

    DialogUserSearchBinding binding;
    private OnDismissListener dismissListener;
    UserSearchDialogViewModel viewModel;
    ContactViewModel contactViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogUserSearchBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(UserSearchDialogViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.searchButton.setOnClickListener(v -> viewModel.searchUser(getContext()));
        binding.addContactButton.setOnClickListener(v -> viewModel.addContact(getContext()));
        viewModel.getAddContactSuccess().observe(getViewLifecycleOwner(), success-> {
            if(success == true){
                contactViewModel.addContact(viewModel.getCreatedContact());
            }
        });

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

    public void setContactFragmentViewModel(ContactViewModel contactViewModel){
        this.contactViewModel = contactViewModel;
    }
}
