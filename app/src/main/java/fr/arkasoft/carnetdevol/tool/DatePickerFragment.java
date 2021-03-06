package fr.arkasoft.carnetdevol.tool;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

import fr.arkasoft.carnetdevol.R;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener callback;


    public void onAttach ( Context context ) {

        super.onAttach ( context );

        try {
            callback = ( DatePickerDialog.OnDateSetListener ) Objects.requireNonNull ( getActivity ( ) ).getSupportFragmentManager ( ).findFragmentById ( R.id.mainFrag );
        } catch ( ClassCastException e ) {
            throw new ClassCastException ( context.toString ( )
                                           + " must implement OnDateSetListener" );
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog ( Bundle savedInstanceState ) {
        // Use the current date as the default date in the picker
        final Calendar c     = Calendar.getInstance ( );
        int            year  = c.get ( Calendar.YEAR );
        int            month = c.get ( Calendar.MONTH );
        int            day   = c.get ( Calendar.DAY_OF_MONTH );

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog ( getActivity ( ), callback, year, month, day );
    }
}