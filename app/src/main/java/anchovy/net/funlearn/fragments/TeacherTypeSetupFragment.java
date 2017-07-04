package anchovy.net.funlearn.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import anchovy.net.funlearn.R;

public class TeacherTypeSetupFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private TeacherTypeListener mListener;
    private EditText codeInput;
    private TextInputLayout codeInputLayout;

    public TeacherTypeSetupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_type_setup, container, false);

        codeInput = (EditText)view.findViewById(R.id.login_activity_teacher_type_code_input);
        codeInput.addTextChangedListener(this);
        codeInputLayout = (TextInputLayout)view.findViewById(R.id.login_activity_teacher_type_code_input_layout);
        codeInputLayout.setErrorEnabled(true);
        codeInputLayout.setError(getResources().getString(R.string.login_activity_code_error));

        TextView schoolCode = (TextView) view.findViewById(R.id.login_activity_teacher_type_code_explanation);
        schoolCode.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TeacherTypeListener) {
            mListener = (TeacherTypeListener) context;
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

    @Override
    public void onClick(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setTitle(getResources().getString(R.string.login_activity_code_school_what))
                .setMessage(getResources().getString(R.string.login_activity_code_school_explanation))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = codeInput.getText().toString();
        if (text.length() != 10) {
            codeInputLayout.setErrorEnabled(true);
            codeInputLayout.setError(getResources().getString(R.string.login_activity_code_error));
        } else {
            codeInputLayout.setErrorEnabled(false);
        }
        mListener.teacherTypeSubmit(codeInput.getText().toString());
    }

    public interface TeacherTypeListener {
        void teacherTypeSubmit(String code);
    }
}
