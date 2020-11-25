package ca.bcit.here;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StudentListFragment extends Fragment {

    private FragmentManager fragmentManager;
    private String[] studentList;


    public StudentListFragment() {
        // Required empty public constructor
    }


    public static StudentListFragment newInstance(String[] studentList) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("studentList", studentList);

        StudentListFragment fragment = new StudentListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            studentList = bundle.getStringArray("studentList");

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        RecyclerView studentListRecycler = view.findViewById(R.id.studentListRecycler);

        fragmentManager = getFragmentManager();

        readBundle(getArguments());
        StudentListAdapter adapter = new StudentListAdapter(studentList);
        studentListRecycler.setAdapter(adapter);

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        studentListRecycler.setLayoutManager(lm);

        return view;
    }
}







