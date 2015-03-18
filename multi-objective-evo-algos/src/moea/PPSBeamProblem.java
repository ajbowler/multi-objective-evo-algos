package moea;

import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;

/**
 * @author Andrew Bowler
 *
 */
public class PPSBeamProblem extends AbstractProblem
{

  /**
   * @param numberOfVariables
   * @param numberOfObjectives
   */
  public PPSBeamProblem(int numberOfVariables, int numberOfObjectives)
  {
    super(numberOfVariables, numberOfObjectives);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param numberOfVariables
   * @param numberOfObjectives
   * @param numberOfConstraints
   */
  public PPSBeamProblem(int numberOfVariables, int numberOfObjectives, int numberOfConstraints)
  {
    super(numberOfVariables, numberOfObjectives, numberOfConstraints);
    // TODO Auto-generated constructor stub
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
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.moeaframework.core.Problem#newSolution()
   */
  @Override
  public Solution newSolution()
  {
    // TODO Auto-generated method stub
    return null;
  }
}