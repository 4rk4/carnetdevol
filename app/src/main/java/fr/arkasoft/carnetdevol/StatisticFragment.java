/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;

import fr.arkasoft.carnetdevol.db.FlightDbHelper;
import fr.arkasoft.carnetdevol.tool.Cercle;
import fr.arkasoft.carnetdevol.tool.TypeCercle;

public class StatisticFragment extends Fragment {
    
    protected Cercle cercleHeures;
    private   View   v;
    private   String fonctionSelect;
    private   int    dateSelect;
    
    public StatisticFragment( ) {
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        
        v = inflater.inflate( R.layout.fragment_statistic, container, false );
        
        StatisticPagerAdapter cPagerAdapter = new StatisticPagerAdapter( getChildFragmentManager( ), getActivity( ) );
        ViewPager             mViewPager    = v.findViewById( R.id.pager );
        mViewPager.setAdapter( cPagerAdapter );
        
        Spinner spFonction = v.findViewById( R.id.selectFonction );
        ArrayAdapter< CharSequence > adapterFonction = ArrayAdapter.createFromResource( getActivity( ),
                                                                                        R.array.fonction_bord, android.R.layout.simple_list_item_1 );
        spFonction.setAdapter( adapterFonction );
        
        spFonction.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ) {
            
            @Override
            public void onItemSelected( AdapterView< ? > adapterView, View view, int i, long l ) {
                fonctionSelect = String.valueOf( adapterView.getSelectedItemId( ) );
                affStat( );
            }
            
            @Override
            public void onNothingSelected( AdapterView< ? > adapterView ) {
            
            }
        } );
        
        Spinner sp = v.findViewById( R.id.selectStat );
        ArrayAdapter< CharSequence > adapter = ArrayAdapter.createFromResource( getActivity( ),
                                                                                R.array.stat, android.R.layout.simple_list_item_1 );
        sp.setAdapter( adapter );
        
        sp.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ) {
            
            @Override
            public void onItemSelected( AdapterView< ? > adapterView, View view, int i, long l ) {
                dateSelect = ( int ) adapterView.getSelectedItemId( );
                affStat( );
            }
            
            @Override
            public void onNothingSelected( AdapterView< ? > adapterView ) {
            }
        } );
        return v;
    }
    
    @SuppressWarnings( "ConstantConditions" )
    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        ( ( AppCompatActivity ) getActivity( ) ).getSupportActionBar( ).setSubtitle( R.string.act_main_drawer_stat );
        ( ( AppCompatActivity ) getActivity( ) ).getSupportActionBar( ).setTitle( R.string.nav_header_main_app_name );
    }
    
    @Override
    public void onStart( ) {
        super.onStart( );
        cercleHeures = v.findViewById( R.id.cercleHeures );
    }
    
    @Override
    public void onDetach( ) {
        super.onDetach( );
    }
    
    public void affStat( ) {
        
        FlightDbHelper db = new FlightDbHelper( getActivity( ).getBaseContext( ) );
        
        float heureJour;
        float heureNuit;
        int   attJour;
        int   attNuit;
        
        HashMap< String, Float > lesHeuresTotal;
        
        switch ( dateSelect ) {
            case 0:
                lesHeuresTotal = db.getStatVol( FlightDbHelper.STAT_TOTAL, fonctionSelect );
                break;
            case 1:
                lesHeuresTotal = db.getStatVol( FlightDbHelper.STAT_MOIS_EN_COURS, fonctionSelect );
                break;
            case 2:
                lesHeuresTotal = db.getStatVol( FlightDbHelper.STAT_ANNEE_EN_COURS, fonctionSelect );
                break;
            default:
                lesHeuresTotal = db.getStatVol( FlightDbHelper.STAT_TOTAL, fonctionSelect );
                break;
        }
        
        db.close( );
        
        heureJour = lesHeuresTotal.get( "jour" );
        heureNuit = lesHeuresTotal.get( "nuit" );
        attJour = Math.round( lesHeuresTotal.get( "attjour" ) );
        attNuit = Math.round( lesHeuresTotal.get( "attnuit" ) );
        
        cercleHeures = v.findViewById( R.id.cercleHeures );
        cercleHeures.init( Math.round( heureJour ), Math.round( heureNuit ), TypeCercle.HDV );
        
        Cercle cercleAtt = v.findViewById( R.id.cercleAtt );
        cercleAtt.init( attJour, attNuit, TypeCercle.ATT );
        
    }
}
