package anchovy.net.funlearn.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import anchovy.net.funlearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment1 extends Fragment {

    private static final String JENJANG = "jenjang";
    private static final String PELAJARAN = "pelajaran";

    public PersonalFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_fragment1, container, false);

        TextView materi = (TextView) view.findViewById(R.id.main_activity_student_personal_fragment1_materi);
        TextView latihan = (TextView) view.findViewById(R.id.main_activity_student_personal_fragment1_latihan);
        TextView latihanTime = (TextView) view.findViewById(R.id.main_activity_student_personal_fragment1_latihan_waktu);

        materi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment materiDialog = CustomDialogSelectGradePersonal.newInstance(0);
                materiDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        latihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment latihanDialog = CustomDialogSelectGradePersonal.newInstance(1);
                latihanDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        latihanTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment latihanTimeDialog = CustomDialogSelectGradePersonal.newInstance(2);
                latihanTimeDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        return view;
    }

    public static class CustomDialogSelectGradePersonal extends DialogFragment {

        private static final String TYPE = "type";

        private int type;

        public static CustomDialogSelectGradePersonal newInstance(int type) {

            Bundle args = new Bundle();
            args.putInt(TYPE, type);
            CustomDialogSelectGradePersonal fragment = new CustomDialogSelectGradePersonal();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                type = getArguments().getInt(TYPE);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_fragment_personal_grade_select, container, false);

            TextView sd = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_grade_sd);
            TextView smp = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_grade_smp);
            TextView sma = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_grade_sma);

            sd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    DialogFragment sdDialog = CustomDialogSelectSubjectPersonal.newInstance(type, 0);
                    sdDialog.show(getActivity().getSupportFragmentManager(), null);
                }
            });

            smp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment smpDialog = CustomDialogSelectSubjectPersonal.newInstance(type, 1);
                    smpDialog.show(getActivity().getSupportFragmentManager(), null);
                }
            });

            sma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment smaDialog = CustomDialogSelectSubjectPersonal.newInstance(type, 2);
                    smaDialog.show(getActivity().getSupportFragmentManager(), null);
                }
            });

            return view;
        }
    }

    public static class CustomDialogSelectSubjectPersonal extends DialogFragment {

        private static final String TYPE = "type";
        private static final String GRADE = "grade";

        private int type, grade;

        public static CustomDialogSelectSubjectPersonal newInstance(int type, int grade) {

            Bundle args = new Bundle();
            args.putInt(TYPE, type);
            args.putInt(GRADE, grade);
            CustomDialogSelectSubjectPersonal fragment = new CustomDialogSelectSubjectPersonal();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                type = getArguments().getInt(TYPE);
                grade = getArguments().getInt(GRADE);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_fragment_personal_subjects_select, container, false);

            TextView bi = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_subject_bi);
            TextView bing = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_subject_bing);
            TextView ipa = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_subject_ipa);
            TextView ips = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_subject_ips);
            TextView mat = (TextView) view.findViewById(R.id.main_activity_student_dialog_personal_select_subject_mat);

            if (grade == 2) {
                ipa.setVisibility(View.GONE);
                ips.setVisibility(View.GONE);
            }

            bi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            bing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            ipa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            ips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            mat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return view;
        }
    }
}
