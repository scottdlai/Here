package ca.bcit.here;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SessionFragment extends Fragment {

    private FragmentManager fragmentManager;
    private String[] sessionDate;
    private String[] sessionTimeStart;
    private String[] sessionTimeEnd;
    private String[] sessionRatio;

    public SessionFragment() {
        // Required empty public constructor
    }


    public static SessionFragment newInstance(String[] dates, String[] timeStarts, String[] timeEnds, String[] ratios) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("dates", dates);
        bundle.putStringArray("timeStarts", timeStarts);
        bundle.putStringArray("timeEnds", timeEnds);
        bundle.putStringArray("ratios", ratios);


        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            sessionDate = bundle.getStringArray("dates");
            sessionTimeStart = bundle.getStringArray("timeStarts");
            sessionTimeEnd = bundle.getStringArray("timeEnds");
            sessionRatio = bundle.getStringArray("ratios");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_session, container, false);
        RecyclerView sessionListRecycler = view.findViewById(R.id.sessionListRecycler);

        fragmentManager = getFragmentManager();

        readBundle(getArguments());
        SessionInfoAdapter adapter = new SessionInfoAdapter(sessionDate, sessionTimeStart, sessionTimeEnd, sessionRatio);
        sessionListRecycler.setAdapter(adapter);

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        sessionListRecycler.setLayoutManager(lm);

//        adapter.setListener(new ClassInfoAdapter.Listener() {
//
//            public void onClick(String classId) {
//
//                Bundle bundle = new Bundle();
//
//                Log.e(TAG, classId + "THIS IS CLASS ID");
//
//                bundle.putString("id", classId);
//
//                ClassInfoFragment classInfoFrag = ClassInfoFragment.newInstance(classId);
//
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack if needed
//                transaction.replace(R.id.frameLayout, classInfoFrag);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();
//            }
//
//        });

        return view;
    }
}







