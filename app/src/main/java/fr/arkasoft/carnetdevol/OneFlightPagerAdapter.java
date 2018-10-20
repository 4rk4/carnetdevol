/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import fr.arkasoft.carnetdevol.db.Flight;
import fr.arkasoft.carnetdevol.db.FlightDbHelper;

public class OneFlightPagerAdapter extends FragmentStatePagerAdapter {
    
    private final Context             context;
    private final ArrayList< Flight > flights;
    
    public OneFlightPagerAdapter( FragmentManager fm, Context nContext, ArrayList< Flight > f ) {
        super( fm );
        context = nContext;
        flights = f;
    }
    
    @Override
    public Fragment getItem( int i ) {
        return OneFlightTabFragment.newInstance( flights.get( i ).getId( ) );
    }
    
    @Override
    public int getCount( ) {
        return flights.size( );
    }
    
    @Override
    public CharSequence getPageTitle( int position ) {
        
        StringBuilder affDate = new StringBuilder( );
        affDate.append( context.getResources( ).getString( R.string.frag_one_flight_date ) );
        affDate.append( " " );
        affDate.append( FlightDbHelper.formatDateAffichage( flights.get( position ).getDate( ) ) );
        affDate.append( " - " );
        affDate.append( flights.get( position ).getHeureFin( ) );
        
        return affDate.toString( );
    }
    
}