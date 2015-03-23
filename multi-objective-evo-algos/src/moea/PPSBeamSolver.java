package moea;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartUtilities;
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
      // generate the file to write the data to
      List<File> files = generateFiles(algorithms[i]);
      PrintStream txt = new PrintStream(files.get(0));

      // configure and run this experiment
      NondominatedPopulation result = new Executor().withProblem("moea.PPSBeamProblem").withAlgorithm(algorithms[i])
          .withMaxEvaluations(10000).distributeOnAllCores().run();

      // write to file
      int COUNT = result.size();
      float[][] data = new float[2][COUNT];
      int j = 0;
      txt.format("FF  Cost%n");
      for (Solution solution : result)
      {
        txt.format("%.4f      %.4f%n", solution.getObjective(0), solution.getObjective(1));
        data[0][j] = (float) solution.getObjective(0);
        data[1][j] = (float) solution.getObjective(1);
        j++;
      }

      // create the pareto and save to file
      plotParetoFront plotChart = new plotParetoFront(algorithms[i], data);
      plotChart.pack();
      RefineryUtilities.centerFrameOnScreen(plotChart);
      plotChart.setVisible(true);
      ChartUtilities.saveChartAsJPEG(files.get(1), plotChart.pareto, plotChart.getWidth(), plotChart.getHeight());

      // Write the hypervolume
      txt.print("\n\n");

      Analyzer analyzer = new Analyzer().withProblem("moea.PPSBeamProblem").includeHypervolume();
      analyzer.add(algorithms[i], result);
      analyzer.printAnalysis(txt);
      txt.close();
    }
    System.out.println("Done!");
  }

  /**
   * Returns a list of File objects pointing to a jpg for a pareto chart and a
   * txt file for the corresponding data.
   */
  private static List<File> generateFiles(String algorithm)
  {
    String timeStamp = setTimeStamp();
    String txtPath = "Data/" + algorithm + "/" + timeStamp + ".txt";
    String imgPath = "Data/" + algorithm + "/" + timeStamp + ".jpg";

    List<File> files = new ArrayList<File>();
    files.add(new File(txtPath));
    files.add(new File(imgPath));

    return files;
  }

  /**
   * Returns a timestamp for file names.
   */
  private static String setTimeStamp()
  {
    SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd HH-mm-ss");
    Date now = new Date();
    return sdfDate.format(now);
  }
}