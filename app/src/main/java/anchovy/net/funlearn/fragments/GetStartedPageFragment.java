package anchovy.net.funlearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import anchovy.net.funlearn.LoginActivity;
import anchovy.net.funlearn.R;

public class GetStartedPageFragment extends Fragment implements View.OnClickListener {

    private static final String PAGE = "page";

    private int page;

    public GetStartedPageFragment() {
        // Required empty public constructor
    }

    public static GetStartedPageFragment newInstance(int page) {
        GetStartedPageFragment fragment = new GetStartedPageFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_started_page, container, false);

        LinearLayout dot1 = (LinearLayout)view.findViewById(R.id.get_started_activity_dot1);
        LinearLayout dot2 = (LinearLayout)view.findViewById(R.id.get_started_activity_dot2);
        LinearLayout dot3 = (LinearLayout)view.findViewById(R.id.get_started_activity_dot3);

        ImageView next1 = (ImageView)view.findViewById(R.id.get_started_activity_next1);
        ImageView next2 = (ImageView)view.findViewById(R.id.get_started_activity_next2);

        Button getStarted = (Button)view.findViewById(R.id.get_started_activity_get_started_button);
        getStarted.setOnClickListener(this);

        switch (page) {
            case 1 :
                dot1.setVisibility(View.VISIBLE);
                dot2.setVisibility(View.INVISIBLE);
                dot3.setVisibility(View.INVISIBLE);
                next1.setVisibility(View.INVISIBLE);
                next2.setVisibility(View.INVISIBLE);
                getStarted.setVisibility(View.INVISIBLE);
                break;
            case 2 :
                dot1.setVisibility(View.INVISIBLE);
                dot2.setVisibility(View.VISIBLE);
                dot3.setVisibility(View.INVISIBLE);
                next1.setVisibility(View.INVISIBLE);
                next2.setVisibility(View.INVISIBLE);
                getStarted.setVisibility(View.INVISIBLE);
                break;
            case 3 :
                dot1.setVisibility(View.INVISIBLE);
                dot2.setVisibility(View.INVISIBLE);
                dot3.setVisibility(View.VISIBLE);
                next1.setVisibility(View.VISIBLE);

                Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_next);

                next2.setVisibility(View.VISIBLE);
                next1.setAnimation(fade);

                next2.setAnimation(fade);

                getStarted.setVisibility(View.VISIBLE);
                break;
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_started_activity_get_started_button :
                if (page == 3) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    getActivity().finish();
                }
        }
    }
}
