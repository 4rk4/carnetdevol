package fr.arkasoft.carnetdevol.db;

public class Flight {

    private int     id;
    private String  date;
    private String  fonction_bord;
    // PILOTE - CAPTAIN - STUDENT PILOT - CREW MEMBER - INSTRUCTOR
    private String  nature_vol     = "-";
    // TRUE:SIMU - FALSE: FLIGNT
    private String  arrivees_ifr   = "-";
    private String  heureDebut     = "00:00";
    private String  heureFin       = "00:00";
    private String  aircraft_type  = "-";
    private String  aircraft_immat = "-";
    //VFR / IFR / LOCAL / TRAINING
    private String  heuresJour     = "00:00";
    private String  heuresNuit;
    private boolean multi_mono     = false;
    // True Multi / false Mono
    private boolean ifr_vfr        = false;
    //TRUE:IFR / FALSE:VFR
    private boolean simu_vol       = false;
    private int     att_jour       = 0;
    private int     att_nuit       = 0;
    private String  obs            = "RAS";


    public Flight ( int id, String date, String heureDebut, String heureFin, String aircraft_type, String aircraft_immat, String fonction_bord, String nature_vol, String heuresJour, String heuresNuit, boolean multi_mono, boolean ifr_vfr, boolean simu_vol, String arrivees_ifr, int att_jour, int att_nuit, String obs ) {

        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.aircraft_type = aircraft_type;
        this.aircraft_immat = aircraft_immat;
        this.fonction_bord = fonction_bord;
        this.nature_vol = nature_vol;
        this.heuresJour = heuresJour;
        this.heuresNuit = heuresNuit;
        this.multi_mono = multi_mono;
        this.ifr_vfr = ifr_vfr;
        this.simu_vol = simu_vol;
        this.arrivees_ifr = arrivees_ifr;
        this.att_jour = att_jour;
        this.att_nuit = att_nuit;
        this.obs = obs;
    }


    public Flight ( String date, String heureDebut, String heureFin, String aircraft_type, String aircraft_immat, String fonction_bord, String nature_vol, String heuresJour, String heuresNuit, boolean multi_mono, boolean ifr_vfr, boolean simu_vol, String arrivees_ifr, int att_jour, int att_nuit, String obs ) {

        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.aircraft_type = aircraft_type;
        this.aircraft_immat = aircraft_immat;
        this.fonction_bord = fonction_bord;
        this.nature_vol = nature_vol;
        this.heuresJour = heuresJour;
        this.heuresNuit = heuresNuit;
        this.multi_mono = multi_mono;
        this.ifr_vfr = ifr_vfr;
        this.simu_vol = simu_vol;
        this.arrivees_ifr = arrivees_ifr;
        this.att_jour = att_jour;
        this.att_nuit = att_nuit;
        this.obs = obs;
    }


    public Flight ( int id, String date, String heuresJour, String heuresNuit ) {

        this.id = id;
        this.date = date;
        this.heuresJour = heuresJour;
        this.heuresNuit = heuresNuit;
    }


    public int getId ( ) {

        return id;
    }


    public void setId ( int id ) {

        this.id = id;
    }


    public String getDate ( ) {

        return date;
    }


    public void setDate ( String date ) {

        this.date = date;
    }


    public String getHeureDebut ( ) {

        return heureDebut;
    }


    public void setHeureDebut ( String heureDebut ) {

        this.heureDebut = heureDebut;
    }


    public String getHeureFin ( ) {

        return heureFin;
    }


    public void setHeureFin ( String heureFin ) {

        this.heureFin = heureFin;
    }


    public String getAircraft_type ( ) {

        return aircraft_type;
    }


    public void setAircraft_type ( String aircraft_type ) {

        this.aircraft_type = aircraft_type;
    }


    public String getAircraft_immat ( ) {

        return aircraft_immat;
    }


    public void setAircraft_immat ( String aircraft_immat ) {

        this.aircraft_immat = aircraft_immat;
    }


    public String getFonction_bord ( ) {

        return fonction_bord;

    }


    public void setFonction_bord ( String fonction_bord ) {

        this.fonction_bord = fonction_bord;
    }


    public String getNature_vol ( ) {

        return nature_vol;
    }


    public void setNature_vol ( String nature_vol ) {

        this.nature_vol = nature_vol;
    }


    public String getHeuresJour ( ) {

        return heuresJour;
    }


    public void setHeuresJour ( String heuresJour ) {

        this.heuresJour = heuresJour;
    }


    public String getHeuresNuit ( ) {

        return heuresNuit;
    }


    public void setHeuresNuit ( String heuresNuit ) {

        this.heuresNuit = heuresNuit;
    }


    public boolean isMulti_mono ( ) {

        return multi_mono;
    }


    public void setMulti_mono ( boolean multi_mono ) {

        this.multi_mono = multi_mono;
    }


    public boolean isIfr_vfr ( ) {

        return ifr_vfr;
    }


    public void setIfr_vfr ( boolean ifr_vfr ) {

        this.ifr_vfr = ifr_vfr;
    }


    public boolean isSimu_vol ( ) {

        return simu_vol;
    }


    public void setSimu_vol ( boolean simu_vol ) {

        this.simu_vol = simu_vol;
    }


    public String getArrivees_ifr ( ) {

        return arrivees_ifr;
    }


    public void setArrivees_ifr ( String arrivees_ifr ) {

        this.arrivees_ifr = arrivees_ifr;
    }


    public int getAtt_jour ( ) {

        return att_jour;
    }


    public void setAtt_jour ( int att_jour ) {

        this.att_jour = att_jour;
    }


    public int getAtt_nuit ( ) {

        return att_nuit;
    }


    public void setAtt_nuit ( int att_nuit ) {

        this.att_nuit = att_nuit;
    }


    public String getObs ( ) {

        return obs;
    }


    public void setObs ( String obs ) {

        this.obs = obs;
    }


    public String toString ( ) {

        @SuppressWarnings ( "StringBufferReplaceableByString" ) StringBuilder affichage = new StringBuilder ( );
        affichage.append ( "---------------------------------------\n" );
        affichage.append ( String.format ( "id          : %d\n", this.id ) );
        affichage.append ( String.format ( "date        : %s\n", this.date ) );
        affichage.append ( String.format ( "HeureDebut  : %s\n", this.heureDebut ) );
        affichage.append ( String.format ( "HeureFin    : %s\n", this.heureFin ) );
        affichage.append ( String.format ( "heureJour   : %s\n", this.heuresJour ) );
        affichage.append ( String.format ( "heuresNuit  : %s\n", this.heuresNuit ) );
        affichage.append ( String.format ( "nature      : %s\n", this.nature_vol ) );
        affichage.append ( String.format ( "immat       : %s\n", this.aircraft_immat ) );
        affichage.append ( String.format ( "type        : %s\n", this.aircraft_type ) );
        affichage.append ( String.format ( "fonction    : %s\n", this.fonction_bord ) );
        affichage.append ( String.format ( "ifr         : %s\n", this.ifr_vfr ) );
        affichage.append ( String.format ( "simu        : %s\n", this.simu_vol ) );
        affichage.append ( String.format ( "multimot    : %s\n", this.multi_mono ) );
        affichage.append ( String.format ( "attJour     : %s\n", this.att_jour ) );
        affichage.append ( String.format ( "att_nuit    : %s\n", this.att_nuit ) );
        affichage.append ( String.format ( "arriveesIfr : %s\n", this.arrivees_ifr ) );
        affichage.append ( String.format ( "obs         : %s\n", this.obs ) );
        affichage.append ( "---------------------------------------" );

        return affichage.toString ( );
    }
}
