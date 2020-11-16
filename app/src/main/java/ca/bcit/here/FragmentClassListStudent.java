package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentClassListStudent extends Fragment {


    private String[] classNames;
    private String[] classTimes;
    private String[] classIds;

    public static FragmentClassListStudent newInstance(String[] classes, String[] times, String[] ids) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("classes",classes);
        bundle.putStringArray("times",times);
        bundle.putStringArray("ids",ids);

        FragmentClassListStudent fragment = new FragmentClassListStudent();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            classNames = bundle.getStringArray("classes");
            classTimes = bundle.getStringArray("times");
            classIds = bundle.getStringArray("ids");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_class_list_student, container, false);
        RecyclerView classListRecycler = view.findViewById(R.id.classListRecycler);

        readBundle(getArguments());
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(classNames, classTimes, classIds);
        classListRecycler.setAdapter(adapter);

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        classListRecycler.setLayoutManager(lm);
        adapter.setListener(new CaptionedImagesAdapter.Listener() {

            public void onClick(String classId) {

                Bundle bundle = new Bundle();
                bundle.putString("id", classId);

                ClassInfoFragment classDetailFrag = ClassInfoFragment.newInstance(classId);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.frameLayout, classDetailFrag);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }

        });
        return view;










    }
}