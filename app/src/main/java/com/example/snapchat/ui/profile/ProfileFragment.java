package com.example.snapchat.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.snapchat.utils.DataManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(this);
        binding.saveButton.setOnClickListener(v -> profileViewModel.updateUserInfo(getContext()));
        profileViewModel.modifySuccess.observe(getViewLifecycleOwner() , success->{
            if(success == true){
                binding.message.setText(R.string.profile_modify_success);
            }
        });
        DataManager.getInstance().getUserLiveData().observe(getViewLifecycleOwner(), user ->{
            if(user!= null) {
                Glide.with(binding.avatar.getContext())
                        .load(DataManager.getInstance().getUserLiveData().getValue().avatar.isEmpty() ? R.drawable.avatar_icon : DataManager.getInstance().getUserLiveData().getValue().avatar)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.avatar);
                binding.executePendingBindings();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume(){
        super.onResume();
        binding.message.setText("");
    }
}