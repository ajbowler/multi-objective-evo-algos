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
    // Run this with two different algorithms.
    String[] algorithms = { "NSGAII", "GDE3" };

    for (int i = 0; i < algorithms.length; i++)
    {
      // configure and run this experiment
      NondominatedPopulation result = new Executor().withProblem("moea.PPSBeamProblem").withAlgorithm(algorithms[i])
          .withMaxEvaluations(10000).distributeOnAllCores().run();

      // display the results
      int COUNT = result.size();
      float[][] data = new float[2][COUNT];
      int j = 0;
      System.out.format("FF  Cost%n");
      for (Solution solution : result)
      {
        System.out.format("%.4f      %.4f%n", solution.getObjective(0), solution.getObjective(1));
        data[0][j] = (float) solution.getObjective(0);
        data[1][j] = (float) solution.getObjective(1);
        j++;
      }

      plotParetoFront plotChart = new plotParetoFront(algorithms[i], data);
      plotChart.pack();
      RefineryUtilities.centerFrameOnScreen(plotChart);
      plotChart.setVisible(true);

      Analyzer analyzer = new Analyzer().withProblem("moea.PPSBeamProblem").includeHypervolume();

      Executor nsgaii = new Executor().withProblem("moea.PPSBeamProblem").withMaxEvaluations(10000);
      analyzer.addAll(algorithms[i], nsgaii.withAlgorithm(algorithms[i]).runSeeds(50));
      analyzer.printAnalysis();
    }
  }
}