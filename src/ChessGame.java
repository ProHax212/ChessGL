import graphics.Texture;
import graphics.TextureState;
import models.ChessPiece;
import models.ChessPiece.Type;
import models.ChessSlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static models.ChessPiece.Type.*;

/**
 * Created by Ryan_Comer on 12/22/2016.
 */
public class ChessGame {

    private ChessSlot[][] board = new ChessSlot[8][8];
    private int playerTurn = 0;
    private TextureState background;

    public ChessGame(){
        initSlots();
        initPieces();
        initBackground();
    }

    public void render(){
        background.render();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j] != null) board[i][j].render();
            }
        }
    }

    private void initSlots(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new ChessSlot(null, j, i);
            }
        }
    }

    private void initPieces(){
        int width = Game.WIDTH/11;
        int height = Game.HEIGHT/10;

        // PAWNS
        for(int i = 0; i < 8; i++){
            board[1][i].setPiece(new ChessPiece(0, PAWN, i, 1, width, height));
            board[6][i].setPiece(new ChessPiece(1, PAWN, i, 6, width, height));
        }

        // ROOKS
        board[0][0].setPiece(new ChessPiece(0, ROOK, 0, 0, width, height));
        board[0][7].setPiece(new ChessPiece(0, ROOK, 7, 0, width, height));
        board[7][0].setPiece(new ChessPiece(1, ROOK, 0, 7, width, height));
        board[7][7].setPiece(new ChessPiece(1, ROOK, 7, 7, width, height));

        // BISHOPS
        board[0][2].setPiece(new ChessPiece(0, BISHOP, 2, 0, width, height));
        board[0][5].setPiece(new ChessPiece(0, BISHOP, 5, 0, width, height));
        board[7][2].setPiece(new ChessPiece(1, BISHOP, 2, 7, width, height));
        board[7][5].setPiece(new ChessPiece(1, BISHOP, 5, 7, width, height));

        // KNIGHTS
        board[0][1].setPiece(new ChessPiece(0, KNIGHT, 1, 0, width, height));
        board[0][6].setPiece(new ChessPiece(0, KNIGHT, 6, 0, width, height));
        board[7][1].setPiece(new ChessPiece(1, KNIGHT, 1, 7, width, height));
        board[7][6].setPiece(new ChessPiece(1, KNIGHT, 6, 7, width, height));

        // QUEENS
        board[0][4].setPiece(new ChessPiece(0, QUEEN, 4, 0, width, height));
        board[7][4].setPiece(new ChessPiece(1, QUEEN, 4, 7, width, height));

        // KINGS
        board[0][3].setPiece(new ChessPiece(0, KING, 3, 0, width, height));
        board[7][3].setPiece(new ChessPiece(1, KING, 3, 7, width, height));
    }

    public void initBackground(){
        try {
            background = new TextureState(new Texture("./res/chessboard.png", -1f, -1f));
            Texture tex = background.getTexture();
            tex.setTexWidth(Game.WIDTH);
            tex.setTexHeight(Game.HEIGHT);
            background.updateVbo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChessSlot mouseClick(int x, int y){
        float xPos = (float)x / (float)Game.WIDTH;
        float yPos = (float)y / (float)Game.HEIGHT;

        xPos = 2f*xPos - 1f;
        yPos = 2f*yPos - 1f;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPiece comp = board[i][j].getPiece();
                if(comp == null) continue;

                Texture tex = comp.getTextureState().getTexture();
                float x1 = tex.getX();
                float y1 = tex.getY();
                float x2 = x1+((float)tex.getTexWidth() / (float)Game.WIDTH)*2;
                float y2 = y1+((float)tex.getTexHeight() / (float) Game.HEIGHT)*2;

                if(xPos > x1 && xPos < x2 && yPos > y1 && yPos < y2) return board[i][j];
            }
        }

        return null;
    }

    /*
    Highlight the possible moves for the selected piece
     */
    public void highlightMoves(ChessSlot slot){
        if(slot.getPiece().getPlayer() != playerTurn) return;

        List<Integer> xVals = new ArrayList<>(), yVals = new ArrayList<>();
        int xStart = slot.getPiece().getX(), yStart = slot.getPiece().getY();

        switch (slot.getPiece().getType()){
            case PAWN:
                if(playerTurn == 0){
                    if((yStart + 1) <= 7){
                        if((xStart - 1) >= 0){
                            xVals.add(xStart-1); yVals.add(yStart+1);
                        }
                        if((xStart + 1) <= 7){
                            xVals.add(xStart+1); yVals.add(yStart+1);
                        }
                    }
                }else{
                    if((yStart - 1) >= 0){
                        if((xStart - 1) >= 0){
                            xVals.add(xStart-1); yVals.add(yStart-1);
                        }
                        if((xStart + 1) <= 7){
                            xVals.add(xStart+1); yVals.add(yStart-1);
                        }
                    }
                }
                break;
            case ROOK:
                for(int i = 0; i < 8; i++){
                    if(i != xStart){
                        xVals.add(i);
                        yVals.add(yStart);
                    }
                    if(i != yStart){
                        xVals.add(xStart);
                        yVals.add(i);
                    }
                }
                break;
            case KNIGHT:
                int x = xStart-2; int y = yStart-1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                x = xStart-2; y = yStart+1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                x = xStart+2; y = yStart-1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                x = xStart+2; y = yStart+1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                y = yStart-2; x = xStart-1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                y = yStart-2; x = xStart+1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                y = yStart+2; x = xStart-1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }

                y = yStart+2; x = xStart+1;
                if(x >= 0 && x < 8 && y >= 0 && y < 8){
                    xVals.add(x); yVals.add(y);
                }
                break;
            case BISHOP:
                for(int i=1; i<8; i++){
                    if((xStart-i >= 0) && yStart-i >= 0) {
                        xVals.add(xStart - i);
                        yVals.add(yStart - i);
                    }
                    if(xStart+i < 8 && yStart-i >= 0){
                        xVals.add(xStart+i);
                        yVals.add(yStart-i);
                    }
                    if(xStart-i >= 0 && yStart+i < 8){
                        xVals.add(xStart-i);
                        yVals.add(yStart+i);
                    }
                    if(xStart+i < 8 && yStart+i < 8){
                        xVals.add(xStart+i);
                        yVals.add(yStart+i);
                    }
                }
                break;
            case QUEEN:
                for(int i = 0; i < 8; i++){
                    if(i != xStart){
                        xVals.add(i);
                        yVals.add(yStart);
                    }
                    if(i != yStart){
                        xVals.add(xStart);
                        yVals.add(i);
                    }
                }

                for(int i=1; i<8; i++){
                    if((xStart-i >= 0) && yStart-i >= 0) {
                        xVals.add(xStart - i);
                        yVals.add(yStart - i);
                    }
                    if(xStart+i < 8 && yStart-i >= 0){
                        xVals.add(xStart+i);
                        yVals.add(yStart-i);
                    }
                    if(xStart-i >= 0 && yStart+i < 8){
                        xVals.add(xStart-i);
                        yVals.add(yStart+i);
                    }
                    if(xStart+i < 8 && yStart+i < 8){
                        xVals.add(xStart+i);
                        yVals.add(yStart+i);
                    }
                }
                break;
            case KING:
                if(xStart-1 >= 0){
                    xVals.add(xStart-1); yVals.add(yStart);
                    if(yStart-1 >= 0){
                        xVals.add(xStart-1); yVals.add(yStart-1);
                    }
                    if(yStart+1 < 8){
                        xVals.add(xStart-1); yVals.add(yStart+1);
                    }
                }
                if(xStart+1 < 8){
                    xVals.add(xStart+1); yVals.add(yStart);
                    if(yStart-1 >= 0){
                        xVals.add(xStart+1); yVals.add(yStart-1);
                    }
                    if(yStart+1 < 8){
                        xVals.add(xStart+1); yVals.add(yStart+1);
                    }
                }
                if(yStart-1 >= 0){
                    xVals.add(xStart); yVals.add(yStart-1);
                }
                if(yStart+1 < 8){
                    xVals.add(xStart); yVals.add(yStart+1);
                }
                break;
        }

        for(int i = 0; i < xVals.size(); i++){
            board[yVals.get(i)][xVals.get(i)].setHighlighted(true);
        }
    }

    /*
    Clear all of the highlighted flags
     */
    public void clearHighlight(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j].setHighlighted(false);
            }
        }
    }

    public String toString(){
        String returnString = "";

        for(int j = 0; j < 8; j++){
            for(int i = 0; i < 8; i++){
                if(board[j][i] != null) returnString += board[j][i].getPiece().getType() + "\t";
            }
            returnString += "\n";
        }

        return returnString;
    }



}
