/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.arkasoft.carnetdevol.db.Flight;
import fr.arkasoft.carnetdevol.db.FlightDbHelper;
import fr.arkasoft.carnetdevol.tool.DatePickerFragment;
import fr.arkasoft.carnetdevol.tool.NumberPickerFragment;

public class AddFlightFragment extends Fragment implements NumberPickerFragment.OnValidateListener, DatePickerDialog.OnDateSetListener {
    
    private View                 rootView;
    private boolean              isHeureNuit  = false;
    private boolean              isHeureJour  = false;
    private boolean              isMod;
    private int                  idFlight;
    private FlightDbHelper       fdbh;
    private AutoCompleteTextView actNatureVol;
    private AutoCompleteTextView actTypeAvion;
    private AutoCompleteTextView actImmat;
    private AutoCompleteTextView actArriveesIfr;
    private Button               btAddOneFlight;
    private Button               btDate;
    private Button               btHeureDebut;
    private Button               btHeuresDeJour;
    private Button               btHeuresDeNuit;
    private NumberPicker         npAttDeJour;
    private NumberPicker         npAttDeNuit;
    private Button               btOngletAtt;
    private Button               btOngletDivers;
    private Button               btOngletIfr;
    private Spinner              spinFonction;
    private Calendar             cal;
    public  View.OnClickListener addOneFlight = new View.OnClickListener( ) {
        
        @Override
        public void onClick( View view ) {
            String   laDate       = btDate.getText( ).toString( );
            String   heureDebut   = btHeureDebut.getText( ).toString( );
            String   heuresJour   = btHeuresDeJour.getText( ).toString( );
            String   heuresNuit   = btHeuresDeNuit.getText( ).toString( );
            String   nature       = actNatureVol.getText( ).toString( );
            String   typeAvion    = actTypeAvion.getText( ).toString( );
            String   immat        = actImmat.getText( ).toString( );
            String   fonction     = String.valueOf( spinFonction.getSelectedItemId( ) );
            boolean  simu         = ( ( Switch ) rootView.findViewById( R.id.simulation ) ).isChecked( );
            boolean  ifr          = ( ( Switch ) rootView.findViewById( R.id.ifr ) ).isChecked( );
            boolean  multi        = ( ( Switch ) rootView.findViewById( R.id.multi ) ).isChecked( );
            String   arriveesIfr  = ( ( EditText ) rootView.findViewById( R.id.arriveesIfr ) ).getText( ).toString( );
            String   obs          = ( ( EditText ) rootView.findViewById( R.id.observation ) ).getText( ).toString( );
            int      attNuit      = ( ( NumberPicker ) rootView.findViewById( R.id.attNuit ) ).getValue( );
            int      attJour      = ( ( NumberPicker ) rootView.findViewById( R.id.attJour ) ).getValue( );
            String   dateFormmater;
            String[] dateSplitter = laDate.split( "/" );
            int      day          = Integer.valueOf( dateSplitter[ 0 ] );
            int      month        = Integer.valueOf( dateSplitter[ 1 ] );
            int      year         = Integer.valueOf( dateSplitter[ 2 ] );
            int      heure;
            int      minutes;
            
            // Contrôle de saisie
            if ( ( !heuresJour.contains( ":" ) ) && ( !heuresNuit.contains( ":" ) ) ) {
                Snackbar.make( rootView, getResources( ).getString( R.string.error_nb_heures ), Snackbar.LENGTH_LONG ).show( );
                return;
            }
            if ( !heureDebut.contains( ":" ) ) {
                Snackbar.make( rootView, getResources( ).getString( R.string.error_start_time ), Snackbar.LENGTH_LONG ).show( );
                return;
            } else {
                cal = Calendar.getInstance( );
                cal.set( Calendar.DAY_OF_MONTH, day );
                cal.set( Calendar.MONTH, month );
                cal.set( Calendar.YEAR, year );
                String[] hmDebut = heureDebut.split( ":" );
                heure = Integer.valueOf( hmDebut[ 0 ] );
                minutes = Integer.valueOf( hmDebut[ 1 ] );
                cal.set( Calendar.HOUR_OF_DAY, heure );
                cal.set( Calendar.MINUTE, minutes );
                cal.set( Calendar.SECOND, 0 );
                dateFormmater = String.format( "%4d-%02d-%02d %s:00", year, month, day, heureDebut );
            }
            if ( !heuresJour.contains( ":" ) ) {
                heuresJour = "00:00";
            } else {
                String[] heureMinuteJour = heuresJour.split( ":" );
                cal.add( Calendar.HOUR_OF_DAY, Integer.valueOf( heureMinuteJour[ 0 ] ) );
                cal.add( Calendar.MINUTE, Integer.valueOf( heureMinuteJour[ 1 ] ) );
            }
            if ( !heuresNuit.contains( ":" ) ) {
                heuresNuit = "00:00";
            } else {
                String[] heureMinuteNuit = heuresNuit.split( ":" );
                cal.add( Calendar.HOUR_OF_DAY, Integer.valueOf( heureMinuteNuit[ 0 ] ) );
                cal.add( Calendar.MINUTE, Integer.valueOf( heureMinuteNuit[ 1 ] ) );
            }
            
            String heureFin = new SimpleDateFormat( "HH:mm" ).format( cal.getTime( ) );
            
            Flight oneFlight = new Flight( dateFormmater, heureDebut, heureFin, typeAvion,
                                           immat, fonction, nature, heuresJour, heuresNuit,
                                           multi, ifr, simu,
                                           arriveesIfr,
                                           attJour, attNuit,
                                           obs );
            if ( isMod ) {
                fdbh.updateFlight( oneFlight, idFlight );
            } else {
                fdbh.insertFlight( oneFlight );
            }
            
            // Information utilisateur
            Snackbar.make( rootView, getResources( ).getString( R.string.msg_save_flight ), Snackbar.LENGTH_LONG ).show( );
        }
    };
    private Switch               btIfr;
    private Switch               btSimu;
    private Switch               btMulti;
    private EditText             etObservations;
    private Bundle               bundle;
    
