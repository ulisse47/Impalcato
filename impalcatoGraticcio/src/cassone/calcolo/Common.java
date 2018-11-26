/*
 * Created on 7-mar-2004
 * Andrea Cavalieri
 *
 */
package cassone.calcolo;


/**
 * @author Andrea
 *         <p/>
 *         To change the template for this generated type comment go to
     *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class Common {

  /**
   * nome dell'opera
   */
  public static String nome = "";

  /**
   * numero conci
   */
  public static int nconci = 1;

  /**
   * numero massimo di conci
   */
  public static int nconci_max = 200;

  /**
   * numero di n diversi
   */
  public static int numn = 4;

  /**
   *
   */
  public static double[] n = {
      Double.MAX_VALUE, //1
      22, //2
      6.24, //3
      14,//4
      };

  /** numero di sezioni massime di verifica ( per gli array )*/
  public static int nsez = 200;


  /** numero massimo di condizioni di carico per sezione */
  public static int nsol = 200;

  /** x,L1,L2, Concio */
  public static int ntipisez = 4;

  /** n,N,V,M,Nsoletta */
  public static int ntipisol = 5;

  /** numero di condizioni di carico */
  public static int ncond = 1;

  /** numero di combinazioni di carico */
  public static int ncomb = 2;

 /** numero di combinazioni di carico */
  public static final int ncombMax = 20;


  /** numero di sezioni di verifica */
  public static int sezioni = 1;

  /** la sezione corrente, default la prima */
  public static int current_sezione = 0;

  /** il concio corrente, default il primo */
  public static int current_concio = 0;

  //PARAMETRI DI INPUT//////////////////////////////////

  //seconda parte
  public static double[][] sez = new double[nsez][ntipisez];
  public static double[][][] sol = new double[nsez][nsol][ntipisol];
  public static double[][] comb = new double[nsol][ncombMax];
  //nuova variabile, desrcizione delle condizioni di carico
  public static String[] desc_cond = new String[ nsol ];


  //prima parte
  public static double[] B = new double[nconci_max]; //1
  public static double[] H = new double[nconci_max]; //3

  public static double[] bs = new double[nconci_max]; //2
  public static double[] hs = new double[nconci_max]; //4

  public static double[] ba = new double[nconci_max]; //5
  public static double[] ha = new double[nconci_max]; //6
  
  public static double[] bi = new double[nconci_max];
  public static double[] hi = new double[nconci_max];
  
  public static double[] Af = new double[nconci_max];
 
  
  
  public static double[] b3 = new double[nconci_max];
  public static double[] s3 = new double[nconci_max];
  public static double[] sec = new double[nconci_max];
  public static double[] alfa = new double[nconci_max];
  public static double[] al = new double[nconci_max];
  public static double[] hl = new double[nconci_max];
  public static double[] ar = new double[nconci_max];
  public static double[] hr = new double[nconci_max];
  public static double[] ani = new double[nconci_max];
  public static double[] hni = new double[nconci_max];
  public static double[] xni = new double[nconci_max];
  public static double[] hsa = new double[nconci_max];
  public static double[] asa = new double[nconci_max];
  public static double[] acs = new double[nconci_max];

  //PARAMETRI DI OUTPUT
  public static double[][] Aid = new double[nconci_max][numn];
  public static double[][] Jid = new double[nconci_max][numn];
  public static double[][] yg = new double[nconci_max][numn];
  
  
  public static double[][] jxi = new double[nconci_max][numn];
  public static double[][] jyi = new double[nconci_max][numn];
  public static double[][] an = new double[nconci_max][numn];
  public static double[][] wx1 = new double[nconci_max][numn];
  public static double[][] wx2 = new double[nconci_max][numn];
  public static double[][] wx3 = new double[nconci_max][numn];
  public static double[][] wx4 = new double[nconci_max][numn];
  public static double[][] wx5 = new double[nconci_max][numn];
  public static double[][] wx6 = new double[nconci_max][numn];
  public static double[][] wy1 = new double[nconci_max][numn];
  public static double[][] wy2 = new double[nconci_max][numn];
  public static double[][] wy3 = new double[nconci_max][numn];
  public static double[][] ms1 = new double[nconci_max][numn];
  public static double[][] ms2 = new double[nconci_max][numn];
  public static double[][] ms3 = new double[nconci_max][numn];
  public static double[][] ms4 = new double[nconci_max][numn];
  public static double[][] ana = new double[nconci_max][numn];
  public static double[][] jt = new double[nconci_max][numn];
  public static double[][] aso = new double[nconci_max][numn];

  public static Object[][][] tensioni; //new Object[ sezioni ][ ncond + 2 ][ 17 ];

  public static double[][][] tensioni_ideali; //[sezioni][10]*[2]


}
