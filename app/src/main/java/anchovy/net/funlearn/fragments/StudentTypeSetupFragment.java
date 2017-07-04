package anchovy.net.funlearn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import anchovy.net.funlearn.R;

public class StudentTypeSetupFragment extends Fragment {

    private StudentTypeListener mListener;
    private Spinner jenjang, kelas;
    private ArrayAdapter<CharSequence> jenjangAdapter, sdAdapter, smpSmaAdapter;

    private static final String JENJANG = "jenjang";
    private static final String KELAS = "kelas";
    private int jenjangPos, kelasPos;

    public StudentTypeSetupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            jenjangPos = savedInstanceState.getInt(JENJANG);
            kelasPos = savedInstanceState.getInt(KELAS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_type_setup, container, false);

        jenjang = (Spinner)view.findViewById(R.id.login_activity_student_type_spinner1);
        kelas = (Spinner)view.findViewById(R.id.login_activity_student_type_spinner2);

        jenjangAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.school_array_account_type, R.layout.support_simple_spinner_dropdown_item);
        sdAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sd_array_account_type, R.layout.support_simple_spinner_dropdown_item);
        smpSmaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.smp_sma_array_account_type, R.layout.support_simple_spinner_dropdown_item);

        jenjangAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sdAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        smpSmaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        jenjang.setAdapter(jenjangAdapter);
        kelas.setAdapter(sdAdapter);

        if (jenjangPos != 0) {
            jenjang.setSelection(jenjangPos);
        }

        if (kelasPos != 0) {
            kelas.setSelection(kelasPos);
        }

        jenjang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = adapterView.getSelectedItem().toString();
                jenjangPos = i;
                if (data.equals("SD")) {
                    kelas.setAdapter(sdAdapter);
                    kelas.setSelection(kelasPos);
                } else {
                    kelas.setAdapter(smpSmaAdapter);
                    if (kelasPos > 2) {
                        kelas.setSelection(0);
                    } else {
                        kelas.setSelection(kelasPos);
                    }
                }

                mListener.submitData(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String data = adapterView.getItemAtPosition(0).toString();
                jenjangPos = 0;
                mListener.submitData(data);
            }
        });

        kelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = adapterView.getSelectedItem().toString();
                kelasPos = i;
                mListener.submitData(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String data = adapterView.getItemAtPosition(0).toString();
                kelasPos = 0;
                mListener.submitData(data);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StudentTypeListener) {
            mListener = (StudentTypeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface StudentTypeListener {
        void submitData(String data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(JENJANG, jenjangPos);
        outState.putInt(KELAS, kelasPos);
        super.onSaveInstanceState(outState);
    }
}
