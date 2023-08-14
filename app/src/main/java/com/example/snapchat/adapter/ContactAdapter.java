package com.example.snapchat.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemContactBinding;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.ui.contact.ContactViewModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private ContactViewModel viewModel;

    public ContactAdapter(List<Contact> contacts, ContactViewModel viewModel){
        this.viewModel = viewModel;
        contactList = contacts;
    }

    public ContactAdapter(List<Contact> contacts){
        contactList = contacts;
    }
    public void updateData(List<Contact> newContacts) {
        contactList = newContacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding = ItemContactBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ItemContactBinding binding;
        ContactViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if(viewModel != null) {
                itemView.setOnClickListener(view -> {
                    int clickedPosition = getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        viewModel.selectContact(clickedPosition);
                    }
                });
            }
        }

        void bind(Contact contact) {
            binding.setContact(contact);
            Glide.with(itemView.getContext())
                    .load(contact.avatar.isEmpty()?R.drawable.avatar_icon:contact.avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.avatar);
            binding.executePendingBindings();
        }
    }
}
