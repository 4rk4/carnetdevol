package fr.arkasoft.carnetdevol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.arkasoft.carnetdevol.db.FlightDbHelper;
import fr.arkasoft.carnetdevol.tool.ListFlightAdapter;

public class FlightListFragment extends Fragment {

    private FlightDbHelper flightDb;


    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {

        final View view = inflater.inflate ( R.layout.fragment_flight_list, null );

        flightDb = new FlightDbHelper ( getActivity ( ) );

        if ( flightDb.getAllFlight ( ).isEmpty ( ) ) {
            Snackbar.make ( view, getResources ( ).getString ( R.string.error_empty_db ), Snackbar.LENGTH_LONG ).show ( );
        }

        RecyclerView mListView = view.findViewById ( R.id.listAllFlight );
        mListView.setHasFixedSize ( true );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager ( getActivity ( ) );
        mListView.setLayoutManager ( mLayoutManager );

        RecyclerView.Adapter mAdapter = new ListFlightAdapter ( getActivity ( ), flightDb.getAllFlight ( ) );
        mListView.setAdapter ( mAdapter );

        TextView tvNbFlight = view.findViewById ( R.id.nbFlight );
        tvNbFlight.setText (
                String.format (
                        getResources ( ).getString ( R.string.frag_list_nb_flight ),
                        flightDb.getNumberOfFlight ( ) ) );

        return view;
    }


    @SuppressWarnings ( "ConstantConditions" )
    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {

        super.onActivityCreated ( savedInstanceState );
        ( ( AppCompatActivity ) getActivity ( ) ).getSupportActionBar ( ).setSubtitle ( R.string.act_main_drawer_my_flight );
        ( ( AppCompatActivity ) getActivity ( ) ).getSupportActionBar ( ).setTitle ( R.string.nav_header_main_app_name );
    }


    @Override
    public void onDestroy ( ) {

        super.onDestroy ( );
        flightDb.close ( );
    }


    public interface OnFragmentInteractionListener {

    }

}