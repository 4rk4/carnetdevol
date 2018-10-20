package fr.arkasoft.carnetdevol.tool;

import android.content.Context;
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
    
    protected Context             context;
    private   ArrayList< Flight > flights;
    
    public ListFlightAdapter( Context context, ArrayList< Flight > flights ) {
        this.context = context;
        this.flights = flights;
    }
    
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        // create a new view
        View v = LayoutInflater.from( parent.getContext( ) )
                               .inflate( R.layout.listview_items, parent, false );
        ViewHolder vh = new ViewHolder( v, this.context );
        return vh;
    }
    
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( ViewHolder holder, int position ) {
        holder.display( flights.get( position ) );
    }
    
    @Override
    public int getItemCount( ) {
        return flights.size( );
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        protected final Context  mContext;
        // each data item is just a string in this case
        public          TextView mTextViewDate;
        public          TextView mTextViewHeureJour;
        public          TextView mTextViewHeureNuit;
        public          TextView mTextViewAvion;
        public          TextView mTextViewImmat;
        public          TextView mTextViewNature;
        private         Flight   mFlight;
        
        public ViewHolder( final View v, Context context ) {
            super( v );
            this.mContext = context;
            mTextViewDate = v.findViewById( R.id.itemDate );
            mTextViewHeureJour = v.findViewById( R.id.itemHeureJour );
            mTextViewHeureNuit = v.findViewById( R.id.itemHeuresNuit );
            mTextViewNature = v.findViewById( R.id.itemNature );
            mTextViewAvion = v.findViewById( R.id.itemAvion );
            mTextViewImmat = v.findViewById( R.id.itemImat );
            
            v.setOnClickListener( new View.OnClickListener( ) {
                
                @Override
                public void onClick( View view ) {
                    int tagDate = ( int ) ( view.findViewById( R.id.itemDate ) ).getTag( );
                    ( ( AppCompatActivity ) mContext ).getSupportFragmentManager( )
                                                      .beginTransaction( )
                                                      .replace( R.id.mainFrag, new OneFlightFragment( ).newInstance( tagDate ) )
                                                      .addToBackStack( "listeDesVols" )
                                                      .commit( );
                }
            } );
        }
        
        public void display( Flight f ) {
            this.mFlight = f;
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