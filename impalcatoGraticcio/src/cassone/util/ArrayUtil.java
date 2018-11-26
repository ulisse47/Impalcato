package cassone.util;

public class ArrayUtil {

  public static double[] filterArray( double[] old , int toDelete ){
        double[] newArr = new double[ old.length ];
        System.arraycopy( old, 0, newArr, 0, toDelete );
        System.arraycopy( old, toDelete + 1, newArr, toDelete, old.length - toDelete - 1 );
        return newArr;
  }


  public static double[][][] filterArray( double[][][] old , int toDelete ){
      double[][][] newArr = new double[ old.length ][old[0].length][old[0][0].length];
      System.arraycopy( old, 0, newArr, 0, toDelete );
      System.arraycopy( old, toDelete + 1, newArr, toDelete, old.length - toDelete - 1 );
      return newArr;
}



  public static double[][] filterArray( double[][] old , int toDelete ){
       double[][] newArr = new double[ old.length ][old[0].length];
       System.arraycopy( old, 0, newArr, 0, toDelete );
       System.arraycopy( old, toDelete + 1, newArr, toDelete, old.length - toDelete - 1 );
       return newArr;
 }


 }

