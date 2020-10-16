package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentClassListStudent extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list_student, container, false);
    }
}