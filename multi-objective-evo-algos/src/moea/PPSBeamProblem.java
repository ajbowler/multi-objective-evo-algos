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
  // dummy values, not actually used
  private double d1 = 0;
  private double d2 = 0;
  private double d3 = 0;
  private double b = 0;
  private double L = 0;

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
   * 5 variables, 2 objectives (the fundamental frequency and the cost), 7
   * constraints g(x) for the variables.
   */
  public PPSBeamProblem()
  {
    super(5, 2, 7);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.moeaframework.core.Problem#evaluate(org.moeaframework.core.Solution)
   */
  @Override
  public void evaluate(Solution solution)
  {
    double[] x = EncodingUtils.getReal(solution);
    double f1 = 0.0;
    double f2 = 0.0;

    double EI = getEI();
    double mu = getMu();
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
    // TODO: actually handle constraints

    Solution solution = new Solution(numberOfVariables, numberOfObjectives);

    // b
    solution.setVariable(0, new RealVariable(0.3, 0.55));

    // L
    solution.setVariable(1, new RealVariable(3, 6));

    // d3
    solution.setVariable(2, new RealVariable(0.3, 0.6));

    // d1
    solution.setVariable(3, new RealVariable(0.01, 0.58));

    // d2
    solution.setVariable(4, new RealVariable(0.01 + d1, 0.58 + d1));

    return solution;
  }

  private double getEI()
  {
    double result = (2 * b) / 3;
    double first = E1 * Math.pow(d1, 3);
    double second = E2 * (Math.pow(d2, 3) - Math.pow(d1, 3));
    double third = E3 * (Math.pow(d3, 3) - Math.pow(d2, 3));
    double secondHalf = first + second + third;
    result *= secondHalf;

    return result;
  }

  private double getMu()
  {
    double result = 2 * b;
    double first = p1 * d1;
    double second = p2 * (d2 - d1);
    double third = p3 * (d3 - d2);
    double secondHalf = first + second + third;
    result *= secondHalf;

    return result;
  }

  public double getD1()
  {
    return d1;
  }

  public double getD2()
  {
    return d2;
  }

  public double getD3()
  {
    return d3;
  }

  public double getB()
  {
    return b;
  }

  public double getL()
  {
    return L;
  }
}