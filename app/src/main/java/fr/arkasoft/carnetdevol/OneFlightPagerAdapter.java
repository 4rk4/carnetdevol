package fr.arkasoft.carnetdevol;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import fr.arkasoft.carnetdevol.db.Flight;
import fr.arkasoft.carnetdevol.db.FlightDbHelper;

class OneFlightPagerAdapter extends FragmentStatePagerAdapter {
    
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
    
        return context.getResources( ).getString( R.string.frag_one_flight_date ) +
               " " +
               FlightDbHelper.formatDateAffichage( flights.get( position ).getDate( ) ) +
               " - " +
               flights.get( position ).getHeureFin( );
    }
    
}