package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragmentStatistic extends Fragment {


    public ProfilFragmentStatistic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profil_fragment_statistic, container, false);

        final TextView umumTotalGames = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_games_played_result);
        final TextView umumTotalWin = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_win_result);
        final TextView umumTotalLose = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_lose_result);
        final TextView umumTotalExercise = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_latihan_result);
        final TextView umumBestPVP = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_pvp_nilai_result);
        final TextView umumBestTimeTrial = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_time_trial_nilai_result);
        final TextView umumTotalLearn = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_belajar_nilai_result);
        final TextView pVPTotalGames = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_main_pvp_nilai_result);
        final TextView pVPTotalWin = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_win_pvp_result);
        final TextView pVPTotalLose = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_lose_pvp_result);
        final TextView pVPTotalAnswer = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_question_pvp_result);
        final TextView pVPTotalAnswerTrue = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_question_true_pvp_result);
        final TextView pVPTotalAnswerFalse = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_question_false_pvp_result);
        final TextView timeTrialTotalGames = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_main_timetrial_nilai_result);
        final TextView timeTrialTotalWin = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_win_timetrial_result);
        final TextView timeTrialTotalLose = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_lose_timetrial_result);
        final TextView timeTrialTotalAnswer = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_question_timetrial_result);
        final TextView timeTrialTotalAnswerTrue = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_question_true_timetrial_result);
        final TextView timeTrialTotalAnswerFalse = (TextView) view.findViewById(R.id.main_activity_profile_fragment_statistic_total_question_false_timetrial_result);

        DatabaseReference statistik = FirebaseDatabase.getInstance().getReference().child("Statistic").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        statistik.keepSynced(true);

        statistik.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalLearnNumber = Integer.parseInt(dataSnapshot.child("totalLearn").getValue().toString()) ;
                int day = totalLearnNumber/60;
                int minute = totalLearnNumber%60;
                int hour = day%24;
                day /= 24;

                String totalLearn = "";

                try {
                    totalLearn = String.format(Locale.getDefault(), getResources().getString(R.string.main_activity_student_profile_statistic_template_hasil), day, hour, minute);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int bestTimeTrial = Integer.parseInt(dataSnapshot.child("timeTrialBestScore").getValue().toString());

                String bestTimeTrialText = "";

                try {
                    bestTimeTrialText = String.format(Locale.getDefault(), getResources().getString(R.string.main_activity_student_profile_statistic_best_time_trial_template), bestTimeTrial);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                umumTotalGames.setText(dataSnapshot.child("totalGames").getValue().toString());
                umumTotalWin.setText(dataSnapshot.child("totalWin").getValue().toString());
                umumTotalLose.setText(dataSnapshot.child("totalLose").getValue().toString());
                umumTotalExercise.setText(dataSnapshot.child("totalExercise").getValue().toString());
                umumBestPVP.setText(dataSnapshot.child("pvpBestScore").getValue().toString());
                umumBestTimeTrial.setText(bestTimeTrialText);
                umumTotalLearn.setText(totalLearn);
                pVPTotalGames.setText(dataSnapshot.child("pvpTotalGames").getValue().toString());
                pVPTotalWin.setText(dataSnapshot.child("pvpTotalWin").getValue().toString());
                pVPTotalLose.setText(dataSnapshot.child("pvpTotalLose").getValue().toString());
                pVPTotalAnswer.setText(dataSnapshot.child("pvpAnswer").getValue().toString());
                pVPTotalAnswerTrue.setText(dataSnapshot.child("pvpAnswerTrue").getValue().toString());
                pVPTotalAnswerFalse.setText(dataSnapshot.child("pvpAnswerFalse").getValue().toString());
                timeTrialTotalGames.setText(dataSnapshot.child("timeTrialTotalGames").getValue().toString());
                timeTrialTotalWin.setText(dataSnapshot.child("timeTrialTotalWin").getValue().toString());
                timeTrialTotalLose.setText(dataSnapshot.child("timeTrialTotalLose").getValue().toString());
                timeTrialTotalAnswer.setText(dataSnapshot.child("timeTrialAnswer").getValue().toString());
                timeTrialTotalAnswerTrue.setText(dataSnapshot.child("timeTrialAnswerTrue").getValue().toString());
                timeTrialTotalAnswerFalse.setText(dataSnapshot.child("timeTrialAnswerFalse").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
