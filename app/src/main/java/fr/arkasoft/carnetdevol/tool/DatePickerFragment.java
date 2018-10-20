package fr.arkasoft.carnetdevol.tool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import fr.arkasoft.carnetdevol.R;

public class DatePickerFragment extends DialogFragment {
    
    DatePickerDialog.OnDateSetListener callback;
    
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        
        try {
            callback = ( DatePickerDialog.OnDateSetListener ) getActivity( ).getSupportFragmentManager( ).findFragmentById( R.id.mainFrag );
        } catch ( ClassCastException e ) {
            throw new ClassCastException( activity.toString( )
                                          + " must implement OnDateSetListener" );
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        // Use the current date as the default date in the picker
        final Calendar c     = Calendar.getInstance( );
        int            year  = c.get( Calendar.YEAR );
        int            month = c.get( Calendar.MONTH );
        int            day   = c.get( Calendar.DAY_OF_MONTH );
        
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog( getActivity( ), callback, year, month, day );
    }
}