package fr.arkasoft.carnetdevol.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Xml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlSerializer;

import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fr.arkasoft.carnetdevol.R;

public class FlightDbHelper extends SQLiteOpenHelper {

    public static final  int    STAT_TOTAL          = 1;
    public static final  int    STAT_MOIS_EN_COURS  = 2;
    public static final  int    STAT_ANNEE_EN_COURS = 3;
    public static final  String DATABASE_NAME       = "CarnetDeVol.db";
    private static final int    DATABASE_VERSION    = 1;
    private final        String TABLE_NAME          = "flight";
    private final        String COL_ID              = "id";
    private final        String COL_DATE            = "date";
    private final        String COL_HEURE_DEBUT     = "heure_debut";
    private final        String COL_HEURE_FIN       = "heure_fin";
    private final        String COL_AIRCRAFT_TYPE   = "aircraft_type";
    private final        String COL_AIRCRAFT_IMMAT  = "aircraft_immat";
    private final        String COL_FONCTION_BORD   = "fonction_bord"; //PIL - CDT - EP
    private final        String COL_NATURE_VOL      = "nature"; //VFR - IFR - Local - Ent
    private final        String COL_HEURES_JOUR     = "heures_jour";
    private final        String COL_HEURES_NUIT     = "heures_nuit";
    private final        String COL_MULTI_MONO      = "multi_mono";
    private final        String COL_IFR_VFR         = "ifr_vfr";
    private final        String COL_SIMU_VOL        = "simu_vol";
    private final        String COL_ARRIVEES_IFR    = "arrivees_ifr";
    private final        String COL_ATT_JOUR        = "att_jour";
    private final        String COL_ATT_NUIT        = "att_nuit";
    private final        String COL_OBS             = "observation";

    private final Context        context;
    private       SQLiteDatabase db;


    public FlightDbHelper ( Context context ) {

        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
        this.context = context;
    }


    public static String formatDateAffichage ( String dateToShow ) {

        String           dateOut = "";
        SimpleDateFormat sdfOut  = new SimpleDateFormat ( "dd/MM/yyyy HH:mm" );
        SimpleDateFormat sdfIn   = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
        try {
            Date date = sdfIn.parse ( dateToShow );
            dateOut = sdfOut.format ( date );
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }
        return dateOut;
    }


    public static String formatOnlyDateAffichage ( String dateToShow ) {

        String           dateOut = "";
        SimpleDateFormat sdfOut  = new SimpleDateFormat ( "dd/MM/yyyy" );
        SimpleDateFormat sdfIn   = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
        try {
            Date date = sdfIn.parse ( dateToShow );
            dateOut = sdfOut.format ( date );
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }
        return dateOut;
    }


    @Override
    public void onCreate ( SQLiteDatabase sqLiteDatabase ) {

        String TEXT_TYPE    = " TEXT";
        String INT_TYPE     = " INTEGER";
        String NUMERIC_TYPE = " NUMERIC";
        String COMMA_SEP    = ",";
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COL_DATE + TEXT_TYPE + COMMA_SEP +
                COL_HEURE_DEBUT + TEXT_TYPE + COMMA_SEP +
                COL_HEURE_FIN + TEXT_TYPE + COMMA_SEP +
                COL_AIRCRAFT_TYPE + TEXT_TYPE + COMMA_SEP +
                COL_AIRCRAFT_IMMAT + TEXT_TYPE + COMMA_SEP +
                COL_FONCTION_BORD + TEXT_TYPE + COMMA_SEP +
                COL_NATURE_VOL + TEXT_TYPE + COMMA_SEP +
                COL_HEURES_JOUR + TEXT_TYPE + COMMA_SEP +
                COL_HEURES_NUIT + TEXT_TYPE + COMMA_SEP +
                COL_MULTI_MONO + NUMERIC_TYPE + COMMA_SEP +
                COL_IFR_VFR + NUMERIC_TYPE + COMMA_SEP +
                COL_SIMU_VOL + NUMERIC_TYPE + COMMA_SEP +
                COL_ARRIVEES_IFR + TEXT_TYPE + COMMA_SEP +
                COL_ATT_JOUR + INT_TYPE + COMMA_SEP +
                COL_ATT_NUIT + INT_TYPE + COMMA_SEP +
                COL_OBS + TEXT_TYPE +
                " )";
        sqLiteDatabase.execSQL ( SQL_CREATE_ENTRIES );
    }


