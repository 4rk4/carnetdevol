package fr.arkasoft.carnetdevol.tool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Objects;

import fr.arkasoft.carnetdevol.R;

public class NumberPickerFragment extends android.support.v4.app.DialogFragment {
    
    private int                heures  = 0;
    private int                minutes = 0;
    private OnValidateListener callback;
    
    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            callback = ( OnValidateListener ) Objects.requireNonNull( getActivity( ) ).getSupportFragmentManager( ).findFragmentById( R.id.mainFrag );
        } catch( ClassCastException e ) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException( context.toString( )
                                          + " must implement OnValidateListener" );
        }
    }
    
    @NonNull
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity( ) );
        // Get the layout inflater
        LayoutInflater inflater = Objects.requireNonNull( getActivity( ) ).getLayoutInflater( );
    
        View v = inflater.inflate( R.layout.fragment_number_picker, null );
        
        final TextView dureePoints = v.findViewById( R.id.dureePoints );
        dureePoints.setText( "0.0" );
        
        NumberPicker pointHeures = v.findViewById( R.id.dureeHeure );
        pointHeures.setMaxValue( 24 );
        pointHeures.setMinValue( 0 );
    
        pointHeures.setOnScrollListener( ( numberPicker, i ) -> {
            heures = numberPicker.getValue( );
            dureePoints.setText( majDureePoints( ) );
        } );
        
        NumberPicker dureeMinutes = v.findViewById( R.id.dureeMinute );
        dureeMinutes.setMaxValue( 60 );
        dureeMinutes.setMinValue( 0 );
        dureeMinutes.setOnScrollListener( ( numberPicker, i ) -> {
            minutes = numberPicker.getValue( );
            dureePoints.setText( majDureePoints( ) );
        } );
        
        builder.setView( v )
               .setTitle( R.string.frag_number_picker_title )
               // Add action buttons
               .setPositiveButton( R.string.msg_valide, ( dialog, id ) -> callback.onDialogPositiveClick( heures, minutes ) )
               .setNegativeButton( R.string.msg_cancel, ( dialog, id ) -> NumberPickerFragment.this.getDialog( ).cancel( ) );
        
        return builder.create( );
        
    }
    
    private String majDureePoints( ) {
        int point = minutes / 6;
        return String.valueOf( heures ) + "." + String.valueOf( point );
    }
    
    public interface OnValidateListener {
    
        void onDialogPositiveClick( int h, int m );
        
    }
}