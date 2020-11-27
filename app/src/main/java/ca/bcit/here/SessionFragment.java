package ca.bcit.here;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Session Fragment to display a class's Session Information
 * Also contains an adapter to display data as a RecyclerView, CardView
 */
public class SessionFragment extends Fragment {

    private FragmentManager fragmentManager;

    /** to store session data */
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

        return view;
    }
}







