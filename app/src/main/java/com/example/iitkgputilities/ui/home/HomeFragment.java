package com.example.iitkgputilities.ui.home;

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
import com.example.iitkgputilities.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    /***********************************************************************************************
     * Load credentials.
     **********************************************************************************************/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        SharedPreferences preferences = requireContext().getSharedPreferences("iitkgp-utilities-erp", Context.MODE_PRIVATE);
        String uid = preferences.getString("uid", "");
        ((TextView)view.findViewById(R.id.uid_erp)).setText(uid);
        String pw = preferences.getString("pw", "");
        ((TextView)view.findViewById(R.id.pw_erp)).setText(pw);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}