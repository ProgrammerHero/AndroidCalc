package phero.calculator;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class CalcActivity extends AppCompatActivity {

  public final static String TAG = "CALC";
  private int currFragment = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calc);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, new BasicCalcFragment()).commit();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_calc, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    else if (id == R.id.action_basic) {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.container, new BasicCalcFragment());
      transaction.addToBackStack(null);
      transaction.commit();
      return true;
    }
    else if (id == R.id.action_plot2d) {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.container, new Plot2DFragment());
      transaction.addToBackStack(null);
      transaction.commit();
      return true;
    }
    else if (id == R.id.action_plot3d) {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.container, new Plot3DFragment());
      transaction.addToBackStack(null);
      transaction.commit();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
