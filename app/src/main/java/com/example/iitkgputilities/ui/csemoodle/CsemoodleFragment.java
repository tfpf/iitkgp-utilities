package com.example.iitkgputilities.ui.csemoodle;

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

import com.example.iitkgputilities.Constants;
import com.example.iitkgputilities.MainActivity;
import com.example.iitkgputilities.R;
import com.example.iitkgputilities.databinding.FragmentCsemoodleBinding;

import org.w3c.dom.Text;

public class CsemoodleFragment extends Fragment
{
    private FragmentCsemoodleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCsemoodleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        SharedPreferences preferences = requireContext().getSharedPreferences(Constants.csemoodle, Context.MODE_PRIVATE);
        ((TextView)view.findViewById(R.id.uid_csemoodle)).setText(preferences.getString("uid", ""));
        ((TextView)view.findViewById(R.id.pw_csemoodle)).setText(preferences.getString("pw", ""));
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}
