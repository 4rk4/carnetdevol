package fr.arkasoft.carnetdevol;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.arkasoft.carnetdevol.db.Flight;
import fr.arkasoft.carnetdevol.db.FlightDbHelper;

public class OneFlightFragment extends Fragment {

    private static final String ARG_PARAM = "idFlight";
    private              int    idFlight;


    public static OneFlightFragment newInstance ( int id ) {

        OneFlightFragment fragment = new OneFlightFragment ( );
        Bundle            args     = new Bundle ( );
        args.putInt ( ARG_PARAM, id );
        fragment.setArguments ( args );
        return fragment;
    }


    @Override
    public void onCreate ( Bundle savedInstanceState ) {

        super.onCreate ( savedInstanceState );
        if ( getArguments ( ) != null ) {
            idFlight = getArguments ( ).getInt ( ARG_PARAM );
        }
    }


    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {

        View                 v        = inflater.inflate ( R.layout.fragment_one_flight, container, false );
        Context              mContext = getActivity ( );
        final FlightDbHelper db       = new FlightDbHelper ( mContext );
        ArrayList < Flight > flights  = db.getAllFlight ( );

        OneFlightPagerAdapter cPagerAdapter = new OneFlightPagerAdapter ( getChildFragmentManager ( ), mContext, flights );
        ViewPager             mViewPager    = v.findViewById ( R.id.pagerOneFlight );
        mViewPager.setAdapter ( cPagerAdapter );

        int positionToShow = 0;
        int compteur       = 0;
        for ( Flight f : flights ) {
            if ( f.getId ( ) == idFlight ) {
                positionToShow = compteur;
                Log.i ( "DEBUG POSITION TO SHOW", String.valueOf ( positionToShow ) );
                Log.i ( "DEBUG COMPTEUR", String.valueOf ( compteur ) );
                Log.i ( "DEBUG GET ID", String.valueOf ( f.getId ( ) ) );
                Log.i ( "DEBUG ID FLIGHT", String.valueOf ( idFlight ) );
            }
            compteur++;
        }
        mViewPager.setCurrentItem ( positionToShow );

        return v;
    }

}
