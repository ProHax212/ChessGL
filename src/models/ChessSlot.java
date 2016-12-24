package models;

import graphics.Texture;
import graphics.TextureState;

import java.io.IOException;

/**
 * Created by Ryan_Comer on 12/23/2016.
 */
public class ChessSlot {

    private ChessPiece piece;
    private TextureState highlight;
    private boolean isHighlighted;

    public static float slotDistance = 0.22f;
    public static float xOffset = 0.13f;
    public static float yOffset = 0.31f;

    public ChessSlot(ChessPiece piece, int x, int y){
        this.piece = piece;
        isHighlighted = false;

        try {
            highlight = new TextureState(new Texture("./res/highlight.png", 0f, 0f));
            Texture tex = highlight.getTexture();
            int w=tex.getTexWidth(), h=tex.getTexHeight();
            w*=(800f/1200f); h*=(800f/1200f);
            tex.setX((-1f + x*slotDistance) + xOffset) ;
            tex.setY(1f - yOffset - (y*slotDistance));
            tex.setTexHeight(h);    tex.setTexWidth(w);
            highlight.updateVbo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(){
        if(isHighlighted){
            highlight.render();
        }
        if(piece != null) piece.render();
    }

    public ChessPiece getPiece() {
        return piece;
    }
    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }
}
