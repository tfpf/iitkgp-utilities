package com.example.iitkgputilities.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.iitkgputilities.R;
import com.example.iitkgputilities.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    /***********************************************************************************************
     * Load credentials.
     **********************************************************************************************/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        SharedPreferences preferences = requireContext().getSharedPreferences("iitkgp-utilities-moodle", Context.MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        ((TextView)view.findViewById(R.id.uid_moodle)).setText(uid);
        String pw = preferences.getString("pw", "");
        ((TextView)view.findViewById(R.id.pw_moodle)).setText(pw);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}