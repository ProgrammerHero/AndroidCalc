package phero.calculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import phero.calculator.calctree.CalcTree;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicCalcFragment extends Fragment implements View.OnClickListener {

  private View rootView = null;
  private TextView screen = null;
  private String equation = "";
  private String prevAnswer = "";

  // this is a good candidate for an integer state machine
  private boolean shift = false;
  private boolean alpha = false;
  private boolean error = false;

  public BasicCalcFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_calc, container, false);

    screen = (TextView)rootView.findViewById(R.id.txtScreen);
    prevAnswer = equation = screen.getText().toString();
    for (int b : buttons) {
      Button but = (Button)rootView.findViewById(b);
      if (but == null) {
        Log.w(CalcActivity.TAG, "invalid button: " + b);
      }
      else {
        but.setOnClickListener(this);
      }
    }

    return rootView;
  }

  private void setError(CalcTree.BUILD_ERROR err) {
    if (err == CalcTree.BUILD_ERROR.BERR_NONE) {
      equation = getString(R.string.empty_equation);
      screen.setText(equation);
      error = false;
    }
    else {
      equation = "";
      screen.setText("ERROR");
      error = true;
    }
  }

  private void setAlpha(boolean a) {
    alpha = a;
    // toggle the button texts where necessary
    if (alpha) {
      ((Button)rootView.findViewById(R.id.btnNegative)).setText("A");
      ((Button)rootView.findViewById(R.id.btnDegrees)).setText("B");
      ((Button)rootView.findViewById(R.id.btnHyperbolic)).setText("C");
      ((Button)rootView.findViewById(R.id.btnSin)).setText("D");
      ((Button)rootView.findViewById(R.id.btnCos)).setText("E");
      ((Button)rootView.findViewById(R.id.btnTan)).setText("F");
    }
    else {
      ((Button)rootView.findViewById(R.id.btnNegative)).setText("-");
      ((Button)rootView.findViewById(R.id.btnDegrees)).setText("DMS");
      ((Button)rootView.findViewById(R.id.btnHyperbolic)).setText("hyp");
      ((Button)rootView.findViewById(R.id.btnSin)).setText("sin");
      ((Button)rootView.findViewById(R.id.btnCos)).setText("cos");
      ((Button)rootView.findViewById(R.id.btnTan)).setText("tan");
    }
  }

  private void setShift(boolean s) {
    shift = s;
    if (shift) {
      // toggle the button texts where necessary
    }
    else {
    }
  }

  private boolean canPlacedecimal() {
    // search from the back of the string
    String numbers = "1234567890";
    int i = equation.length()-1;
    while (i>=0 && numbers.indexOf(equation.charAt(i--)) > -1);
    // found the last non-digit character in the string, so make sure its not a decimal point already
    // we also may have walked off the front end of the string
    return i<0 || equation.charAt(i) != '.';
  }

  private CalcTree.BUILD_ERROR evaluate() {
    // insert any macro values we may have added
    equation = equation.replace(getString(R.string.answer), prevAnswer);
    // stored memory values

    CalcTree tree = new CalcTree(equation);
    CalcTree.BUILD_ERROR err = tree.build();
    if (err == CalcTree.BUILD_ERROR.BERR_NONE) {
      prevAnswer = String.valueOf(tree.solve());
      equation = prevAnswer;
    }
    return err;
  }

  @Override
  public void onClick(View v) {

    if (error && v.getId() != R.id.btnClear) {
      return; // ignore this message
    }

    if (equation.equals(getString(R.string.empty_equation))) {
      equation = ""; // clear
    }
    switch (v.getId()) {
      case R.id.btn0:
      case R.id.btn1:
      case R.id.btn2:
      case R.id.btn3:
      case R.id.btn4:
      case R.id.btn5:
      case R.id.btn6:
      case R.id.btn7:
      case R.id.btn8:
      case R.id.btn9:
        // this is simple: just add the button tet
        equation += ((Button)v).getText().toString();
        break;
      case R.id.btnAlpha:
        setAlpha(!alpha);
        break;
      case R.id.btnAnswer:
        equation += getString(R.string.answer);
        break;
      case R.id.btnClear:
        equation = getString(R.string.empty_equation);
        // make sure to clear the error state too
        setError(CalcTree.BUILD_ERROR.BERR_NONE);
        break;
      case R.id.btnCloseParen:
        equation += ")";
        break;
      case R.id.btnCos:
        equation += "cos(";
        break;
      case R.id.btndecimal:
        if (canPlacedecimal()) {
          // e don't enforce any form of "0." here, a number starting with "." and no leading 0 is perfectly fine by this logic
          equation += '.';
        }
        break;
      case R.id.btnDel: // make this backspace for now
        if (equation.length() <= 1) {
          equation = getString(R.string.empty_equation);
        }
        else {
          equation = equation.substring(0, equation.length() - 1);
        }
        break;
      case R.id.btnDivide:
        equation += "/";
        break;

      // THE HEART: THE EVALUATE CASE
      case R.id.btnEquals:
        setError(evaluate());
        break;

      case R.id.btnExponent:
        equation += "^";
        break;
      case R.id.btnLn:
        equation += "ln(";
        break;
      case R.id.btnLog:
        equation += "log(";
        break;
      case R.id.btnMultiply:
        equation += "*";
        break;
      case R.id.btnNegative:
        equation += "-";
        break;
      case R.id.btnOpenParen:
        equation += "(";
        break;
      case R.id.btnPlus:
        equation += "+";
        break;
      case R.id.btnShift:
        setShift(!shift);
        break;
      case R.id.btnSubtract:
        equation += "-";
        break;
      case R.id.btnSin:
        equation += "sin(";
        break;
      case R.id.btnTan:
        equation += "tan(";
        break;
    }

    // make sure we don't ever display an empty string
    if (equation.equals("")) {
      equation = getString(R.string.empty_equation);
    }
    screen.setText(equation);
  }

  public final static int buttons[] = {
    R.id.btn0,
    R.id.btn1,
    R.id.btn2,
    R.id.btn3,
    R.id.btn4,
    R.id.btn5,
    R.id.btn6,
    R.id.btn7,
    R.id.btn8,
    R.id.btn9,
    R.id.btnAlpha,
    R.id.btnAnswer,
    R.id.btnCalc,
    R.id.btnCaret,
    R.id.btnClear,
    R.id.btnCloseParen,
    R.id.btnComma,
    R.id.btnConst,
    R.id.btnCos,
    R.id.btndecimal,
    R.id.btnDegrees,
    R.id.btnDel,
    R.id.btnDivide,
    R.id.btnEngineering,
    R.id.btnEquals,
    R.id.btnExponent,
    R.id.btnFrac,
    R.id.btnHyperbolic,
    R.id.btnIntegral,
    R.id.btnLn,
    R.id.btnLog,
    R.id.btnMemPlus,
    R.id.btnMode,
    R.id.btnMultiply,
    R.id.btnNegative,
    R.id.btnOn,
    R.id.btnOpenParen,
    R.id.btnPlus,
    R.id.btnRecall,
    R.id.btnReciprocal,
    R.id.btnShift,
    R.id.btnSin,
    R.id.btnSqrt,
    R.id.btnSquare,
    R.id.btnSubtract,
    R.id.btnTan,
  };
}
