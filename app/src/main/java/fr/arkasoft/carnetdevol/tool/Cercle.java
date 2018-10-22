package fr.arkasoft.carnetdevol.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import fr.arkasoft.carnetdevol.R;

public class Cercle extends View {
    
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
    private final Paint      paint       = new Paint( Paint.ANTI_ALIAS_FLAG );
    private       int        nbDeJour;
    private       int        nbDeNuit;
    private       TypeCercle typeDeCercle;
    private final int        padding     = 10;
    private       int        bas;
    private       int        droite;
    private       int        haut;
    private       int        gauche;
    private       RectF      oval1;
    private       int        tailleMax;
    
    public Cercle( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }
    
    public Cercle( Context context ) {
        super( context );
    }
    
    public Cercle( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }
    
    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
        super.onSizeChanged( w, h, oldw, oldh );
        tailleMax = Math.min( getMeasuredWidth( ), getMeasuredHeight( ) );
        centerY = getMeasuredHeight( ) / 2;
        centerX = getMeasuredWidth( ) / 2;
        radius = tailleMax / 2;
        
        gauche = centerX - radius + padding;
        haut = centerY - radius + padding;
        droite = centerX + radius - padding;
        bas = centerY + radius - padding;
        
        oval1 = new RectF( gauche, haut, droite, bas );
    }
    
    public void init( int j, int n, TypeCercle t ) {
        
        this.typeDeCercle = t;
        nbDeJour = j;
        nbDeNuit = n;
        
        int total = j + n;
        if( total > 0 ) {
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
        
        paint.setStyle( Paint.Style.FILL_AND_STROKE );
        paint.setStrokeWidth( padding );
        paint.setColor( Color.DKGRAY );
        /*RectF oval1 = new RectF( gauche, haut, droite, bas );*/
        canvas.drawOval( oval1, paint );
        
        paint.setStyle( Paint.Style.FILL );
        paint.setColor( getResources( ).getColor( R.color.colorPrimaryDark, null ) );
        canvas.drawArc( oval1, 0, degNuit, true, paint );
    
        paint.setColor( getResources( ).getColor( R.color.colorPrimaryLight, null ) );
        canvas.drawArc( oval1, degNuit, degJour, true, paint );
        
        paint.setTextSize( radius / 8f );
        paint.setColor( 0x99FFFFFF );
        
        posText( degNuit );
        
        if( typeDeCercle == TypeCercle.HDV ) {
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_night ), nbDeNuit ), textX, textY, paint );
            canvas.drawText( String.valueOf( percentNuit ) + " %", textX, textY + radius * 0.2f, paint );
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_day ), nbDeJour ), txtJourX, txtJourY, paint );
            canvas.drawText( String.valueOf( percentJour ) + " %", txtJourX, txtJourY + radius * 0.2f, paint );
        } else if( typeDeCercle == TypeCercle.ATT ) {
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_att_night ), nbDeNuit ), textX, textY, paint );
            canvas.drawText( String.valueOf( percentNuit ) + " %", textX, textY + radius * 0.2f, paint );
            canvas.drawText( String.format( getResources( ).getString( R.string.frag_statistic_att_day ), nbDeJour ), txtJourX, txtJourY, paint );
            canvas.drawText( String.valueOf( percentJour ) + " %", txtJourX, txtJourY + radius * 0.2f, paint );
        }
        
        super.onDraw( canvas );
    }
    
    private void posText( int degree ) {
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

