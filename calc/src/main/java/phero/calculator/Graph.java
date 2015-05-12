package phero.calculator;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A wrapper for a graph of a data set.
 * Created by Jason on 12/05/2015.
 */
public class Graph {

  private final String vertexShaderCode =
    // This matrix member variable provides a hook to manipulate
    // the coordinates of the objects that use this vertex shader
    "uniform mat4 uMVPMatrix;" +
    "attribute vec4 vPosition;" +
    "void main() {" +
    // the matrix must be included as a modifier of gl_Position
    // Note that the uMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    "  gl_Position = uMVPMatrix * vPosition;" +
    "}";

  private final String fragmentShaderCode =
    "precision mediump float;" +
    "uniform vec4 vColor;" +
    "void main() {" +
    "  gl_FragColor = vColor;" +
    "}";

  public static final int COORDS_PER_VERTEX = 3; // x,y,z
  private final int mProgram;

  private FloatBuffer vertexBuffer;
  private int vertexCount;
  private int mMVPMatrixHandle, mColorHandle, mPositionHandle;

  /**
   * Sets up the drawing object data for use in an OpenGL ES context.
   */
  public Graph() {
    // prepare shaders and OpenGL program
    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

    mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
    GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
    GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
    GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    // get handle to shape's transformation matrix
    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    // get handle to fragment shader's vColor member
    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    // get handle to vertex shader's vPosition member
    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

    // load some dummy data for now so the viiew doesn't crash when trying to render
    loadData(new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f} );
  }

  public void loadData(float[] points) {
    // initialize vertex byte buffer for shape coordinates
    ByteBuffer bb = ByteBuffer.allocateDirect(points.length * 4); // 4 bytes per float
    // use the device hardware's native byte order
    bb.order(ByteOrder.nativeOrder());

    vertexCount = points.length / 3; // x,y,z per vertex
    // create a floating point buffer from the ByteBuffer
    vertexBuffer = bb.asFloatBuffer();
    // add the coordinates to the FloatBuffer
    vertexBuffer.put(points);
    // set the buffer to read the first coordinate
    vertexBuffer.position(0);
  }

  private int loadShader(int type, String shaderCode){
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, shaderCode);
    GLES20.glCompileShader(shader);

    return shader;
  }

  /**
   * Encapsulates the OpenGL ES instructions for drawing this shape.
   *
   * @param mvpMatrix - The Model View Project matrix in which to draw
   * this shape.
   */
  public void draw(float[] mvpMatrix) {
    // Add program to OpenGL environment
    GLES20.glUseProgram(mProgram);

    // Enable a handle to the triangle vertices
    GLES20.glEnableVertexAttribArray(mPositionHandle);

    // Prepare the triangle coordinate data
    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

    // Set color for drawing the triangle
    GLES20.glUniform4fv(mColorHandle, 1, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f }, 0);

    // Apply the projection and view transformation
    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
//    MyGLRenderer.checkGlError("glUniformMatrix4fv");

    // Draw the triangle
    GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertexCount);

    // Disable vertex array
    GLES20.glDisableVertexAttribArray(mPositionHandle);
  }

}
