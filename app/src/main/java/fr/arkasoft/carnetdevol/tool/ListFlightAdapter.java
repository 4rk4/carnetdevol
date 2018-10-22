package fr.arkasoft.carnetdevol.tool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fr.arkasoft.carnetdevol.OneFlightFragment;
import fr.arkasoft.carnetdevol.R;
import fr.arkasoft.carnetdevol.db.Flight;
import fr.arkasoft.carnetdevol.db.FlightDbHelper;

public class ListFlightAdapter extends RecyclerView.Adapter< ListFlightAdapter.ViewHolder > {
    
    private final Context             context;
    private final ArrayList< Flight > flights;
    
    public ListFlightAdapter( Context context, ArrayList< Flight > flights ) {
        this.context = context;
        this.flights = flights;
    }
    
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        // create a new view
        View v = LayoutInflater.from( parent.getContext( ) )
                               .inflate( R.layout.listview_items, parent, false );
        return new ViewHolder( v, this.context );
    }
    
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position ) {
        holder.display( flights.get( position ) );
    }
    
    @Override
    public int getItemCount( ) {
        return flights.size( );
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
    
        final Context  mContext;
        // each data item is just a string in this case
        final TextView mTextViewDate;
        final TextView mTextViewHeureJour;
        final TextView mTextViewHeureNuit;
        final TextView mTextViewAvion;
        final TextView mTextViewImmat;
        final TextView mTextViewNature;
    
        ViewHolder( final View v, Context context ) {
            super( v );
            this.mContext = context;
            mTextViewDate = v.findViewById( R.id.itemDate );
            mTextViewHeureJour = v.findViewById( R.id.itemHeureJour );
            mTextViewHeureNuit = v.findViewById( R.id.itemHeuresNuit );
            mTextViewNature = v.findViewById( R.id.itemNature );
            mTextViewAvion = v.findViewById( R.id.itemAvion );
            mTextViewImmat = v.findViewById( R.id.itemImat );
        
            v.setOnClickListener( view -> {
                int tagDate = ( int ) ( view.findViewById( R.id.itemDate ) ).getTag( );
                ( ( AppCompatActivity ) mContext ).getSupportFragmentManager( )
                                                  .beginTransaction( )
                                                  .replace( R.id.mainFrag, OneFlightFragment.newInstance( tagDate ) )
                                                  .addToBackStack( "listeDesVols" )
                                                  .commit( );
            } );
        }
    
        void display( Flight f ) {
            mTextViewDate.setText( String.valueOf( FlightDbHelper.formatDateAffichage( f.getDate( ) ) ) );
            //Dans le tag de la date on cachera l'ID en DB du vol
            mTextViewDate.setTag( f.getId( ) );
            mTextViewHeureJour.setText( f.getHeuresJour( ) );
            mTextViewHeureNuit.setText( f.getHeuresNuit( ) );
            mTextViewNature.setText( f.getNature_vol( ) );
            mTextViewAvion.setText( f.getAircraft_type( ) );
            mTextViewImmat.setText( f.getAircraft_immat( ) );
        }
    }
}