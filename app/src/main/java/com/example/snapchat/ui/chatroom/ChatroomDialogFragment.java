package com.example.snapchat.ui.chatroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogChatroomBinding;
import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.provider.DefaultModule;
import com.example.snapchat.ui.dialog.OnDismissListener;

import dagger.hilt.android.EntryPointAccessors;


public class ChatroomDialogFragment extends DialogFragment {

    private DialogChatroomBinding binding;
    private ChatPartner partner;
    private OnDismissListener dismissListener;

    public ChatroomDialogFragment(ChatPartner partner){
        this.partner = partner;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ChatroomDialogViewModel chatroomViewModel =
                new ViewModelProvider(this).get(ChatroomDialogViewModel.class);

        binding = DialogChatroomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.setViewModel(chatroomViewModel);

        binding.sendButton.setOnClickListener(v -> {
            chatroomViewModel.sendMessage();
            binding.recyclerView.scrollToPosition(chatroomViewModel.getMessageAdapter().getValue().getItemCount()-1);
        });

        chatroomViewModel.getClearEditText().observe(getViewLifecycleOwner(), clear -> {
            if (clear) {
                binding.contentEditText.setText("");
            }
        });
        chatroomViewModel.setChatPartner(partner);
        binding.recyclerView.scrollToPosition(chatroomViewModel.getMessageAdapter().getValue().getItemCount()-1);
        Glide.with(binding.avatar.getContext())
                .load(partner.contact.avatar.isEmpty()? R.drawable.avatar_icon:partner.contact.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.avatar);

        chatroomViewModel.setGson(EntryPointAccessors.fromApplication(getContext(), DefaultModule.ProviderEntryPoint.class).getGSon());

        chatroomViewModel.getMessagesWithSender().observe(this, messageList -> {
            if (messageList != null) {
                chatroomViewModel.receiveMessage();
                binding.recyclerView.scrollToPosition(chatroomViewModel.getMessageAdapter().getValue().getItemCount()-1);
            }
        });

        binding.returnButton.setOnClickListener(v -> dismiss());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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