package graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

public class Texture{

    private final int id;

    private int texWidth, texHeight;
    private float x, y;

    private ByteBuffer pixels;

    public Texture(String path, float x, float y){
        this.x = x;
        this.y = y;
        id = glGenTextures();

        STBImage.stbi_set_flip_vertically_on_load(true);
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        pixels = STBImage.stbi_load(path, w, h, comp, 4);

        texWidth = w.get();
        texHeight = h.get();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texWidth, texHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ByteBuffer getPixels() {
        return pixels;
    }

    public int getTexWidth() {
        return texWidth;
    }

    public int getTexHeight() {
        return texHeight;
    }

    public void setTexWidth(int texWidth) {
        this.texWidth = texWidth;
    }

    public void setTexHeight(int texHeight) {
        this.texHeight = texHeight;
    }
}