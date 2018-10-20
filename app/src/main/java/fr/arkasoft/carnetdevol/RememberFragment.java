/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RememberFragment extends Fragment {
    
    public RememberFragment( ) {
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        View     vue      = inflater.inflate( R.layout.fragment_remember, container, false );
        TextView datePn   = vue.findViewById( R.id.visitePn );
        TextView dateCert = vue.findViewById( R.id.certificatMedical );
        
        SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences( getActivity( ) );
        String            nextPn        = preferences.getString( "nextDatePn", "-" );
        String            nextCertifMed = preferences.getString( "nextDateCertif", "-" );
        
        datePn.setText( getResources( ).getString( R.string.rappel_pn ) + ": " + nextPn );
        dateCert.setText( getResources( ).getString( R.string.rappel_medical ) + ": " + nextCertifMed );
        
        return vue;
    }
    
    @SuppressWarnings( "ConstantConditions" )
    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( AppCompatActivity ) getActivity( ) ).getSupportActionBar( ).setSubtitle( R.string.act_main_drawer_racall );
        ( ( AppCompatActivity ) getActivity( ) ).getSupportActionBar( ).setTitle( R.string.nav_header_main_app_name );
    }
    
}
