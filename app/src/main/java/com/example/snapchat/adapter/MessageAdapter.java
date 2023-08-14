package com.example.snapchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemMessageBinding;
import com.example.myapplication.databinding.ItemMessageOtherBinding;
import com.example.snapchat.data.model.ChatPartner;
import com.example.snapchat.data.model.Message;
import com.example.snapchat.utils.DataManager;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> messageList;
    private ChatPartner chatPartner;

    public MessageAdapter(List<Message> messageList){
        super();
        this.messageList = messageList;
    }

    public void setPartner(ChatPartner chatPartner){
        this.chatPartner = chatPartner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_message) {
            return new MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new OtherMessageViewHolder(ItemMessageOtherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder realHolder = (CustomViewHolder) holder;
        realHolder.bind(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.senderId == DataManager.getInstance().getUserLiveData().getValue().id) {
            return R.layout.item_message;
        } else {
            return R.layout.item_message_other;
        }
    }

    public void updateData(List<Message> newMessages) {
        messageList = newMessages;
        notifyDataSetChanged();
    }
    public void updateData() {
        notifyItemInserted(messageList.size() - 1);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements CustomViewHolder {
        private final ItemMessageBinding binding;
        MessageViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.setMessage(message);
            if(message.content.contains("https://")){
                if( message.content.contains(".png") || message.content.contains(".jpg")){
                    Glide.with(binding.getRoot().getContext())
                            .load(message.content)
                            .into(binding.imageContent);
                    binding.imageContent.setVisibility(View.VISIBLE);
                }
                binding.executePendingBindings();
            }
        }
    }
    class OtherMessageViewHolder extends RecyclerView.ViewHolder implements CustomViewHolder {
        private final ItemMessageOtherBinding binding;
        OtherMessageViewHolder(ItemMessageOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.setMessage(message);
            Glide.with(binding.getRoot().getContext())
                    .load(chatPartner.contact.avatar.isEmpty()?R.drawable.avatar_icon:chatPartner.contact.avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.avatar);
            if( message.content.contains(".png") || message.content.contains(".jpg")){
                Glide.with(binding.getRoot().getContext())
                        .load(message.content)
                        .into(binding.imageContent);
                binding.imageContent.setVisibility(View.VISIBLE);
            }
            binding.executePendingBindings();
        }
    }


}


