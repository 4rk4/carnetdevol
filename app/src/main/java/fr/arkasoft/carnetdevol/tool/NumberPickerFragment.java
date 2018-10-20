package fr.arkasoft.carnetdevol.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import fr.arkasoft.carnetdevol.R;

public class NumberPickerFragment extends android.support.v4.app.DialogFragment {
    
    protected int  heures  = 0;
    protected int  minutes = 0;
    protected View v;
    OnValidateListener callback;
    
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            callback = ( OnValidateListener ) getActivity( ).getSupportFragmentManager( ).findFragmentById( R.id.mainFrag );
        } catch ( ClassCastException e ) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException( activity.toString( )
                                          + " must implement OnValidateListener" );
        }
    }
    
    @NonNull
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity( ) );
        // Get the layout inflater
        LayoutInflater inflater = getActivity( ).getLayoutInflater( );
        
        v = inflater.inflate( R.layout.fragment_number_picker, null );
        
        final TextView dureePoints = v.findViewById( R.id.dureePoints );
        dureePoints.setText( "0.0" );
        
        NumberPicker pointHeures = v.findViewById( R.id.dureeHeure );
        pointHeures.setMaxValue( 24 );
        pointHeures.setMinValue( 0 );
        
        pointHeures.setOnScrollListener( new NumberPicker.OnScrollListener( ) {
            
            @Override
            public void onScrollStateChange( NumberPicker numberPicker, int i ) {
                heures = numberPicker.getValue( );
                dureePoints.setText( majDureePoints( ) );
            }
        } );
        
        NumberPicker dureeMinutes = v.findViewById( R.id.dureeMinute );
        dureeMinutes.setMaxValue( 60 );
        dureeMinutes.setMinValue( 0 );
        dureeMinutes.setOnScrollListener( new NumberPicker.OnScrollListener( ) {
            
            @Override
            public void onScrollStateChange( NumberPicker numberPicker, int i ) {
                minutes = numberPicker.getValue( );
                dureePoints.setText( majDureePoints( ) );
            }
        } );
        
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView( v )
               .setTitle( R.string.frag_number_picker_title )
               // Add action buttons
               .setPositiveButton( R.string.msg_valide, new DialogInterface.OnClickListener( ) {
            
                   @Override
                   public void onClick( DialogInterface dialog, int id ) {
                       callback.onDialogPositiveClick( NumberPickerFragment.this, heures, minutes );
                   }
               } )
               .setNegativeButton( R.string.msg_cancel, new DialogInterface.OnClickListener( ) {
            
                   public void onClick( DialogInterface dialog, int id ) {
                       NumberPickerFragment.this.getDialog( ).cancel( );
                   }
               } );
        
        return builder.create( );
        
    }
    
    public String majDureePoints( ) {
        int point = minutes / 6;
        return String.valueOf( heures ) + "." + String.valueOf( point );
    }
    
    /*
    Permet de communiquer avec l'activity ou le frag appelant
     */
    public interface OnValidateListener {
        
        void onDialogPositiveClick( DialogFragment dialog, int h, int m );
        
    }
}