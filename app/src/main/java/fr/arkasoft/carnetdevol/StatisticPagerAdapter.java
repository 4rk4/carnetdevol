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

public class StatisticPagerAdapter extends FragmentStatePagerAdapter {
    
    private final Context context;
    
    public StatisticPagerAdapter( FragmentManager fm, Context nContext ) {
        super( fm );
        context = nContext;
        
    }
    
    @Override
    public Fragment getItem( int i ) {
        return StatisticTabFragment.newInstance( i );
    }
    
    @Override
    public int getCount( ) {
        return 2;
    }
    
    @Override
    public CharSequence getPageTitle( int position ) {
        
        String res = "";
        switch ( position ) {
            case 0:
                res = context.getResources( ).getString( R.string.frag_statistic_repart_hdv );
                break;
            case 1:
                res = context.getResources( ).getString( R.string.frag_statistic_att );
                break;
        }
        
        return res;
    }
    
}