package fr.arkasoft.carnetdevol;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import fr.arkasoft.carnetdevol.db.FlightDbHelper;

public class CarnetDeVolBackupAgent extends BackupAgentHelper {
    
    private static final String DB_CARNET_DE_VOL    = FlightDbHelper.DATABASE_NAME;
    private static final String FILES_BACKUP_KEY    = "db";
    private static final String PREFS_FILE          = "fragment_setting";
    private static final String MY_PREFS_BACKUP_KEY = "myprefs";
    
    public void onCreate( ) {
        String           path   = getBaseContext( ).getDatabasePath( DB_CARNET_DE_VOL ).getAbsolutePath( );
        FileBackupHelper helper = new FileBackupHelper( this, path );
        addHelper( FILES_BACKUP_KEY, helper );
        
        SharedPreferencesBackupHelper helperPref =
                new SharedPreferencesBackupHelper( this, PREFS_FILE );
        addHelper( MY_PREFS_BACKUP_KEY, helperPref );
    }
    
}
