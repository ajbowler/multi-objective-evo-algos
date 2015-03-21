package moea;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

/**
 * 
 * @author Andrew Bowler
 *
 */
public class PPSBeamSolver
{
  public static void main(String[] args) throws IOException
  {
    // configure and run this experiment
    NondominatedPopulation result = new Executor().withProperty("populationSize", 10000)
        .withProblem("moea.PPSBeamProblem").withAlgorithm("NSGAII").withMaxEvaluations(10000).distributeOnAllCores()
        .run();

    // display the results
    int COUNT = result.size();
    System.out.println(COUNT);
    float[][] data = new float[2][COUNT];
    int i = 0;
    System.out.format("FF  Cost%n");
    for (Solution solution : result)
    {
      System.out.format("%.4f      %.4f%n", solution.getObjective(0), solution.getObjective(1));
      data[0][i] = (float) solution.getObjective(0);
      data[1][i] = (float) solution.getObjective(1);
      i++;
    }

    plotParetoFront plotChart = new plotParetoFront("Pareto solutions", data);
    plotChart.pack();
    RefineryUtilities.centerFrameOnScreen(plotChart);
    plotChart.setVisible(true);

    Analyzer analyzer = new Analyzer().withProblem("moea.PPSBeamProblem").includeHypervolume();

    Executor executor = new Executor().withProblem("moea.PPSBeamProblem").withProperty("populationSize", 10000)
        .withMaxEvaluations(10000);
    analyzer.addAll("NSGAII", executor.withAlgorithm("NSGAII").runSeeds(1));

    analyzer.printAnalysis();

  }
}