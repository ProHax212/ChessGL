package models;

import graphics.Texture;
import graphics.TextureState;

/**
 * Created by Ryan_Comer on 12/23/2016.
 */
public class ChessSlot {

    private ChessPiece piece;
    private boolean isHighlighted;

    private static Texture highlightTex = new Texture("./res/highlight.png", 0f, 0f);

    public ChessSlot(ChessPiece piece){
        this.piece = piece;
        isHighlighted = false;
    }

    public void render(){
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
