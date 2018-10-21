/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatisticTabFragment extends Fragment {
    
    public static StatisticTabFragment newInstance( int pos ) {
        StatisticTabFragment stf  = new StatisticTabFragment( );
        Bundle               args = new Bundle( );
        args.putInt( "pos", pos );
        stf.setArguments( args );
        return stf;
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState ) {
        View   rootView;
        Bundle args   = getArguments( );
        int    numVue = args.getInt( "pos" );
        int    resId;
        switch( numVue ) {
            case 0:
                resId = R.layout.fragment_statistic_hdv;
                break;
            case 1:
                resId = R.layout.fragment_statistic_att;
                break;
            default:
                resId = R.layout.fragment_statistic_hdv;
                break;
        }
        
        rootView = inflater.inflate( resId, null );
        return rootView;
    }
}