    public AddFlightFragment( ) {
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }
    
    @Override
    public View onCreateView( final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        rootView = inflater.inflate( R.layout.fragment_add_flight, container, false );
        
        SharedPreferences preferences  = PreferenceManager.getDefaultSharedPreferences( getContext( ) );
        String            prefFonction = preferences.getString( "prefFunction", "" );
        
        btSimu = rootView.findViewById( R.id.simulation );
        btMulti = rootView.findViewById( R.id.multi );
        btIfr = rootView.findViewById( R.id.ifr );
        btHeureDebut = rootView.findViewById( R.id.flight_heure_debut );
        etObservations = rootView.findViewById( R.id.observation );
        
        btHeuresDeJour = rootView.findViewById( R.id.jour );
        btHeuresDeJour.setOnClickListener( new View.OnClickListener( ) {
            
            @Override
            public void onClick( View view ) {
                showNumberPickerDialog( "heureDeJour" );
                // Pour ouvrir le numberpicker fonction de nuit ou jour
                isHeureJour = true;
                isHeureNuit = false;
            }
        } );
        
        btHeuresDeNuit = rootView.findViewById( R.id.nuit );
        btHeuresDeNuit.setOnClickListener( view -> {
            showNumberPickerDialog( "heureDeNuit" );
            isHeureJour = false;
            isHeureNuit = true;
        } );
        
        actTypeAvion = rootView.findViewById( R.id.avionType );
        actImmat = rootView.findViewById( R.id.avionImmat );
        actNatureVol = rootView.findViewById( R.id.natureVol );
        actArriveesIfr = rootView.findViewById( R.id.arriveesIfr );
        
        npAttDeJour = rootView.findViewById( R.id.attJour );
        npAttDeJour.setMaxValue( 20 );
        
        npAttDeNuit = rootView.findViewById( R.id.attNuit );
        npAttDeNuit.setMaxValue( 20 );
        
        btOngletAtt = rootView.findViewById( R.id.titleOngletAtt );
        btOngletAtt.setOnClickListener( view -> {
            LinearLayout onglet = rootView.findViewById( R.id.ongletAtt );
            annimationDesOnglets( onglet );
        } );
        
        btOngletDivers = rootView.findViewById( R.id.titleOngletDiver );
        btOngletDivers.setOnClickListener( view -> {
            LinearLayout onglet = rootView.findViewById( R.id.ongletDivers );
            annimationDesOnglets( onglet );
        } );
        
        btOngletIfr = rootView.findViewById( R.id.titleOngletIfr );
        btOngletIfr.setOnClickListener( view -> {
            LinearLayout onglet = rootView.findViewById( R.id.ongletIfr );
            annimationDesOnglets( onglet );
        } );
        
        btDate = rootView.findViewById( ( R.id.flight_date ) );
        btDate.setOnClickListener( view -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment( );
            datePickerFragment.show( getActivity( ).getSupportFragmentManager( ), "datePicker" );
        } );
        
        spinFonction = rootView.findViewById( R.id.fonction );
        int j = spinFonction.getCount( );
        for ( int i = 0; i < j; i++ ) {
            if ( spinFonction.getItemAtPosition( i ).toString( ).equals( prefFonction ) ) {
                spinFonction.setSelection( i );
                break;
            }
        }
        
        fdbh = new FlightDbHelper( getActivity( ) );
        
        ArrayAdapter< String > adapterNature = new ArrayAdapter<>( getActivity( ),
                                                                   android.R.layout.simple_dropdown_item_1line, fdbh.getNature( ) );
        actNatureVol.setAdapter( adapterNature );
        
        ArrayAdapter< String > adapterTypeAvion = new ArrayAdapter<>( getActivity( ),
                                                                      android.R.layout.simple_dropdown_item_1line, fdbh.getTypeAvion( ) );
        actTypeAvion.setAdapter( adapterTypeAvion );
        
        ArrayAdapter< String > adapterImmat = new ArrayAdapter<>( getActivity( ),
                                                                  android.R.layout.simple_dropdown_item_1line, fdbh.getImmat( ) );
        actImmat.setAdapter( adapterImmat );
        
