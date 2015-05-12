package phero.calculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Jason on 12/05/2015.
 */
public class Plot3DFragment extends Fragment implements View.OnClickListener {

  private View rootView = null;
  private MyGLSurfaceView glView;

  public Plot3DFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_3dplot, container, false);

    glView = new MyGLSurfaceView(this.getActivity());
    ((LinearLayout)rootView.findViewById(R.id.root_3dplot)).addView(glView);
    return rootView;
  }

  @Override
  public void onClick(View v) {

  }
}
