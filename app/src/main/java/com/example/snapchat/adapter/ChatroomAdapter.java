package com.example.snapchat.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemChatroomBinding;
import com.example.snapchat.ui.home.HomeViewModel;
import com.example.snapchat.data.model.ChatPartner;

import java.util.List;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ChatPartnerViewHolder> {

    private List<ChatPartner> chatPartnerList;
    private HomeViewModel viewModel;

    public ChatroomAdapter(List<ChatPartner> chatPartnerList, HomeViewModel viewModel){
        this.viewModel = viewModel;
        this.chatPartnerList = chatPartnerList;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ChatPartner> newChatPartnerList) {
        chatPartnerList = newChatPartnerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatPartnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatroomBinding binding = ItemChatroomBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ChatPartnerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatPartnerViewHolder holder, int position) {
        holder.bind(chatPartnerList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatPartnerList.size();
    }

    class ChatPartnerViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatroomBinding binding;
        ChatPartnerViewHolder(ItemChatroomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if(viewModel != null) {
                itemView.setOnClickListener(view -> {
                    int clickedPosition = getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        viewModel.selectChatPartner(clickedPosition);
                    }
                });
            }
        }

        void bind(ChatPartner chatPartner) {
            binding.setCharPartner(chatPartner);
            Glide.with(itemView.getContext())
                    .load(chatPartner.contact.avatar.isEmpty()? R.drawable.avatar_icon:chatPartner.contact.avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.avatar);
            binding.executePendingBindings();
        }
    }
}


