package fr.arkasoft.carnetdevol;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class StatisticPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;


    public StatisticPagerAdapter ( FragmentManager fm, Context nContext ) {

        super ( fm );
        context = nContext;

    }


    @Override
    public Fragment getItem ( int i ) {

        return StatisticTabFragment.newInstance ( i );
    }


    @Override
    public int getCount ( ) {

        return 2;
    }


    @Override
    public CharSequence getPageTitle ( int position ) {

        String res = "";
        switch ( position ) {
            case 0:
                res = context.getResources ( ).getString ( R.string.frag_statistic_repart_hdv );
                break;
            case 1:
                res = context.getResources ( ).getString ( R.string.frag_statistic_att );
                break;
        }

        return res;
    }

}