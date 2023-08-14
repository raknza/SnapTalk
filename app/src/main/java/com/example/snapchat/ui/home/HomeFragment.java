package com.example.snapchat.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.ui.chatroom.ChatroomDialogFragment;
import com.example.snapchat.ui.dialog.ContactDialogFragment;
import com.example.snapchat.ui.dialog.OnDismissListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    HomeViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.observeChatPartners(this);

        viewModel.getOnSelectedCharPartners().observe( getViewLifecycleOwner(), chatPartner -> {
            if(chatPartner != null)
                openChatroom(chatPartner);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openChatroom(ChatPartner chatPartner) {
        ChatroomDialogFragment contactDialogFragment = new ChatroomDialogFragment(chatPartner);
        contactDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialogStyle);
        contactDialogFragment.show(getChildFragmentManager(), "chatroom_dialog");
        contactDialogFragment.setOnDismissListener(() -> viewModel.closeChatroomDialog());
    }




}