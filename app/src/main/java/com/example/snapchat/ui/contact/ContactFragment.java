package com.example.snapchat.ui.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentContactBinding;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.ui.dialog.ContactDialogFragment;
import com.example.snapchat.ui.dialog.UserSearchDialogFragment;

public class ContactFragment extends Fragment {
    private ContactViewModel contactViewModel;
    FragmentContactBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.setViewModel(contactViewModel);
        binding.setLifecycleOwner(this);

        binding.searchUserButton.setOnClickListener(v -> openUserSearchPage());

        contactViewModel.getContactList(getContext());
        contactViewModel.getOnSelectedContact().observe(getViewLifecycleOwner(), contact-> {
            if(contact != null)
                openContactPage(contact);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openContactPage(Contact contact) {
        ContactDialogFragment contactDialogFragment = new ContactDialogFragment(contact);
        contactDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle);

        contactDialogFragment.setOnDismissListener(() -> contactViewModel.closeContactDialog());
        contactDialogFragment.show(getChildFragmentManager(), "contact_dialog");
    }

    private void openUserSearchPage(){
        UserSearchDialogFragment userSearchDialogFragment = new UserSearchDialogFragment();
        userSearchDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle);
        userSearchDialogFragment.setOnDismissListener(() -> contactViewModel.closeContactDialog());
        contactViewModel.openDialog();
        userSearchDialogFragment.setContactFragmentViewModel(contactViewModel);
        userSearchDialogFragment.show(getChildFragmentManager(), "search_user_dialog");
    }
}