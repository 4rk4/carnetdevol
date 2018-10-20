package fr.arkasoft.carnetdevol.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import fr.arkasoft.carnetdevol.R;

public class Cercle extends View {
    
    private final Context    context     = null;
    private       int        percentNuit = 0;
    private       int        percentJour = 0;
    private       int        degJour     = 0;
    private       int        degNuit     = 0;
    private       int        centerX;
    private       int        centerY;
    private       int        radius;
    private       int        textX;
    private       int        textY;
    private       int        txtJourX;
    private       int        txtJourY;
    private       Paint      paint       = new Paint( Paint.ANTI_ALIAS_FLAG );
    private       int        nbDeJour;
    private       int        nbDeNuit;
    private       int        nbTotal;
    private       TypeCercle typeDeCercle;
    
    public Cercle( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }
    
    public Cercle( Context context ) {
        super( context );
    }
    
    public Cercle( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }
    
    public void init( int j, int n, TypeCercle t ) {
        
        this.typeDeCercle = t;
        
        nbDeJour = j;
        nbDeNuit = n;
        nbTotal = j + n;
        
        int total = j + n;
        if ( total > 0 ) {
            percentJour = j * 100 / total;
            percentNuit = n * 100 / total;
            degJour = j * 360 / total;
            degNuit = n * 360 / total + 1;
        } else {
            percentJour = 0;
            percentNuit = 0;
            degJour = 0;
            degNuit = 0;
        }
        this.invalidate( );
    }
    
    @Override
    protected void onDraw( Canvas canvas ) {
        
        int tailleMax = Math.min( getMeasuredWidth( ), getMeasuredHeight( ) );
        int padding   = 10;
        centerY = getMeasuredHeight( ) / 2;
        centerX = getMeasuredWidth( ) / 2;
        radius = tailleMax / 2;
        
        int gauche = centerX - radius + padding;
        int haut   = centerY - radius + padding;
        int droite = centerX + radius - padding;
        int bas    = centerY + radius - padding;
        
        // Get the screen's density scale
        //        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        //        int textSizePx = (int) (15.0f * scale + 0.5f);
        
        paint.setStyle( Paint.Style.FILL_AND_STROKE );
        paint.setStrokeWidth( padding );
        paint.setColor( Color.DKGRAY );
        RectF oval1 = new RectF( gauche, haut, droite, bas );
        canvas.drawOval( oval1, paint );
        
        paint.setStyle( Paint.Style.FILL );
        paint.setColor( getResources( ).getColor( R.color.colorPrimaryDark ) );
        canvas.drawArc( oval1, 0, degNuit, true, paint );
        
        paint.setColor( getResources( ).getColor( R.color.colorPrimaryLight ) );
        canvas.drawArc( oval1, degNuit, degJour, true, paint );
        
        paint.setTextSize( radius / 8f );
        paint.setColor( 0x99FFFFFF );
        
        posText( degNuit );
        //        StringBuilder strHDN = new StringBuilder(getResources().getString(R.string.frag_statistic_night));
        //        strHDN.append(String.valueOf(nbDeNuit));
        //        strHDN.append(" h");
        //        strHDN.append("\n");
        //        strHDN.append(String.valueOf(percentNuit));
        //        strHDN.append(" %");
        
        //        String strHDN = String.format(getResources().getString(R.string.frag_statistic_night), nbDeNuit, percentNuit);
        if ( typeDeCercle == TypeCercle.HDV ) {
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_night ), nbDeNuit ), textX, textY, paint );
            canvas.drawText( String.valueOf( percentNuit ) + " %", textX, textY + radius * 0.2f, paint );
            
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_day ), nbDeJour ), txtJourX, txtJourY, paint );
            canvas.drawText( String.valueOf( percentJour ) + " %", txtJourX, txtJourY + radius * 0.2f, paint );
            Log.i( "DEBUG CERCLE", "hdv" );
        } else if ( typeDeCercle == TypeCercle.ATT ) {
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_att_night ), nbDeNuit ), textX, textY, paint );
            canvas.drawText( String.valueOf( percentNuit ) + " %", textX, textY + radius * 0.2f, paint );
            
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_att_day ), nbDeJour ), txtJourX, txtJourY, paint );
            canvas.drawText( String.valueOf( percentJour ) + " %", txtJourX, txtJourY + radius * 0.2f, paint );
            Log.i( "DEBUG CERCLE", "Att" );
        }
        
        //        canvas.drawText(getResources().getString(R.string.frag_statistic_day) + " " + String.valueOf(nbDeJour), txtJourX, txtJourY, paint);
        super.onDraw( canvas );
    }
    
    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        int parentWidth  = MeasureSpec.getSize( widthMeasureSpec );
        int parentHeight = MeasureSpec.getSize( heightMeasureSpec );
        this.setMeasuredDimension( parentWidth, parentHeight );
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
    }
    
    public void posText( int degree ) {
        double rad    = radius * 0.4;
        float  deg    = degree / 2;
        float  degRad = ( float ) ( ( deg * Math.PI ) / 180f );
        int    cosX   = ( int ) Math.round( Math.cos( degRad ) * rad );
        int    sinY   = ( int ) Math.round( Math.sin( degRad ) * rad );
        
        textX = centerX + cosX;
        textY = centerY + sinY;
        txtJourX = centerX - cosX;
        txtJourY = centerY - sinY;
    }
    
}

