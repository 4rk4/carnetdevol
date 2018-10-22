package fr.arkasoft.carnetdevol.tool;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.arkasoft.carnetdevol.R;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    
    private static final String KEY_PREF_DATE_PN  = "prefDatePn";
    private static final String KEY_PREF_DATE_CIV = "prefDateCertificatMediacal";
    
    public SettingFragment( ) {
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.fragment_setting );
        
    }
    
    @Override
    public void onResume( ) {
        super.onResume( );
        getPreferenceScreen( ).getSharedPreferences( )
                              .registerOnSharedPreferenceChangeListener( this );
    }
    
    @Override
    public void onPause( ) {
        getPreferenceManager( ).getSharedPreferences( ).registerOnSharedPreferenceChangeListener( this );
        super.onPause( );
    }
    
    public void onSharedPreferenceChanged( SharedPreferences sharedPreferences,
                                           String key ) {
        DateFormat               sdf    = new SimpleDateFormat( "dd/MM/yyyy" );
        SharedPreferences.Editor editor = sharedPreferences.edit( );
    
        if( key.equals( KEY_PREF_DATE_PN ) ) {
            Preference pref     = findPreference( key );
            String     ladate[] = pref.getSharedPreferences( ).getString( KEY_PREF_DATE_PN, "01/01/2015" ).split( "/" );
            
            // Visite tout les 6 mois
            Calendar cal = new GregorianCalendar( );
            cal.set( Calendar.YEAR, Integer.valueOf( ladate[ 2 ] ) );
            cal.set( Calendar.MONTH, Integer.valueOf( ladate[ 1 ] ) );
            cal.set( Calendar.DAY_OF_MONTH, 1 );
            cal.add( Calendar.MONTH, 5 );
            String dateFormaterPn = sdf.format( cal.getTime( ) );
            // datePn.setText(getResources().getString(R.string.prochainePN) + ": " + dateFormaterPn);
            editor.putString( "nextDatePn", dateFormaterPn );
            editor.apply( );
        
        } else if( key.equals( KEY_PREF_DATE_CIV ) ) {
            Preference pref      = findPreference( key );
            String     ladate2[] = pref.getSharedPreferences( ).getString( "prefDateCertificatMediacal", "01/01/2015" ).split( "/" );
            String     age       = pref.getSharedPreferences( ).getString( "age", "20" );
            
            Calendar cal2 = new GregorianCalendar( );
            cal2.set( Calendar.YEAR, Integer.valueOf( ladate2[ 2 ] ) );
            cal2.set( Calendar.MONTH, Integer.valueOf( ladate2[ 1 ] ) );
            cal2.set( Calendar.DAY_OF_MONTH, Integer.valueOf( ladate2[ 0 ] ) );
            cal2.add( Calendar.DAY_OF_MONTH, -1 );
            // fonction de l'age la visite med est plus rapproché
            if( Integer.valueOf( age ) > 50 ) {
                cal2.add( Calendar.YEAR, 1 );
            } else if( Integer.valueOf( age ) < 40 ) {
                cal2.add( Calendar.YEAR, 5 );
            } else {
                cal2.add( Calendar.YEAR, 2 );
            }
            
            String dateFormaterCiv = sdf.format( cal2.getTime( ) );
            editor.putString( "nextDateCertif", dateFormaterCiv );
            editor.commit( );
        }
    }
    
}
