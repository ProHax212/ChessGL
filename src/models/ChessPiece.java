package models;

import graphics.Texture;
import graphics.TextureState;

import java.io.IOException;

import static models.ChessSlot.slotDistance;
import static models.ChessSlot.xOffset;
import static models.ChessSlot.yOffset;

/**
 * Created by Ryan_Comer on 12/22/2016.
 */
public class ChessPiece {

    private TextureState textureState;
    private int x, y;
    private Type type;
    private int player;

    private boolean pawnFirstMove;

    public ChessPiece(int player, Type t, int x, int y, int width, int height){
        type = t;
        this.x = x;
        this.y = y;
        this.player = player;
        this.pawnFirstMove = true;

        float texX=0f, texY=0f;

        Texture tex = new Texture("./res/ch1.png", 0, 0);
        String basePath = "./res/chess_sprites/";
        switch(t){
            case PAWN:
                if(player == 0) tex = new Texture(basePath+"blackPawn.png", texX, texY);
                else tex = new Texture(basePath+"whitePawn.png", texX, texY);
                break;
            case ROOK:
                if(player == 0) tex = new Texture(basePath+"blackRook.png", texX, texY);
                else tex = new Texture(basePath+"whiteRook.png", texX, texY);
                break;
            case KNIGHT:
                if(player == 0) tex = new Texture(basePath+"blackKnight.png", texX, texY);
                else tex = new Texture(basePath+"whiteKnight.png", texX, texY);
                break;
            case BISHOP:
                if(player == 0) tex = new Texture(basePath+"blackBishop.png", texX, texY);
                else tex = new Texture(basePath+"whiteBishop.png", texX, texY);
                break;
            case QUEEN:
                if(player == 0) tex = new Texture(basePath+"blackQueen.png", texX, texY);
                else tex = new Texture(basePath+"whiteQueen.png", texX, texY);
                break;
            case KING:
                if(player == 0) tex = new Texture(basePath+"blackKing.png", texX, texY);
                else tex = new Texture(basePath+"whiteKing.png", texX, texY);
                break;
        }

        try {
            textureState = new TextureState(tex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tex.setX((-1f + x*slotDistance) + xOffset) ;
        tex.setY(1f - yOffset - (y*slotDistance));

        tex.setTexHeight((int)(tex.getTexHeight()*1.4));
        tex.setTexWidth((int)(tex.getTexWidth()*1.4));
        textureState.updateVbo();
    }

    public void render(){
        textureState.render();
    }

    public void updateTexPos(){
        Texture tex = textureState.getTexture();

        tex.setX((-1f + x*slotDistance) + xOffset) ;
        tex.setY(1f - yOffset - (y*slotDistance));
        textureState.updateVbo();
    }

    public String toString(){
        return "X: " + this.x + "\tY: " + this.y + "\tType: " + this.type;
    }

    public static enum Type{
        KING,
        QUEEN,
        ROOK,
        BISHOP,
        KNIGHT,
        PAWN
    }

    public Type getType() {
        return type;
    }

    public TextureState getTextureState() {
        return textureState;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isPawnFirstMove() {
        return pawnFirstMove;
    }

    public void setPawnFirstMove(boolean pawnFirstMove) {
        this.pawnFirstMove = pawnFirstMove;
    }
}