    @Override
    public void onUpgrade ( SQLiteDatabase sqLiteDatabase, int i, int i1 ) {

    }


    public int getNumberOfFlight ( ) {
        //int nb = 0;
        db = getReadableDatabase ( );
        Cursor c = db.rawQuery ( "select " + COL_ID + " from " + TABLE_NAME, null );

        c.moveToFirst ( );
        int nb = c.getCount ( );
        c.close ( );
        db.close ( );

        return nb;
    }


    public Flight getOneFlightDb ( int id ) {

        db = getReadableDatabase ( );
        // Séléction d'un élément dont l'id est
        String whereClause = COL_ID + " = ?";
        String[] whereArgs = new String[] {
                Integer.toString ( id )
        };

        Cursor c = db.query (
                TABLE_NAME,  // The table to query
                null,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null // The sort order
                            );
        c.moveToFirst ( );

        boolean multi = true;
        boolean ifr   = true;
        boolean simu  = true;

        if ( c.getInt ( c.getColumnIndexOrThrow ( COL_MULTI_MONO ) ) == 0 ) {
            multi = false;
        }
        if ( c.getInt ( c.getColumnIndexOrThrow ( COL_IFR_VFR ) ) == 0 ) {
            ifr = false;
        }
        if ( c.getInt ( c.getColumnIndexOrThrow ( COL_SIMU_VOL ) ) == 0 ) {
            simu = false;
        }

        //Flight(String date, String heureDebut, String heureFin, String aircraft_type, String aircraft_immat, String fonction_bord, String nature_vol, ...)
        Flight flight = new Flight ( c.getInt ( c.getColumnIndexOrThrow ( COL_ID ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_DATE ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_DEBUT ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_FIN ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_TYPE ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_IMMAT ) ),
                                     affFonction ( c.getString ( c.getColumnIndexOrThrow ( COL_FONCTION_BORD ) ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_NATURE_VOL ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_JOUR ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_NUIT ) ),
                                     multi,
                                     ifr,
                                     simu,
                                     c.getString ( c.getColumnIndexOrThrow ( COL_ARRIVEES_IFR ) ),
                                     c.getInt ( c.getColumnIndexOrThrow ( COL_ATT_JOUR ) ),
                                     c.getInt ( c.getColumnIndexOrThrow ( COL_ATT_NUIT ) ),
                                     c.getString ( c.getColumnIndexOrThrow ( COL_OBS ) ) );
        c.close ( );
        db.close ( );
        return flight;
    }


    private String affFonction ( String fonc ) {

        if ( fonc.length ( ) < 2 ) {
            String[] fonctions = this.context.getResources ( ).getStringArray ( R.array.fonction_bord );
            return fonctions[ Integer.valueOf ( fonc ) ];
        } else {
            return fonc;
        }
    }


    public int updateFlight ( Flight flight, int idFlight ) {
        // Gets the data repository in write mode
        db = getWritableDatabase ( );

        //Formattage des champs vide
        if ( flight.getObs ( ).equalsIgnoreCase ( "" ) ) {
            flight.setObs ( context.getResources ( ).getString ( R.string.msg_ras ) );
        }

        ContentValues values = new ContentValues ( );
        values.put ( COL_HEURE_DEBUT, flight.getHeureDebut ( ) );
        values.put ( COL_HEURE_FIN, flight.getHeureFin ( ) );
        values.put ( COL_DATE, flight.getDate ( ) );
        values.put ( COL_AIRCRAFT_TYPE, flight.getAircraft_type ( ) );
        values.put ( COL_AIRCRAFT_IMMAT, flight.getAircraft_immat ( ) );
        values.put ( COL_FONCTION_BORD, flight.getFonction_bord ( ) );
        values.put ( COL_NATURE_VOL, flight.getNature_vol ( ) );
        values.put ( COL_HEURES_JOUR, flight.getHeuresJour ( ) );
        values.put ( COL_HEURES_NUIT, flight.getHeuresNuit ( ) );
        values.put ( COL_MULTI_MONO, flight.isMulti_mono ( ) );
        values.put ( COL_IFR_VFR, flight.isIfr_vfr ( ) );
        values.put ( COL_SIMU_VOL, flight.isSimu_vol ( ) );
        values.put ( COL_ARRIVEES_IFR, flight.getArrivees_ifr ( ) );
        values.put ( COL_ATT_JOUR, flight.getAtt_jour ( ) );
        values.put ( COL_ATT_NUIT, flight.getAtt_nuit ( ) );
        values.put ( COL_OBS, flight.getObs ( ) );

        int newRowId = db.update (
                TABLE_NAME,
                values,
                COL_ID + "=" + idFlight,
                null );
        db.close ( );
        return newRowId;
    }


