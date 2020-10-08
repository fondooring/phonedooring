package com.fonekey.mainpage;
import com.fonekey.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;

public class CSearchFragment  extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }
}