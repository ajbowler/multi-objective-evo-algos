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
   * constraints for the mass, and width differences of the materials.
   */
  public PPSBeamProblem()
  {
    super(5, 2, 3);
  }

  @Override
  public void evaluate(Solution solution)
  {
    double[] x = EncodingUtils.getReal(solution);
    double f1 = 0.0;
    double f2 = 0.0;

    double b = x[0];
    double L = x[1];
    double d1 = x[2];
    double d2 = x[3];
    double d3 = x[4];

    double EI = getEI(x);
    double mu = getMu(x);
    double firstHalf = (Math.PI / (2 * L * L));
    double secondHalf = (Math.pow((EI / mu), 0.5));
    f1 = firstHalf * secondHalf;

    firstHalf = 2 * b * L;
    secondHalf = ((c1 * d1) + (c2 * (d2 - d1)) + (c3 * (d3 - d2)));
    f2 = firstHalf * secondHalf;

    solution.setObjective(0, f1);
    solution.setObjective(1, f2);
  }

  @Override
  public Solution newSolution()
  {
    Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);

    // b
    solution.setVariable(0, new RealVariable(0.3, 0.55));

    // L
    solution.setVariable(1, new RealVariable(3, 6));

    // d1
    solution.setVariable(2, new RealVariable(0.01, 0.58));

    // d2
    solution.setVariable(3, new RealVariable(0.01, 1.16));

    // d3
    solution.setVariable(4, new RealVariable(0.3, 0.6));

    return solution;
  }

  private double getEI(double[] x)
  {
    double b = x[0];
    double d1 = x[2];
    double d2 = x[3];
    double d3 = x[4];

    double result = (2 * b) / 3;
    double first = E1 * Math.pow(d1, 3);
    double second = E2 * (Math.pow(d2, 3) - Math.pow(d1, b));
    double third = E3 * (Math.pow(d3, 3) - Math.pow(d2, 3));
    result *= (first + second + third);

    return result;
  }

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
    result *= (first + second + third);

    return result;
  }
}