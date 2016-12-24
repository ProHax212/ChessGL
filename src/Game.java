/**
 * Created by Ryan_Comer on 12/15/2016.
 */

import graphics.Texture;
import graphics.TextureState;
import models.ChessPiece;
import models.ChessSlot;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.io.IOException;
import java.util.Random;

import static models.ChessSlot.slotDistance;
import static models.ChessSlot.xOffset;
import static models.ChessSlot.yOffset;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

    public static int WIDTH=800, HEIGHT=800;
    public static float scalingFactor = 800f/1200f;
    public long window;
    private GLCapabilities capabilities;

    private State state = State.NORMAL;

    private ChessGame chessGame;

    /*
    Callback function for keys
     */
    public void key_callback(long window, int key, int scancode, int action, int mod){
        if(key==GLFW_KEY_F && action==GLFW_RELEASE){
            glfwSetWindowShouldClose(window, true);
        }else if(key==GLFW_KEY_A && action==GLFW_RELEASE){
            Random r = new Random();
            String[] movies = {"Tarzan", "Independence Day", "Mechanic"};
            System.out.println(movies[r.nextInt(3)]);
        }
    }

    public void mouse_button_callback(long window, int button, int action, int mods) {
        if(action != GLFW_MOUSE_BUTTON_1) return;

        double[] x = new double[1], y = new double[1];
        glfwGetCursorPos(window, x, y);

        ChessSlot slot = chessGame.mouseClick((int)x[0], HEIGHT - (int)y[0]);
        if(slot != null) slotClicked(slot);
    }

    private void slotClicked(ChessSlot slot){
        if(state == State.NORMAL){
            if(chessGame.highlightMoves(slot)) {
                chessGame.setSelectedSlot(slot);
                state = State.PIECE_CLICKED;
            }
        }else{
            if(chessGame.movePiece(slot)) chessGame.swapTurns();
            chessGame.setSelectedSlot(null);
            chessGame.clearHighlight();
            state = State.NORMAL;
        }
    }

    private void windowInit(){
        // Set up window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);  // OPENGL version 3.2
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // Use core functionality
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);  // Deactivate deprecated functionality

        window = glfwCreateWindow(WIDTH, HEIGHT, "Fire Emblem", NULL, NULL);
        if(window == NULL){
            System.err.println("Error creating a window");
            System.exit(1);
        }

        // Center the screen
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, ((mode.width()-WIDTH)/2), (mode.height()-HEIGHT)/2);

        glfwSetKeyCallback(window, this::key_callback);
        glfwSetMouseButtonCallback(window, this::mouse_button_callback);

        glfwMakeContextCurrent(window);

        capabilities = GL.createCapabilities();
    }

    public void start() throws IOException{
        if(!glfwInit()){
            System.err.println("Error initializing GLFW");
            System.exit(1);
        }

        windowInit();
        TextureState.init();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        chessGame = new ChessGame();

        // Game loop
        int targetFPS = 60;
        double targetTime = 1/targetFPS;  // Milliseconds
        while(!glfwWindowShouldClose(window)){
            double startTime = glfwGetTime();
            glClear(GL_COLOR_BUFFER_BIT);

            chessGame.render();

            glfwSwapBuffers(window);
            glfwPollEvents();   // Process waiting events

            double endTime = glfwGetTime();
            if((startTime + targetTime - endTime) > 0){
                try {
                    Thread.sleep((long)((startTime + targetTime - endTime) * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        glfwTerminate();
    }

    public static void main(String[] args) {
        try {
            new Game().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static enum State{
        NORMAL,
        PIECE_CLICKED
    }

}