    public ArrayList < Flight > getAllFlight ( ) {

        ArrayList < Flight > ensembleDesVols = new ArrayList <> ( );

        db = getReadableDatabase ( );
        Cursor c = db.rawQuery ( "SELECT * FROM flight ORDER BY datetime(" + COL_DATE + ") DESC;", null );
        c.moveToFirst ( );

        //Contrôle si aucun enregistrement renvoyé
        if ( c.getCount ( ) > 0 ) {
            do {
                int    dbId         = c.getInt ( c.getColumnIndexOrThrow ( COL_ID ) );
                String dbHeuresJour = c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_JOUR ) );
                String dbHeuresNuit = c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_NUIT ) );
                String date         = c.getString ( c.getColumnIndexOrThrow ( COL_DATE ) );
                Flight oneFlight    = new Flight ( dbId, date, dbHeuresJour, dbHeuresNuit );
                oneFlight.setNature_vol ( c.getString ( c.getColumnIndexOrThrow ( COL_NATURE_VOL ) ) );
                oneFlight.setAircraft_immat ( c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_IMMAT ) ) );
                oneFlight.setAircraft_type ( c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_TYPE ) ) );
                oneFlight.setHeureFin ( c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_FIN ) ) );
                ensembleDesVols.add ( oneFlight );
            } while ( c.moveToNext ( ) );
        }
        c.close ( );
        db.close ( );

        return ensembleDesVols;
    }


    public HashMap getStatVol ( int TYPE_STAT, String functionSelect ) {

        db = getReadableDatabase ( );

        Calendar calendar           = Calendar.getInstance ( );
        int      currentMonth       = calendar.get ( Calendar.MONTH ) + 1; // Janvier étant à 0
        int      currentYear        = calendar.get ( Calendar.YEAR );
        String   currentMonthFormat = String.format ( "%02d", currentMonth );

        String where = null;
        String[] projection = new String[] {
                COL_DATE,
                COL_HEURES_JOUR,
                COL_HEURES_NUIT,
                COL_ATT_JOUR,
                COL_ATT_NUIT,
                COL_FONCTION_BORD
        };

        switch ( TYPE_STAT ) {
            case STAT_MOIS_EN_COURS:
                where = " strftime('%m', " + COL_DATE + ") = '" + currentMonthFormat
                        + "' and strftime('%Y', " + COL_DATE + ") = '" + currentYear + "'"
                        + " and " + COL_FONCTION_BORD + "=\"" + functionSelect + "\"";
                break;
            case STAT_TOTAL:
                where = COL_FONCTION_BORD + "=\"" + functionSelect + "\"";
                break;
            case STAT_ANNEE_EN_COURS:
                where = " strftime('%Y', " + COL_DATE + ") = '" + currentYear + "'"
                        + " and " + COL_FONCTION_BORD + "=\"" + functionSelect + "\"";
                break;
        }

        Cursor c = db.query (
                TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null // The sort order
                            );
        c.moveToFirst ( );

        float heuresJourTotal = 0;
        float heuresNuitTotal = 0;
        float attJour         = 0f;
        float attNuit         = 0f;
        //int endCursor = c.getCount();
        if ( c.getCount ( ) > 0 ) {
            do {
                String col_heures_jour = c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_JOUR ) );
                String col_heures_nuit = c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_NUIT ) );

                if ( col_heures_jour.contains ( ":" ) ) {
                    String[] heuresJour = col_heures_jour.split ( ":" );
                    heuresJourTotal += Float.valueOf ( heuresJour[ 0 ] ) + Float.valueOf ( heuresJour[ 1 ] ) / 60;
                }
                if ( col_heures_nuit.contains ( ":" ) ) {
                    String[] heuresNuit = col_heures_nuit.split ( ":" );
                    heuresNuitTotal += Float.valueOf ( heuresNuit[ 0 ] ) + Float.valueOf ( heuresNuit[ 1 ] ) / 60;
                }
                attJour += c.getFloat ( c.getColumnIndexOrThrow ( COL_ATT_JOUR ) );
                attNuit += c.getFloat ( c.getColumnIndexOrThrow ( COL_ATT_NUIT ) );

            } while ( c.moveToNext ( ) );
        }

        HashMap lesStats = new HashMap ( );
        lesStats.put ( "jour", heuresJourTotal );
        lesStats.put ( "nuit", heuresNuitTotal );
        lesStats.put ( "attjour", attJour );
        lesStats.put ( "attnuit", attNuit );
        lesStats.put ( "nbvol", ( float ) c.getCount ( ) );

        c.close ( );
        db.close ( );
        return lesStats;
    }


    public long importFlight ( String carnet ) {

        Document doc;
        long     isInserted = 0;
        try {
            doc = Jsoup.parse ( carnet );
            Elements allFlights = doc.select ( "flight" );
            for ( Element e : allFlights ) {
                isInserted = insertFlight ( new Flight (
                        e.getElementsByTag ( "COL_DATE" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_HEURE_DEBUT" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_HEURE_FIN" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_AIRCRAFT_TYPE" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_AIRCRAFT_IMMAT" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_FONCTION_BORD" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_NATURE_VOL" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_HEURES_JOUR" ).first ( ).ownText ( ),
                        e.getElementsByTag ( "COL_HEURES_NUIT" ).first ( ).ownText ( ),
                        Boolean.valueOf ( e.getElementsByTag ( "COL_MULTI_MONO" ).first ( ).ownText ( ) ),
                        Boolean.valueOf ( e.getElementsByTag ( "COL_IFR_VFR" ).first ( ).ownText ( ) ),
                        Boolean.valueOf ( e.getElementsByTag ( "COL_SIMU_VOL" ).first ( ).ownText ( ) ),
                        e.getElementsByTag ( "COL_ARRIVEES_IFR" ).first ( ).ownText ( ),
                        Integer.valueOf ( e.getElementsByTag ( "COL_ATT_JOUR" ).first ( ).ownText ( ) ),
                        Integer.valueOf ( e.getElementsByTag ( "COL_ATT_NUIT" ).first ( ).ownText ( ) ),
                        e.getElementsByTag ( "COL_OBS" ).first ( ).ownText ( ) ) );
            }
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }
        return isInserted;
    }


    public long insertFlight ( Flight flight ) {
        // Gets the data repository in write mode
        db = getWritableDatabase ( );

        //Formattage des champs vide
        if ( flight.getObs ( ).equalsIgnoreCase ( "" ) ) {
            flight.setObs ( context.getResources ( ).getString ( R.string.msg_ras ) );
        }

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues ( );
        values.put ( COL_HEURE_DEBUT, flight.getHeureDebut ( ) );
        values.put ( COL_HEURE_FIN, flight.getHeureFin ( ) );
        values.put ( COL_DATE, flight.getDate ( ) );
        values.put ( COL_AIRCRAFT_TYPE, flight.getAircraft_type ( ) );
        values.put ( COL_AIRCRAFT_IMMAT, flight.getAircraft_immat ( ) );
        values.put ( COL_FONCTION_BORD, flight.getFonction_bord ( ) );
        values.put ( COL_NATURE_VOL, flight.getNature_vol ( ) );
        values.put ( COL_HEURES_JOUR, flight.getHeuresJour ( ) );
        values.put ( COL_HEURES_NUIT, flight.getHeuresNuit ( ) );
        values.put ( COL_MULTI_MONO, flight.isMulti_mono ( ) );
        values.put ( COL_IFR_VFR, flight.isIfr_vfr ( ) );
        values.put ( COL_SIMU_VOL, flight.isSimu_vol ( ) );
        values.put ( COL_ARRIVEES_IFR, flight.getArrivees_ifr ( ) );
        values.put ( COL_ATT_JOUR, flight.getAtt_jour ( ) );
        values.put ( COL_ATT_NUIT, flight.getAtt_nuit ( ) );
        values.put ( COL_OBS, flight.getObs ( ) );

        // Insert the new row, returning the primary key value of the new row
        long newRowId = 0;
        try {
            newRowId = db.insert (
                    TABLE_NAME,
                    null,
                    values );
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }
        db.close ( );

        return newRowId;
    }


    public boolean export ( ) {

        XmlSerializer xmlSerialize = Xml.newSerializer ( );
        StringWriter  writer       = new StringWriter ( );
        try {
            xmlSerialize.setOutput ( writer );
            xmlSerialize.startDocument ( "UTF-8", true );
            xmlSerialize.setFeature ( "http://xmlpull.org/v1/doc/features.html#indent-output", true );
            xmlSerialize.startTag ( "", "flights" );
            db = getReadableDatabase ( );
            Cursor c = db.rawQuery ( "SELECT * FROM flight;", null );
            if ( c.moveToFirst ( ) ) {
                do {
                    xmlSerialize.startTag ( "", "flight" );
                    xmlSerialize.startTag ( "", "COL_DATE" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_DATE ) ) );
                    xmlSerialize.endTag ( "", "COL_DATE" );
                    xmlSerialize.startTag ( "", "COL_HEURE_DEBUT" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_DEBUT ) ) );
                    xmlSerialize.endTag ( "", "COL_HEURE_DEBUT" );
                    xmlSerialize.startTag ( "", "COL_HEURE_FIN" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_FIN ) ) );
                    xmlSerialize.endTag ( "", "COL_HEURE_FIN" );
                    xmlSerialize.startTag ( "", "COL_AIRCRAFT_TYPE" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_TYPE ) ) );
                    xmlSerialize.endTag ( "", "COL_AIRCRAFT_TYPE" );
                    xmlSerialize.startTag ( "", "COL_AIRCRAFT_IMMAT" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_IMMAT ) ) );
                    xmlSerialize.endTag ( "", "COL_AIRCRAFT_IMMAT" );
                    xmlSerialize.startTag ( "", "COL_FONCTION_BORD" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_FONCTION_BORD ) ) );
                    xmlSerialize.endTag ( "", "COL_FONCTION_BORD" );
                    xmlSerialize.startTag ( "", "COL_NATURE_VOL" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_NATURE_VOL ) ) );
                    xmlSerialize.endTag ( "", "COL_NATURE_VOL" );
                    xmlSerialize.startTag ( "", "COL_HEURES_JOUR" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_JOUR ) ) );
                    xmlSerialize.endTag ( "", "COL_HEURES_JOUR" );
                    xmlSerialize.startTag ( "", "COL_HEURES_NUIT" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_NUIT ) ) );
                    xmlSerialize.endTag ( "", "COL_HEURES_NUIT" );
                    xmlSerialize.startTag ( "", "COL_MULTI_MONO" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_MULTI_MONO ) ) );
                    xmlSerialize.endTag ( "", "COL_MULTI_MONO" );
                    xmlSerialize.startTag ( "", "COL_IFR_VFR" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_IFR_VFR ) ) );
                    xmlSerialize.endTag ( "", "COL_IFR_VFR" );
                    xmlSerialize.startTag ( "", "COL_SIMU_VOL" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_SIMU_VOL ) ) );
                    xmlSerialize.endTag ( "", "COL_SIMU_VOL" );
                    xmlSerialize.startTag ( "", "COL_ARRIVEES_IFR" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_ARRIVEES_IFR ) ) );
                    xmlSerialize.endTag ( "", "COL_ARRIVEES_IFR" );
                    xmlSerialize.startTag ( "", "COL_ATT_JOUR" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_ATT_JOUR ) ) );
                    xmlSerialize.endTag ( "", "COL_ATT_JOUR" );
                    xmlSerialize.startTag ( "", "COL_ATT_NUIT" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_ATT_NUIT ) ) );
                    xmlSerialize.endTag ( "", "COL_ATT_NUIT" );
                    xmlSerialize.startTag ( "", "COL_OBS" );
                    xmlSerialize.text ( c.getString ( c.getColumnIndexOrThrow ( COL_OBS ) ) );
                    xmlSerialize.endTag ( "", "COL_OBS" );
                    xmlSerialize.endTag ( "", "flight" );
                } while ( c.moveToNext ( ) );
            }
            xmlSerialize.endTag ( "", "flights" );
            xmlSerialize.endDocument ( );
            c.close ( );
            db.close ( );
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }

        boolean ret = false;

        String filename = context.getResources ( ).getString ( R.string.flight_db_help_export_name );
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter (
                    context.openFileOutput ( filename, Context.MODE_PRIVATE ) );
            outputStreamWriter.write ( writer.toString ( ) );
            outputStreamWriter.close ( );
            ret = true;
        } catch ( Exception e ) {
            e.printStackTrace ( );
        }

        Intent sendIntent = new Intent ( );
        sendIntent.setAction ( Intent.ACTION_SEND );
        sendIntent.putExtra ( Intent.EXTRA_TITLE, "Carnet de vol" );
        sendIntent.putExtra ( Intent.EXTRA_SUBJECT, "Export du carnet de vol" );
        sendIntent.putExtra ( Intent.EXTRA_TEXT, writer.toString ( ) );
        sendIntent.setType ( "text/xml" );
        context.startActivity ( sendIntent );

        return ret;
    }


    public boolean deleteOneFlight ( int id ) {

        db = getWritableDatabase ( );
        boolean res = db.delete ( TABLE_NAME, COL_ID + "=" + id, null ) > 0;
        db.close ( );
        return res;
    }


    public ArrayList < String > getTypeAvion ( ) {

        //Ici on ajoute des élément par défault
        String[]             defaultTypeAvion = context.getResources ( ).getStringArray ( R.array.type_avion );
        ArrayList < String > typeAvion        = new ArrayList <> ( Arrays.asList ( defaultTypeAvion ) );

        db = getReadableDatabase ( );

        String[] projection = new String[] {
                COL_AIRCRAFT_TYPE
        };

        Cursor c = db.query ( true,
                              TABLE_NAME,  // The table to query
                              projection,                               // The columns to return
                              null,                                // The columns for the WHERE clause
                              null,                            // The values for the WHERE clause
                              COL_AIRCRAFT_TYPE,                                     // don't group the rows
                              null,                                     // don't filter by row groups
                              COL_AIRCRAFT_TYPE,
                              null
                            );
        c.moveToFirst ( );

        //Contrôle si aucun enregistrement renvoyé
        if ( c.getCount ( ) > 0 ) {
            do {
                typeAvion.add ( c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_TYPE ) ) );
            } while ( c.moveToNext ( ) );
        }
        c.close ( );
        db.close ( );

        return typeAvion;
    }


    public ArrayList < String > getNature ( ) {
        //Ici on ajoute des élément par défault
        String[]             defaultTab = context.getResources ( ).getStringArray ( R.array.nature_vol );
        ArrayList < String > tab        = new ArrayList <> ( Arrays.asList ( defaultTab ) );

        db = getReadableDatabase ( );

        String[] projection = new String[] {
                COL_NATURE_VOL
        };

        Cursor c = db.query ( true,
                              TABLE_NAME,  // The table to query
                              projection,                               // The columns to return
                              null,                                // The columns for the WHERE clause
                              null,                            // The values for the WHERE clause
                              COL_NATURE_VOL,                                     // don't group the rows
                              null,                                     // don't filter by row groups
                              COL_NATURE_VOL,
                              null
                            );
        c.moveToFirst ( );

        //Contrôle si aucun enregistrement renvoyé
        if ( c.getCount ( ) > 0 ) {
            do {
                tab.add ( c.getString ( c.getColumnIndexOrThrow ( COL_NATURE_VOL ) ) );
            } while ( c.moveToNext ( ) );
        }
        c.close ( );
        db.close ( );
        return tab;
    }


    public ArrayList < String > getImmat ( ) {

        ArrayList < String > tab = new ArrayList <> ( );

        db = getReadableDatabase ( );
        String[] projection = new String[] {
                COL_AIRCRAFT_IMMAT
        };

        Cursor c = db.query ( true,
                              TABLE_NAME,  // The table to query
                              projection,                               // The columns to return
                              null,                                // The columns for the WHERE clause
                              null,                            // The values for the WHERE clause
                              COL_AIRCRAFT_IMMAT,                                     // don't group the rows
                              null,                                     // don't filter by row groups
                              COL_AIRCRAFT_IMMAT,
                              null
                            );
        c.moveToFirst ( );

        //Contrôle si aucun enregistrement renvoyé
        if ( c.getCount ( ) > 0 ) {
            do {
                tab.add ( c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_IMMAT ) ) );
            } while ( c.moveToNext ( ) );
        }
        c.close ( );
        db.close ( );
        return tab;
    }


    public ArrayList < String > getArriveesIFR ( ) {
        //Ici on ajoute des élément par défault
        String[]             defaultTab = context.getResources ( ).getStringArray ( R.array.arrivees_ifr );
        ArrayList < String > tab        = new ArrayList <> ( Arrays.asList ( defaultTab ) );

        db = getReadableDatabase ( );

        String[] projection = new String[] {
                COL_ARRIVEES_IFR
        };

        Cursor c = db.query ( true,
                              TABLE_NAME,  // The table to query
                              projection,                               // The columns to return
                              null,                                // The columns for the WHERE clause
                              null,                            // The values for the WHERE clause
                              null,                                     // don't group the rows
                              null,                                     // don't filter by row groups
                              COL_ARRIVEES_IFR,
                              null
                            );
        c.moveToFirst ( );

        //Contrôle si aucun enregistrement renvoyé
        if ( c.getCount ( ) > 0 ) {
            do {
                tab.add ( c.getString ( c.getColumnIndexOrThrow ( COL_ARRIVEES_IFR ) ) );
            } while ( c.moveToNext ( ) );
        }
        c.close ( );
        db.close ( );
        return tab;
    }


    public boolean deleteAllFlight ( ) {

        db = getWritableDatabase ( );
        boolean res = db.delete ( TABLE_NAME, null, null ) > 0;
        db.close ( );
        return res;
    }


    public String toString ( ) {

        db = getReadableDatabase ( );
        Cursor c = db.rawQuery ( "SELECT * FROM flight ORDER BY datetime(" + COL_DATE + ") DESC;", null );
        c.moveToFirst ( );
        StringBuilder affichage = new StringBuilder ( );
        affichage.append ( "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" );
        affichage.append ( "+ ID +------- DATE -------+HDEBUT+ HFIN + JOUR + NUIT +------- NATURE -----+ IMMAT + TYPE +------- FUNCT ------+ IFR + SIMU + MULTI + ATTJ + ATTN + ARRIV +----- OBS -----+\n" );
        affichage.append ( "+----+--------------------+------+------+------+------+--------------------+-------+------+--------------------+-----+------+-------+------+------+-------+---------------+\n" );
        if ( c.getCount ( ) > 0 ) {
            do {
                affichage.append ( String.format ( "|%-4s|", c.getInt ( c.getColumnIndexOrThrow ( COL_ID ) ) ) );
                affichage.append ( String.format ( "%-20s|", c.getString ( c.getColumnIndexOrThrow ( COL_DATE ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_DEBUT ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_HEURE_FIN ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_JOUR ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_HEURES_NUIT ) ) ) );
                affichage.append ( String.format ( "%-20s|", c.getString ( c.getColumnIndexOrThrow ( COL_NATURE_VOL ) ) ) );
                affichage.append ( String.format ( "%-7s|", c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_IMMAT ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_AIRCRAFT_TYPE ) ) ) );
                affichage.append ( String.format ( "%-20s|", c.getString ( c.getColumnIndexOrThrow ( COL_FONCTION_BORD ) ) ) );
                affichage.append ( String.format ( "%-5s|", c.getString ( c.getColumnIndexOrThrow ( COL_IFR_VFR ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_SIMU_VOL ) ) ) );
                affichage.append ( String.format ( "%-7s|", c.getString ( c.getColumnIndexOrThrow ( COL_MULTI_MONO ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_ATT_JOUR ) ) ) );
                affichage.append ( String.format ( "%-6s|", c.getString ( c.getColumnIndexOrThrow ( COL_ATT_NUIT ) ) ) );
                affichage.append ( String.format ( "%-7s|", c.getString ( c.getColumnIndexOrThrow ( COL_ARRIVEES_IFR ) ) ) );
                affichage.append ( String.format ( "%-15s|", c.getString ( c.getColumnIndexOrThrow ( COL_OBS ) ) ) );
                affichage.append ( "\n+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------+\n" );
            } while ( c.moveToNext ( ) );
        }
        c.close ( );
        db.close ( );
        return affichage.toString ( );
    }
}
