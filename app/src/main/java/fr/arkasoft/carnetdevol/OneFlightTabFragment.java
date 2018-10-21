/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import fr.arkasoft.carnetdevol.db.Flight;
import fr.arkasoft.carnetdevol.db.FlightDbHelper;

public class OneFlightTabFragment extends Fragment {
    
    public static OneFlightTabFragment newInstance( int pos ) {
        OneFlightTabFragment stf  = new OneFlightTabFragment( );
        Bundle               args = new Bundle( );
        args.putInt( "pos", pos );
        stf.setArguments( args );
        return stf;
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState ) {
        View                 rootView       = inflater.inflate( R.layout.fragment_one_flight_tab, null );
        Bundle               args           = getArguments( );
        final int            numVue         = args.getInt( "pos" );
        final FlightDbHelper db             = new FlightDbHelper( getActivity( ) );
        Flight               oneFlight      = db.getOneFlightDb( numVue );
        ArrayList< String >  infosOneFlight = new ArrayList<>( );
        
        infosOneFlight.add( String.valueOf( oneFlight.getFonction_bord( ) ) );
        
        infosOneFlight.add( String.valueOf( oneFlight.getHeuresJour( ) ) + " " +
                            getResources( ).getString( R.string.frag_one_flight_hour_day ) + " - " +
                            String.valueOf( oneFlight.getHeuresNuit( ) ) + " " +
                            getResources( ).getString( R.string.frag_one_flight_hour_night ) );
    
        if( !oneFlight.getNature_vol( ).equalsIgnoreCase( "" ) ) {
            infosOneFlight.add(
                    getResources( ).getString( R.string.frag_one_flight_nature ) + " " +
                    oneFlight.getNature_vol( ) );
        }
        
        infosOneFlight.add(
                String.valueOf( getResources( ).getString( R.string.frag_one_flight_aircraft ) + oneFlight.getAircraft_type( ) ) + " " +
                oneFlight.getAircraft_immat( ) );
        
        infosOneFlight.add( getResources( ).getString( R.string.frag_one_flight_obs ) + " " +
                            oneFlight.getObs( ) );
    
        if( oneFlight.isIfr_vfr( ) ) {
            infosOneFlight.add( getResources( ).getString( R.string.frag_one_flight_ifr ) );
        } else {
            infosOneFlight.add( getResources( ).getString( R.string.frag_one_flight_vfr ) );
        }
    
        if( oneFlight.isSimu_vol( ) ) {
            infosOneFlight.add( getResources( ).getString( R.string.frag_one_flight_simu ) );
        }
    
        if( oneFlight.isMulti_mono( ) ) {
            infosOneFlight.add( getResources( ).getString( R.string.frag_one_flight_multi ) );
        }
    
        if( !oneFlight.getArrivees_ifr( ).equalsIgnoreCase( "" ) ) {
            infosOneFlight.add(
                    getResources( ).getString( R.string.frag_one_flight_arr_ifr ) + " " +
                    String.valueOf( oneFlight.getArrivees_ifr( ) ) );
        }
        infosOneFlight.add( String.valueOf( oneFlight.getAtt_jour( ) ) + " " +
                            getResources( ).getString( R.string.frag_one_flight_att_day ) + " - " +
                            oneFlight.getAtt_nuit( ) + " " +
                            getResources( ).getString( R.string.frag_one_flight_att_night
                                                     )
        
                          );
        //
        //        //test list:
        ArrayAdapter< String > simpleAdapter = new ArrayAdapter<>( getActivity( ), android.R.layout.simple_list_item_1, infosOneFlight );
        ListView               lvOneFlight   = rootView.findViewById( R.id.listOneFlight );
        lvOneFlight.setAdapter( simpleAdapter );
        //
        Button btMod = rootView.findViewById( R.id.oneFlightModification );
        //btMod.setTag(oneFlight.getId());
        btMod.setOnClickListener( new View.OnClickListener( ) {
            
            @Override
            public void onClick( View view ) {
                //id = (int) view.getTag();
                Bundle args = new Bundle( );
                args.putInt( "id", numVue );
                
                AddFlightFragment afFrag = new AddFlightFragment( );
                afFrag.setArguments( args );
                
                getActivity( ).getSupportFragmentManager( )
                              .beginTransaction( )
                              .replace( R.id.mainFrag, afFrag )
                              .addToBackStack( "oneFlight" )
                              .commit( );
            }
        } );
        //
        Button btSup = rootView.findViewById( R.id.oneFlightSuppression );
        // Pour la suppression on tag le textview date
        //btSup.setTag(oneFlight.getId());
        btSup.setOnClickListener( new View.OnClickListener( ) {
            
            @Override
            public void onClick( final View view ) {
                
                //id = (int) view.getTag();
                //DialogInterface.
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity( ) );
                builder.setMessage( R.string.dialog_mesg_delete )
                       .setTitle( R.string.dialog_delete_title );
                builder.setPositiveButton( R.string.dialog_delete_ok, new DialogInterface.OnClickListener( ) {
                    
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
    
                        if( db.deleteOneFlight( numVue ) ) {
                            Snackbar.make( view, getResources( ).getString( R.string.frag_one_flight_delete ), Snackbar.LENGTH_LONG ).show( );
                            getActivity( ).getSupportFragmentManager( )
                                          .beginTransaction( )
                                          .replace( R.id.mainFrag, new FlightListFragment( ) )
                                          .commit( );
                        }
                    }
                } );
                builder.setNegativeButton( R.string.dialog_delete_cancel, new DialogInterface.OnClickListener( ) {
                    
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                    
                    }
                } );
                AlertDialog dialog = builder.create( );
                dialog.show( );
            }
        } );
        
        return rootView;
    }
}
