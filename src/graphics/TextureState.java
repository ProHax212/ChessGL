package graphics;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Ryan_Comer on 12/21/2016.
 */
public class TextureState {

    private int vao, vbo, ebo;
    private Texture texture;
    private static int vertexShader, fragmentShader;
    private static int shaderProgram;
    private static boolean initialized = false;

    public static void init() throws IOException{
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        String vertexSource = new String(Files.readAllBytes(Paths.get("./res/vertexShader")));
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) != GL_TRUE){
            System.out.println("Vertex compile failed");
            System.out.println(glGetShaderInfoLog(vertexShader));
            System.exit(1);
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        String fragmentSource = new String(Files.readAllBytes(Paths.get("./res/fragmentShader")));
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != GL_TRUE){
            System.out.println("Fragment compile failed");
            System.out.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glBindFragDataLocation(shaderProgram, 0, "fragColor");
        glLinkProgram(shaderProgram);

        if(glGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE){
            System.out.println("Program link failed");
            System.exit(1);
        }

        glUseProgram(shaderProgram);

        initialized = true;
    }

    public TextureState(Texture texture) throws IOException{
        if(!initialized){
            System.err.println("Texture State not initialized.  Call TextureState.init();");
            System.exit(1);
        }

        this.texture = texture;

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        updateVbo();

        IntBuffer elements = BufferUtils.createIntBuffer(2*3);
        elements.put(0).put(1).put(2);
        elements.put(2).put(3).put(0);
        elements.flip();
        ebo = glGenBuffers();
        bindEbo(ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

        specifyVertexAttributes();

        int uniTex = glGetUniformLocation(shaderProgram, "tex");
        glUniform1i(uniTex, 0);
    }

    public void render(){
        bindVao(vao);
        texture.bind();
        useProgram(shaderProgram);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    private void specifyVertexAttributes(){
        int posAttrib = glGetAttribLocation(shaderProgram, "position");
        glVertexAttribPointer(posAttrib, 2, GL_FLOAT, false, 7*Float.BYTES, 0);
        glEnableVertexAttribArray(posAttrib);

        int colAttrib = glGetAttribLocation(shaderProgram, "color");
        glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 7*Float.BYTES, 2*Float.BYTES);
        glEnableVertexAttribArray(colAttrib);

        int texAttrib = glGetAttribLocation(shaderProgram, "texcoords");
        glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, 7*Float.BYTES, 5*Float.BYTES);
        glEnableVertexAttribArray(texAttrib);
    }

    public void updateVbo(){
        long window = glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get(), height = heightBuffer.get();

        float w = ((float)texture.getTexWidth()/(float)width) * 2;
        float h = ((float)texture.getTexHeight()/(float)height) * 2;
        float x1=texture.getX(), y1=texture.getY(), x2=x1+w, y2=y1+h;

        FloatBuffer vertices = BufferUtils.createFloatBuffer(4*7);
        vertices.put(x1).put(y1).put(1f).put(1f).put(1f).put(0f).put(0f);
        vertices.put(x1).put(y2).put(1f).put(1f).put(1f).put(0f).put(1f);
        vertices.put(x2).put(y2).put(1f).put(1f).put(1f).put(1f).put(1f);
        vertices.put(x2).put(y1).put(1f).put(1f).put(1f).put(1f).put(0f);
        vertices.flip();

        bindVao(vao);
        bindVbo(vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    }

    private void bindVao(int vao){
        glBindVertexArray(vao);
    }

    private void bindVbo(int vbo){
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
    }

    private void bindEbo(int ebo){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
    }

    private void useProgram(int shaderProgram){
        glUseProgram(shaderProgram);
    }

    public Texture getTexture() {
        return texture;
    }
}
