package MainProject;


class GaussSeidel{
  public static double [] get(double [][]A, int iterations){
    sortDiagonal(A);

    double [] last_results = new double [A[0].length -1];// 5 unknows solve in vector solution
    double [] results = new double [A[0].length -1];// 5 unknows solve in vector solution

    for(int y =0; y < A[0].length -1 ; y++){
      last_results [y] = 0;
      results [y] = 0;
    }

    double coef1 = 0;
    double coef2 = 0;
    int counter =1;

    double [][] B = resizeMat(A);

          while( !(last_results.equals(results)) && counter < iterations){// 30 iterations seems like a good average on 5 equation: experiment on convergence speed

          for (int i=0; i < B.length; i ++){

              /*COEF1 COMPUTATION*/
            for(int j= i+1; j < B.length ; j ++){
                coef2 += (B[i][j] * results[j]);
            }
              /*COEF2 COMPUTATION*/
            if(i==0){//first line, special case: using solution from previous iteration when available
              for(int j=1; j < i; j ++){
                coef1 += (B[i][j] * results[j]);
              }
            }
            else {
                for(int j=0; j < i ; j ++){
                coef1 += ( B[i][j] * last_results[j]);
              }
          }
          /* computation of solution vector entry for the current line
          stored at the rightmost position of the matrix
          B[5] corresponding to constant vector column
          */
          B[i][A[0].length]= (B[i][A[0].length -1] - (coef1 + coef2) )/ B[i][i];

          /*storing result in solution vector*/
          last_results[i] = B[i][A[0].length];

          /*reinitializing coefficients*/
          coef2=0;
          coef1=0;
        }

        /*storing solution after each complete iteration over the matrix lines*/
        for (int a = 0 ; a < A.length; a++){
          results[a] = B[a][A[0].length];
      }
          counter++;
        }
        return last_results;
      }


/*Mutator Operation: Matrix Gauss-Seidel Solver*/
  /* @param A[] corresponds to a matrix of coefficient to poly degree zero to 3 a for each equation AND its last colum contain the x vector*/
public static double [][] resizeMat (double [][]A){
  double [][] B = new double [A.length][A[0].length +1];
  for (int p = 0; p < A.length; p++){
      System.arraycopy(A[p], 0, B[p], 0, A[0].length);
  }
  for (int p = 0; p < B.length; p++){ //fills last colum with zero (initial solution vector)
      B[p][A[0].length]= 0;
    }
  return B;
}

public static double [][] sortDiagonal (double [][]A){
  for( int i = 0; i < A.length; i++){
        if (A[i][i] == 0){

          //SEARCH UP
          if(A[i][i-1] != 0){// we are still IN array if we move to left column && there is no zero on the left of current zero
            if(A[i-1][i] != 0){// we are still IN array if we move to line upper && there is no zero in same colum upper line

              //REPLACE line
              //1 copy line and store in temporary variable
                double [][] T = new double [1][A[0].length];

                System.arraycopy(A[i], 0, T[0], 0, A[0].length);
              //current line becomes upper line
                System.arraycopy(A[i + 1], 0, A[i], 0, A[0].length);
              //upper line becomes Temporary variable
                System.arraycopy(T[i], 0, A[i + 1], 0, A[0].length);
          }
        }
        // SEARCH DOWN
        if(i+1 <= A[0].length && A[i][i+1] != 0 ){// we are still IN array if we move to right column && there is no zero on the right of current zero
          if(i+1 <= A.length && A[i-1][i-1] != 0){// we are still IN array if we move to downer line && there is no zero in same colum downer line

            //REPLACE line
            //1 copy line and store in temporary variable
            double [][] T = new double [2][A[0].length];

              System.arraycopy(A[i], 0, T[0], 0, A[0].length);

            //current line becomes downer line
              System.arraycopy(A[i + 1], 0, A[i], 0, A[0].length);
            //downer line becomes Temporary variable
              System.arraycopy(T[0], 0, A[i + 1], 0, A[0].length);
        }
      }
  }
}
  return A;
}
}


class RunGauss {
    public static void main(String[] args) {
        double [][] matrix = {
                {1, 1, 0, 0, 0, 1},
                {0, 0, 1, 1, 1, 2},
                {1, 3, -1, 0, 0, 0},
                {0, 3, 0, -1, 0, 0},
                {0, 0, 0, 2, 6, 0},
        };

        double[] expectedVector = { 3.0/4.0, 1.0/4.0, 3.0/2.0, 3.0/4.0, -1.0/4.0 };

        testChangingIterations(matrix, expectedVector);
    }

    public static void testChangingIterations(double[][] matrix, double[] expectedVector) {
        int maxIterations = 100;
        int stepSize = 10;

        for (int i = 10; i < maxIterations; i += stepSize) {
            printError(matrix, expectedVector, i);
        }
    }

    public static void printError(double[][] matrix, double[] expectedVector, int i) {
        double[] resultVector = GaussSeidel.get(matrix, i);

        VectorError vectorError = new VectorError(resultVector, expectedVector);
        System.out.println(vectorError.getMeanSquareError());
    }

    public static void printVector(double[] vector) {
        System.out.println("Vector:");
        for (double element: vector) {
            System.out.println(element);
        }
    }
}

class VectorError {
    private final double[] actualVector;
    private final double[] expectedVector;
    private double sumSquareDifference;

    public VectorError(double[] actualVector, double[] expectedVector) {
        this.actualVector = actualVector;
        this.expectedVector = expectedVector;
    }

    public double getMeanSquareError() {
        int rows = actualVector.length - 1;

        for (int i = 0; i <= rows; i++) {
            updateSumSquareDifference(actualVector[i], expectedVector[i]);
        }

        return sumSquareDifference / rows;
    }

    private void updateSumSquareDifference(double p, double q) {
        sumSquareDifference += Math.pow(p - q, 2);
    }
}