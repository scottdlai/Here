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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Session Fragment to display a class's Session Information
 * Also contains an adapter to display data as a RecyclerView, CardView
 */
public class SessionFragment extends Fragment {

    private static final String DATES_KEY = "dates";

    private static final String TIME_START_KEY = "timeStarts";

    private static final String TIME_END_KEY = "timeEnds";

    private static final String RATIOS_KEY = "ratios";

    private static final String ABSENTEE_KEY = "absentees";

    private static final String LATE_KEY = "lates";

    private static final String ON_TIME_KEY = "onTimes";

    private FragmentManager fragmentManager;

    /** to store session data */
    private String[] sessionDate;
    private String[] sessionTimeStart;
    private String[] sessionTimeEnd;
    private String[] sessionRatio;
    private String[] sessionAbsentees;
    private String[] sessionLates;
    private String[] sessionOnTime;

    public SessionFragment() {
        // Required empty public constructor
    }


    public static SessionFragment newInstance(String[] dates,
                                              String[] timeStarts,
                                              String[] timeEnds,
                                              String[] ratios,
                                              String[] absentees,
                                              String[] lates,
                                              String[] onTime) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(DATES_KEY, dates);
        bundle.putStringArray(TIME_START_KEY, timeStarts);
        bundle.putStringArray(TIME_END_KEY, timeEnds);
        bundle.putStringArray(RATIOS_KEY, ratios);
        bundle.putStringArray(ABSENTEE_KEY, absentees);
        bundle.putStringArray(LATE_KEY, lates);
        bundle.putStringArray(ON_TIME_KEY, onTime);

        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            sessionDate = bundle.getStringArray(DATES_KEY);
            sessionTimeStart = bundle.getStringArray(TIME_START_KEY);
            sessionTimeEnd = bundle.getStringArray(TIME_END_KEY);
            sessionRatio = bundle.getStringArray(RATIOS_KEY);
            sessionAbsentees = bundle.getStringArray(ABSENTEE_KEY);
            sessionLates = bundle.getStringArray(LATE_KEY);
            sessionOnTime = bundle.getStringArray(ON_TIME_KEY);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_session, container, false);
        RecyclerView sessionListRecycler = view.findViewById(R.id.sessionListRecycler);

        fragmentManager = getFragmentManager();

        readBundle(getArguments());
        SessionInfoAdapter adapter = new SessionInfoAdapter(
                sessionDate,
                sessionTimeStart,
                sessionTimeEnd, sessionRatio, sessionAbsentees, sessionLates, sessionOnTime);
        sessionListRecycler.setAdapter(adapter);

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        sessionListRecycler.setLayoutManager(lm);


        return view;
    }
}







