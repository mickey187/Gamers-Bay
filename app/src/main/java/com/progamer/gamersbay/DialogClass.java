package com.progamer.gamersbay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class DialogClass extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private TextInputEditText phoneInput, amountInput;
    private DialogClassListener listener;
    private Spinner spinner;

    String paymentOptSelected;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog,null);
        // intialization of edit texts
        phoneInput = view.findViewById(R.id.phone_dialog_textfield);
        amountInput = view.findViewById(R.id.amount_dialog_textfield);
        spinner = view.findViewById(R.id.dialog_spinner_payment_portals);

        // this is to bring the options for the spinner which are saved in values/strings.xml/<string-array>
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.paymentOptions));
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        builder.setView(view).setTitle("TopUp")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phoneNum = phoneInput.getText().toString().trim();
                        String amount = amountInput.getText().toString().trim();

                        if(TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(amount)){
                            phoneInput.setError("Bado new");
                            return;
                        }
                        listener.applyTexts(phoneNum,amount,paymentOptSelected);

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogClassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +  "Must implement Dialog class listener");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        paymentOptSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        paymentOptSelected = "CBE";
    }

    public interface DialogClassListener{

        void applyTexts(String phonenum, String amount, String option);
    }
}
