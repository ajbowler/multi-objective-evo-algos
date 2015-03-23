package moea;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

/**
 * @author Andrew Bowler
 *
 */
public class PPSBeamProblem extends AbstractProblem
{
  private double E1 = 1.60E9;
  private double E2 = 7.00E10;
  private double E3 = 2.00E11;

  private double c1 = 500.0;
  private double c2 = 1500.0;
  private double c3 = 800.0;

  private double p1 = 100.0;
  private double p2 = 2770.0;
  private double p3 = 7780.0;

  /**
   * 5 variables, 2 objectives (the fundamental frequency and the cost), 3
   * constraints for the mass and width differences of the materials.
   */
  public PPSBeamProblem()
  {
    super(5, 2, 3);
  }

  /**
   * Evaluates the generated solution for this problem and calculates and sets
   * the constraint violations defined in the function, if any are violated in
   * the first place.
   */
  @Override
  public void evaluate(Solution solution)
  {
    double[] x = EncodingUtils.getReal(solution);
    double[] f = new double[2];
    double g3 = 0.0;
    double g6 = 0.0;
    double g7 = 0.0;

    double b = x[0];
    double L = x[1];
    double d1 = x[2];
    double d2 = x[3];
    double d3 = x[4];

    double EI = getEI(x);
    double mu = getMu(x);
    double firstHalf = (Math.PI / (2 * L * L));
    double secondHalf = (Math.pow((EI / mu), 0.5));
    f[0] = firstHalf * secondHalf;

    firstHalf = 2 * b * L;
    secondHalf = ((c1 * d1) + (c2 * (d2 - d1)) + (c3 * (d3 - d2)));
    f[1] = firstHalf * secondHalf;

    // mass constraint
    if (L * mu >= 2000 && L * mu <= 2800)
      g3 = 0.0;
    else
    {
      if (L * mu > 2800)
        g3 = (L * mu) - 2800;
      else
        g3 = (L * mu) - 2000;
    }

    // material constraint
    if (d2 - d1 >= 0.01 && d2 - d1 <= 0.58)
      g6 = 0.0;
    else
    {
      if (d2 - d1 > 0.58)
        g6 = (d2 - d1) - 0.58;
      else
        g6 = (d2 - d1) - 0.01;
    }

    // material constraint
    if (d3 - d2 >= 0.01 && d3 - d2 <= 0.57)
      g7 = 0.0;
    else
    {
      if (d3 - d2 > 0.57)
        g7 = (d3 - d2) - 0.57;
      else
        g7 = (d3 - d2) - 0.01;
    }

    solution.setObjectives(f);
    solution.setConstraint(0, g3);
    solution.setConstraint(1, g6);
    solution.setConstraint(2, g7);
  }

  /**
   * Creates the Solution object for this problem.
   */
  @Override
  public Solution newSolution()
  {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);

    solution.setVariable(0, new RealVariable(0.3, 0.55)); // b
    solution.setVariable(1, new RealVariable(3, 6)); // L
    solution.setVariable(2, new RealVariable(0.01, 0.58)); // d1
    solution.setVariable(3, new RealVariable(0.01, 1.16)); // d2
    solution.setVariable(4, new RealVariable(0.3, 0.6)); // d3

    return solution;
  }

  /**
   * Returns the EI coefficient calculated from the provided set of variables
   * for this problem.
   */
  private double getEI(double[] x)
  {
    double b = x[0];
    double d1 = x[2];
    double d2 = x[3];
    double d3 = x[4];

    double fraction = (2 * b) / 3;

    double d1_3 = Math.pow(d1, 3);
    double d2_3 = Math.pow(d2, 3);
    double d3_3 = Math.pow(d3, 3);

    double first = E1 * d1_3;
    double second = (d2_3 - d1_3) * E2;
    double third = (d3_3 - d2_3) * E3;

    double sum = first + second + third;

    double result = fraction * sum;

    return result;
  }

  /**
   * Returns the mu coefficient calculated from the provided set of variables
   * for this problem.
   */
  private double getMu(double[] x)
  {
    double b = x[0];
    double d1 = x[2];
    double d2 = x[3];
    double d3 = x[4];

    double result = 2 * b;
    double first = p1 * d1;
    double second = p2 * (d2 - d1);
    double third = p3 * (d3 - d2);
    double sum = first + second + third;
    result = result * sum;

    return result;
  }
}