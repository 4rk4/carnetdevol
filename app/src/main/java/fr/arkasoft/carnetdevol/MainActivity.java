/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */

package fr.arkasoft.carnetdevol;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.arkasoft.carnetdevol.db.FlightDbHelper;
import fr.arkasoft.carnetdevol.tool.SettingFragment;
import fr.arkasoft.carnetdevol.tool.TimePickerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   FlightListFragment.OnFragmentInteractionListener,
                   TimePickerDialog.OnTimeSetListener {
    
    private int            FILE_SELECTED_CODE;
    private String         compDemandeur;
    private boolean        notify  = false;
    private String         message = "";
    private FlightDbHelper fDB;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( view -> {
            // On affiche le frag de consultation
            getSupportFragmentManager( )
                    .beginTransaction( )
                    .replace( R.id.mainFrag, new AddFlightFragment( ) )
                    .addToBackStack( null )
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                    .commit( );
        } );
        
        //Gestion des notification fonction des dates de PN
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( this );
        String            datePn      = preferences.getString( "nextDatePn", "01/01/2017" );
        String            dateCiv     = preferences.getString( "nextDateCertif", "01/01/2017" );
        
        String[] ladatePn    = datePn.split( "/" );
        String[] datePrefCiv = dateCiv.split( "/" );
        
        Calendar calToday     = new GregorianCalendar( );
        int      currentMonth = calToday.get( Calendar.MONTH ) + 1;
        int      currentYear  = calToday.get( Calendar.YEAR );
        
        // - 1 pour notivier le mois ou la visite doit etre faite
        int moisPn  = Integer.valueOf( ladatePn[ 1 ] ) - 1;
        int anneePn = Integer.valueOf( ladatePn[ 2 ] );
        
        int moisCiv  = Integer.valueOf( datePrefCiv[ 1 ] ) - 1;
        int anneeCiv = Integer.valueOf( datePrefCiv[ 2 ] );
    
        if( ( currentMonth == moisPn ) && currentYear == anneePn ) {
            notify = true;
            message = this.getResources( ).getString( R.string.rappel_pn );
        }
        if( ( currentMonth == moisCiv ) && currentYear == anneeCiv ) {
            notify = true;
            message = this.getResources( ).getString( R.string.rappel_medical );
        }
        if( notify ) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder( this )
                            .setSmallIcon( R.drawable.ic_flight )
                            .setContentTitle( message )
                            .setContentText( datePn );
            Intent           resultIntent = new Intent( this, MainActivity.class );
            TaskStackBuilder stackBuilder = TaskStackBuilder.create( this );
            stackBuilder.addParentStack( MainActivity.class );
            stackBuilder.addNextIntent( resultIntent );
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                                                 );
            mBuilder.setContentIntent( resultPendingIntent );
            NotificationManager mNotificationManager =
                    ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
            mNotificationManager.notify( 2, mBuilder.build( ) );
        }
        
        //Gesion du Drawer
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState( );
        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
        
        // On affiche le frag de consultation
        if( savedInstanceState == null ) {
            getSupportFragmentManager( )
                    .beginTransaction( )
                    .add( R.id.mainFrag, new FlightListFragment( ) )
                    //                    .addToBackStack(null)
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                    .commit( );
        }
        
        fDB = new FlightDbHelper( this );
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
    
        if( requestCode == FILE_SELECTED_CODE && resultCode == Activity.RESULT_OK ) {
            Uri uri;
            if( data != null ) {
                uri = data.getData( );
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( uri, "r" );
                    InputStream          inputStream          = getContentResolver( ).openInputStream( uri );
                    BufferedReader       reader               = new BufferedReader( new InputStreamReader( inputStream ) );
                    StringBuilder        stringBuilder        = new StringBuilder( );
                    String               line;
                    while( ( line = reader.readLine( ) ) != null ) {
                        stringBuilder.append( line );
                        stringBuilder.append( "\n" );
                    }
    
                    if( fDB.importFlight( stringBuilder.toString( ) ) > 0 ) {
                        Snackbar.make( getSupportFragmentManager( ).findFragmentById( R.id.mainFrag ).getView( ),
                                       getResources( ).getString( R.string.msg_import ),
                                       Snackbar.LENGTH_LONG ).show( );
                    }
                    getSupportFragmentManager( )
                            .beginTransaction( )
                            .replace( R.id.mainFrag, new FlightListFragment( ) )
                            .addToBackStack( null )
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                            .commit( );
                    
                    inputStream.close( );
                    parcelFileDescriptor.close( );
                } catch( Exception e ) {
                    e.printStackTrace( );
                }
            }
        }
    }
    
    @Override
    public void onBackPressed( ) {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        }
        int count = getSupportFragmentManager( ).getBackStackEntryCount( );
        if( count == 0 ) {
            super.onBackPressed( );
        } else {
            getSupportFragmentManager( ).popBackStack( );
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater( ).inflate( R.menu.main, menu );
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        int id = item.getItemId( );
    
        if( id == R.id.action_settings ) {
            getSupportFragmentManager( )
                    .beginTransaction( )
                    .remove( getSupportFragmentManager( )
                                     .findFragmentById( R.id.mainFrag ) )
                    .commit( );
            getFragmentManager( )
                    .beginTransaction( )
                    .replace( R.id.mainFrag, new SettingFragment( ) )
                    .commit( );
            return true;
        }
    
        if( id == R.id.action_export ) {
            if( fDB.export( ) ) {
                Snackbar.make( getSupportFragmentManager( ).findFragmentById( R.id.mainFrag ).getView( ),
                               getResources( ).getString( R.string.msg_export ),
                               Snackbar.LENGTH_LONG ).show( );
            }
            return true;
        }
    
        if( id == R.id.action_import ) {
            Intent intent = new Intent( );
            intent.setAction( Intent.ACTION_GET_CONTENT );
            intent.setType( "text/xml" );
            startActivityForResult( intent, FILE_SELECTED_CODE );
            return true;
        }
    
        if( id == R.id.action_delete_all ) {
            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            builder.setMessage( getResources( ).getString( R.string.dialog_delete_all ) )
                   .setTitle( getResources( ).getString( R.string.dialog_delete_title ) )
                   .setPositiveButton( getResources( ).getString( R.string.dialog_delete_ok ), ( dialogInterface, i ) -> {
                       fDB.deleteAllFlight( );
                       getSupportFragmentManager( )
                               .beginTransaction( )
                               .remove( getSupportFragmentManager( ).findFragmentById( R.id.mainFrag ) )
                               .replace( R.id.mainFrag, new FlightListFragment( ) )
                               .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                               .commit( );
                   } )
                   .setNegativeButton( getResources( ).getString( R.string.dialog_delete_cancel ), ( dialogInterface, i ) -> {
                
                   } );
            AlertDialog dialog = builder.create( );
            dialog.show( );
            return true;
        }
        
        return super.onOptionsItemSelected( item );
    }
    
    @SuppressWarnings( "StatementWithEmptyBody" )
    @Override
    public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId( );
        
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager( );
    
        if( id == R.id.add ) {
            try {
                fragmentManager
                        .beginTransaction( )
                        .replace( R.id.mainFrag, new AddFlightFragment( ) )
                        .addToBackStack( null )
                        .commit( );
            } catch( Exception e ) {
                e.printStackTrace( );
            }
        } else if( id == R.id.myflight ) {
            try {
                fragmentManager
                        .beginTransaction( )
                        .replace( R.id.mainFrag, new FlightListFragment( ) )
                        .addToBackStack( null )
                        .commit( );
            } catch( Exception e ) {
                e.printStackTrace( );
            }
        } else if( id == R.id.statistic ) {
            try {
                fragmentManager
                        .beginTransaction( )
                        .replace( R.id.mainFrag, new StatisticFragment( ) )
                        .addToBackStack( null )
                        .commit( );
            } catch( Exception e ) {
                e.printStackTrace( );
            }
        
        } else if( id == R.id.remember ) {
            try {
                fragmentManager
                        .beginTransaction( )
                        .replace( R.id.mainFrag, new RememberFragment( ) )
                        .addToBackStack( null )
                        .commit( );
            } catch( Exception e ) {
                e.printStackTrace( );
            }
        }
        
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        
        //Contact de l'auteur
        if( id == R.id.contact_author ) {
            String   sujet  = getResources( ).getString( R.string.contact_subject );
            String[] email  = { getResources( ).getString( R.string.contact_email_ArkaSoftWare ) };
            String   titre  = getResources( ).getString( R.string.contact_title );
            Intent   intent = new Intent( Intent.ACTION_SEND );
            intent.setType( "text/html" );
            intent.putExtra( Intent.EXTRA_EMAIL, email );
            intent.putExtra( Intent.EXTRA_SUBJECT, sujet );
            startActivity( Intent.createChooser( intent, titre ) );
        }
        return true;
    }
    
    @Override
    public void onFragmentInteraction( String id ) {
    }
    
    public void showTimePickerDialog( View v ) {
        compDemandeur = v.getTag( ).toString( );
        Button btHeuresDbt = findViewById( R.id.flight_heure_debut );
        //Contrôle de la difference d'heure cohérente avec les heures saisie
        if( !btHeuresDbt.getText( ).toString( ).contains( ":" ) && compDemandeur.equals( "end_hour" ) ) {
            Snackbar.make( getSupportFragmentManager( ).findFragmentById( R.id.mainFrag ).getView( ),
                           getResources( ).getString( R.string.msg_hours_start_before ),
                           Snackbar.LENGTH_LONG ).show( );
            
            return;
        }
        TimePickerFragment timePickerFragment = new TimePickerFragment( );
        timePickerFragment.show( getSupportFragmentManager( ), "timePicker" + v.getTag( ).toString( ) );
    }
    
    @Override
    public void onTimeSet( TimePicker timePicker, int hour, int minute ) {
        Button bt = getSupportFragmentManager( ).findFragmentById( R.id.mainFrag ).getView( ).findViewById( R.id.flight_heure_debut );
        bt.setText( String.format( "%02d:%02d", hour, minute ) );
    }
}