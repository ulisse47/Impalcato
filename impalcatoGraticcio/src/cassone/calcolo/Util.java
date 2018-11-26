/*
 * Created on 7-mar-2004
 * Andrea Cavalieri
 *
 */
package cassone.calcolo;

/**
 * @author Andrea
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Util {

  /**
   *
   * @param arr
   * @return
   */
  public static double[][] copyArray(double[][] arr) {
    double[][] copy = new double[arr.length][arr[0].length];
    for (int i = 0; i < arr.length; i++) {
      for (int z = 0; z < arr[i].length; z++) {
        copy[i][z] = arr[i][z];
      }
    }
    return copy;
  }

  /**
   *
   * @param arr
   * @return
   */
  public static double[][][] copyArray(double[][][] arr) {
    double[][][] copy = new double[arr.length][arr[0].length][arr[0][0].length];
    for (int i = 0; i < arr.length; i++) {
      for (int z = 0; z < arr[i].length; z++) {
        for (int k = 0; k < arr[i][z].length; k++) {
          copy[i][z][k] = arr[i][z][k];
        }
      }
    }
    return copy;
  }

  //FUNZIONI IPERBOLICHE

  /**
   *
   * @param XSER
   * @return
   */
  public static double FNSH(double XSER) {
    double FNSH;
    FNSH = (Math.exp(XSER) - Math.exp( -XSER)) / 2.0;
    return FNSH;
  }

  public static double FNCH(double XSER) {
    return (Math.exp(XSER) + Math.exp( -XSER)) / 2.0;
  }

  public static double FNP(double USER, double TH, double SI) {
    return (SI * FNCH(SI) - FNSH(SI)) * FNCH(TH * USER)
        - TH * USER * FNSH(SI) * FNSH(TH * USER);
  }

  public static double FNQ9(double USER, double SI, double TH) {
    return (2 * FNSH(SI) + SI * FNCH(SI)) * FNSH(TH * USER)
        - TH * USER * FNSH(SI) * FNCH(TH * USER);
  }

}
