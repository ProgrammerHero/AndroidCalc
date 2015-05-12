package phero.calculator.calctree;

/**
 * CalcTree main parser.
 * Created by Jason on 11/05/2015.
 */

public class CalcTree {

  private String equation = "";
  private TreeNode root = null;

  public enum BUILD_ERROR {
    BERR_NONE,
    BERR_NO_EQUATION,
    BERR_UNBALANCED_BRACKETS,
    BERR_SYNTAX_ERR,
  }

  // the current (if any) error state of this parser
  private BUILD_ERROR errState;

  // A class representing the location and size of an operator in the equation
  private class Coord {
    public int index, length;
    public Coord(int index, int length) {
      this.index = index;
      this.length = length;
    }
  }

  public CalcTree(String equation) {
    this.equation = equation;
    errState = BUILD_ERROR.BERR_NONE;
  }

  private Coord findFirstOperator(String subEqn) {
    int index = -1;
    int length = 0;
    int priority = Integer.MAX_VALUE;
    int bracketLevel = 0;
    for (int i = 0; i<subEqn.length(); i++) {
      if (subEqn.charAt(i) == '+') {
        int currLevel = bracketLevel + 1;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 1;
        }
      }
      else if (subEqn.charAt(i) == '-') {
        int currLevel = bracketLevel + 2;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 1;
        }
      }
      else if (subEqn.charAt(i) == '*') {
        int currLevel = bracketLevel + 3;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 1;
        }
      }
      else if (subEqn.charAt(i) == '/') {
        int currLevel = bracketLevel + 4;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 1;
        }
      }
      else if (subEqn.charAt(i) == '^') {
        int currLevel = bracketLevel + 5;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 1;
        }
      }
      // everything else is unary and should be last
      // these will match sin/cos/tan so we have to check them first to prevent problems
      else if (subEqn.indexOf("asin", i) == i
        || subEqn.indexOf("acos", i) == i
        || subEqn.indexOf("atan", i) == i
        || subEqn.indexOf("sinh", i) == i
        || subEqn.indexOf("cosh", i) == i
        || subEqn.indexOf("tanh", i) == i) {
        int currLevel = bracketLevel + 6;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 4;
        }
      }
      else if (subEqn.indexOf("cos", i) == i
        || subEqn.indexOf("sin", i) == i
        || subEqn.indexOf("tan", i) == i
        || subEqn.indexOf("log", i) == i
        || subEqn.indexOf("exp", i) == i) {
        int currLevel = bracketLevel + 6;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 3;
        }
      }
      else if (subEqn.indexOf("ln", i) == i) {
        int currLevel = bracketLevel + 6;
        if (currLevel < priority) {
          priority = currLevel;
          index = i;
          length = 2;
        }
      }
      else if (subEqn.charAt(i) == '(') {
        bracketLevel += 10;
      }
      else if (subEqn.charAt(i) == ')') {
        bracketLevel -= 10;
        // warning - if bracketLevel < 0, then there's a syntax error
      }
    }

    return new Coord(index, length);
  }

  public BUILD_ERROR build() {
    // === first we should do some syntax checking ===
    // missing equation
    if (equation.equals("")) {
      errState = BUILD_ERROR.BERR_NO_EQUATION;
      return errState;
    }
    // unbalanced brackets
    int bracket = 0;
    for (int i = 0; i<equation.length(); i++) {
      if (equation.charAt(i) == '(') bracket++;
      if (equation.charAt(i) == ')') bracket--;
      if (bracket < 0) {
        errState = BUILD_ERROR.BERR_UNBALANCED_BRACKETS;
        return errState;
      }
    }
    if (bracket != 0) {
      errState = BUILD_ERROR.BERR_UNBALANCED_BRACKETS;
      return errState;
    }
    root = buildNode(equation);
    return BUILD_ERROR.BERR_NONE;
  }

  private TreeNode buildNode(String subEquation) {
    Coord p = findFirstOperator(subEquation);
    if (p.index == -1) {
      // leaf node
      for (int i = 0; i < subEquation.length(); i++) {
        if (subEquation.charAt(i) == '(' || subEquation.charAt(i) == ')') {
          char eqn[] = subEquation.toCharArray();
          eqn[i] = ' ';
          subEquation = String.valueOf(eqn);
        }
      }
      return new TreeNode(subEquation);
    }
    TreeNode node = new TreeNode(subEquation.substring(p.index, p.index+p.length), null);
    String lEq = subEquation.substring(0, p.index);
    String rEq = subEquation.substring(p.index + p.length);

    if (lEq.length() > 0) {
      node.setLeft(buildNode(lEq));
      node.getLeft().setParent(node);
    } else {
      node.setLeft(null);
    }

    if (rEq.length() > 0) {
      node.setRight(buildNode(rEq));
      node.getRight().setParent(node);
    } else {
      node.setRight(null);
    }

    return node;
  }

  public double solve() {
    return solveNode(root);
  }

  private double solveNode(TreeNode node) {
    if (node.getValue().equals("+")) {
      return solveNode(node.getLeft()) + solveNode(node.getRight());
    }
    else if (node.getValue().equals("-")) {
      return solveNode(node.getLeft()) - solveNode(node.getRight());
    }
    else if (node.getValue().equals("/")) {
      return solveNode(node.getLeft()) / solveNode(node.getRight());
    }
    else if (node.getValue().equals("*")) {
      return solveNode(node.getLeft()) * solveNode(node.getRight());
    }
    else if (node.getValue().equals("sin")) {
      return Math.sin(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("cos")) {
      return Math.cos(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("tan")) {
      return Math.tan(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("asin")) {
      return Math.asin(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("acos")) {
      return Math.acos(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("atan")) {
      return Math.atan(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("log")) {
      return Math.log(solveNode(node.getRight())) / Math.log(10.0);
    }
    else if (node.getValue().equals("ln")) {
      return Math.log(solveNode(node.getRight()));
    }
    else if (node.getValue().equals("exp")) {
      return Math.exp(solveNode(node.getRight()));
    }
    return Double.valueOf(node.getValue());
  }

  /*
  // this function uses [out] parameters which java doesn't support
  public static boolean evaluate(String equation, double result) {
    CalcTree c = new CalcTree(equation);
    BUILD_ERROR err = c.build();
    if (err != BUILD_ERROR.BERR_NONE) {
      return false;
    }
    double ret = c.solve();
    return true;
  }
  */
}