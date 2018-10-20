package fr.arkasoft.carnetdevol.tool;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    
    // permet de communiquer avec l'activity parent
    TimePickerDialog.OnTimeSetListener callback;
    
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = ( TimePickerDialog.OnTimeSetListener ) activity;
        } catch ( ClassCastException e ) {
            throw new ClassCastException( activity.toString( )
                                          + " must implement OnHeadlineSelectedListener" );
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        // Use the current time as the default values for the picker
        final Calendar c      = Calendar.getInstance( );
        int            hour   = c.get( Calendar.HOUR_OF_DAY );
        int            minute = c.get( Calendar.MINUTE );
        
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog( getActivity( ), callback, hour, minute,
                                     DateFormat.is24HourFormat( getActivity( ) ) );
    }
}