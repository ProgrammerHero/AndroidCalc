package phero.calculator;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by Jason on 12/05/2015.
 */
public class MyGLSurfaceView extends GLSurfaceView {
  private MyGLRenderer renderer;

  public MyGLSurfaceView(Context context) {
    super(context);

    // Create an OpenGL ES 2.0 context.
    setEGLContextClientVersion(2);

    // Set the Renderer for drawing on the GLSurfaceView
    renderer = new MyGLRenderer();
    setRenderer(renderer);

    // Render the view only when there is a change in the drawing data
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }

  private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
  private float mPreviousX;
  private float mPreviousY;

  @Override
  public boolean onTouchEvent(@NonNull MotionEvent e) {
    // MotionEvent reports input details from the touch screen
    // and other input controls. In this case, you are only
    // interested in events where the touch position changed.

    float x = e.getX();
    float y = e.getY();

    switch (e.getAction()) {
      case MotionEvent.ACTION_MOVE:

        float dx = x - mPreviousX;
        float dy = y - mPreviousY;

        //renderer.setAngle(renderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
        requestRender();
    }

    mPreviousX = x;
    mPreviousY = y;

    return true;
  }
}