        ArrayAdapter< String > adapterArriveesIfr = new ArrayAdapter<>( getActivity( ),
                                                                        android.R.layout.simple_dropdown_item_1line, fdbh.getArriveesIFR( ) );
        actArriveesIfr.setAdapter( adapterArriveesIfr );
        
        // On met la date sur le bouton pour éviter une manip
        Calendar         dateDuJour = new GregorianCalendar( );
        SimpleDateFormat formatDate = new SimpleDateFormat( "dd/MM/yyyy" );
        btDate.setText( formatDate.format( dateDuJour.getTime( ) ) );
        
        // Test de la modification ou non
        bundle = this.getArguments( );
        if ( bundle != null ) {
            idFlight = bundle.getInt( "id" );
            enModification( idFlight );
        }
        btAddOneFlight = rootView.findViewById( R.id.addOneFlight );
        btAddOneFlight.setOnClickListener( addOneFlight );
        return rootView;
    }
    
    public void showNumberPickerDialog( String demandeur ) {
        DialogFragment newFragment = new NumberPickerFragment( );
        newFragment.show( getActivity( ).getSupportFragmentManager( ), demandeur );
    }
    
    public void annimationDesOnglets( final View v ) {
        v.clearAnimation( );
        int vitesse = 300;
        
        // getWidth de view n'est pas accessible lors de la premiere fois
        int tailleLargeur;
        if ( v.getWidth( ) == 0 ) {
            tailleLargeur = rootView.getWidth( );
        } else {
            tailleLargeur = v.getWidth( );
        }
        
        int test = v.getVisibility( );
        if ( test == View.VISIBLE ) {
            TranslateAnimation translateAnimationClose = new TranslateAnimation( 0, tailleLargeur, 0, 0 );
            translateAnimationClose.setDuration( vitesse );
            translateAnimationClose.setAnimationListener( new Animation.AnimationListener( ) {
                
                @Override
                public void onAnimationStart( Animation animation ) {
                }
                
                @Override
                public void onAnimationEnd( Animation animation ) {
                    v.setVisibility( View.GONE );
                }
                
                @Override
                public void onAnimationRepeat( Animation animation ) {
                }
            } );
            v.startAnimation( translateAnimationClose );
            
        } else if ( test == View.GONE ) {
            
            TranslateAnimation translateAnimation = new TranslateAnimation( tailleLargeur, 0, 0, 0 );
            translateAnimation.setDuration( vitesse );
            
            v.startAnimation( translateAnimation );
            v.setVisibility( View.VISIBLE );
        }
    }
    
    private void enModification( int idFlight ) {
        isMod = true;
        Flight f = fdbh.getOneFlightDb( idFlight );
        btDate.setText( FlightDbHelper.formatOnlyDateAffichage( f.getDate( ) ) );
        btHeureDebut.setText( f.getHeureDebut( ) );
        btHeuresDeJour.setText( f.getHeuresJour( ) );
        btHeuresDeNuit.setText( f.getHeuresNuit( ) );
        actNatureVol.setText( f.getNature_vol( ) );
        actTypeAvion.setText( f.getAircraft_type( ) );
        actImmat.setText( f.getAircraft_immat( ) );
        int compteur = spinFonction.getCount( );
        for ( int i = 0; i < compteur; i++ ) {
            if ( spinFonction.getItemAtPosition( i ).toString( ).equals( f.getFonction_bord( ) ) ) {
                spinFonction.setSelection( i );
                break;
            }
        }
        if ( f.isSimu_vol( ) )
            btSimu.setChecked( true );
        if ( f.isIfr_vfr( ) )
            btIfr.setChecked( true );
        if ( f.isMulti_mono( ) )
            btMulti.setChecked( true );
        actArriveesIfr.setText( f.getArrivees_ifr( ) );
        etObservations.setText( f.getObs( ) );
        npAttDeNuit.setValue( f.getAtt_nuit( ) );
        npAttDeJour.setValue( f.getAtt_jour( ) );
    }
    
    @SuppressWarnings( "ConstantConditions" )
    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( AppCompatActivity ) getActivity( ) ).getSupportActionBar( ).setSubtitle( R.string.act_main_drawer_add );
        ( ( AppCompatActivity ) getActivity( ) ).getSupportActionBar( ).setTitle( R.string.nav_header_main_app_name );
    }
    
    @Override
    public void onDetach( ) {
        super.onDetach( );
    }
    
    @Override
    public void onDialogPositiveClick( DialogFragment dialog, int h, int m ) {
        Button bt = null;
        if ( isHeureJour ) {
            bt = btHeuresDeJour;
        } else if ( isHeureNuit ) {
            bt = btHeuresDeNuit;
        }
        assert bt != null;
        bt.setText( String.format( "%02d:%02d", h, m ) );
    }
    
    public void onDateSet( DatePicker view, int year, int month, int day ) {
        btDate.setText( String.format( "%02d/%02d/%4d", day, month + 1, year ) );
    }
}