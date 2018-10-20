/**
 * author: 4rk4
 * url: https://github.com/4rk4/carnetdevol
 * Licence: GPL v3
 * Start: 29 oct 2015
 * 1st publish: 06 nov 2015
 */
package fr.arkasoft.carnetdevol;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

import fr.arkasoft.carnetdevol.db.FlightDbHelper;

public class CarnetDeVolBackupAgent extends BackupAgentHelper {
    
    static final String DB_CARNET_DE_VOL    = FlightDbHelper.DATABASE_NAME;
    static final String FILES_BACKUP_KEY    = "db";
    static final String PREFS_FILE          = "fragment_setting";
    static final String MY_PREFS_BACKUP_KEY = "myprefs";
    
    public void onCreate( ) {
        String           path   = getBaseContext( ).getDatabasePath( DB_CARNET_DE_VOL ).getAbsolutePath( );
        FileBackupHelper helper = new FileBackupHelper( this, path );
        addHelper( FILES_BACKUP_KEY, helper );
        
        SharedPreferencesBackupHelper helperPref =
                new SharedPreferencesBackupHelper( this, PREFS_FILE );
        addHelper( MY_PREFS_BACKUP_KEY, helperPref );
    }
    
    @Override
    public void onBackup( ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState ) throws IOException {
        super.onBackup( oldState, data, newState );
    }
    
    @Override
    public void onRestore( BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState ) throws IOException {
        super.onRestore( data, appVersionCode, newState );
    }
}
