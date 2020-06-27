package MainProject;

import java.lang.Math;

public class CoefficientMatrix {
    private final double[] x;
    private final double[] y;

    private Function[] firstSetOfEqualities;
    private Function[] secondSetOfEqualities;
    private Function[] thirdSetOfEqualities;

    public CoefficientMatrix(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        setupCoefficients();
        findCoefficients();
    }

    public double getAWith(int i){
        return y[i];
    }

    public double getBeginInterval(int i){
        return x[i];
    }

    public double[][] getMatrixAtIndex(int i) {
        warnIfMatrixDoesNotExistAtIndex(i);

        // Equation 1
        double[] row1 = firstSetOfEqualities[i].getArrayForm();

        // Equation 2
        double[] row2 = firstSetOfEqualities[i + 1].getMoveForwardArrayForm();

        // Equation 3
        double[] row3 = secondSetOfEqualities[i].getArrayForm();

        // Equation 4
        double[] row4 = thirdSetOfEqualities[i].getArrayForm();

        // Equation 5
        double[] row5 = thirdSetOfEqualities[i + 1].getMoveForwardArrayForm();

        return new double[][]{row1, row2, row3, row4, row5};
    }

    private void warnIfMatrixDoesNotExistAtIndex(int i) {
        boolean invalidIndex = i >= y.length - 2;

        if (invalidIndex) {
            String errorMessage = "The input argument i must be smaller than y.length - 2";
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }
    }

    private void setupCoefficients() {
        int yMinusOne = y.length - 1;

        firstSetOfEqualities = new Function[yMinusOne];
        secondSetOfEqualities = new Function[yMinusOne];
        thirdSetOfEqualities = new Function[yMinusOne];
    }

    private void findCoefficients() {
        int yLength = y.length;

        for (int i = 1; i < yLength; i++) {
            findFirstEquationSetAt(i);
            findSecondEquationSetAt(i);
            findThirdEquationSetAt(i);
        }
    }

    private void findFirstEquationSetAt(int i) {
        // First matching condition
        // S0(x) = S1(x)

        double yMinusX = y[i - 1] - x[i - 1];
        double cMultiplier = Math.pow(yMinusX, 2);
        double dMultiplier = Math.pow(yMinusX, 3);

        Function leftSide = new Function(yMinusX, cMultiplier, dMultiplier, 0);

        double constant =  y[i - 1] - y[i];
        Function rightSide = new Function(0, 0, 0, constant);

        firstSetOfEqualities[i - 1] = leftSide.equals(rightSide);
    }

    private void findSecondEquationSetAt(int i) {
        // Second matching condition
        // S'0(x) = S'1(x)

        double xMinusPreviousX = x[i] - x[i - 1];
        Function leftSide = getOneSideSecondMatchingCondition(xMinusPreviousX);

        double noDifference = 0;
        Function rightSide = getOneSideSecondMatchingCondition(noDifference);

        secondSetOfEqualities[i - 1] = leftSide.equals(rightSide);
    }

    private void findThirdEquationSetAt(int i) {
        // Third matching condition
        // S''0(x) = S''1(x)

        double xMinusPreviousX = x[i] - x[i - 1];
        Function leftSide = getOneSideThirdMatchingCondition(xMinusPreviousX);

        double noDifference = 0;
        Function rightSide = getOneSideThirdMatchingCondition(noDifference);

        thirdSetOfEqualities[i - 1] = leftSide.equals(rightSide);
    }

    private Function getOneSideSecondMatchingCondition(double difference) {
        double bMultiplier = 1;
        double cMultiplier = 2 * difference;
        double dMultiplier = 3 * Math.pow(difference, 2);

        return new Function(bMultiplier, cMultiplier, dMultiplier, 0);
    }

    private Function getOneSideThirdMatchingCondition(double difference) {
        double cMultiplier = 2;
        double dMultiplier = 6 * difference;

        return new Function(0, cMultiplier, dMultiplier, 0);
    }
}


class LinearEquation {
    public double b, c, d, constant;

    LinearEquation(double b, double c, double d, double constant) {
        this.b = b;
        this.c = c;
        this.d = d;

        this.constant = constant;
    }
}

class Function extends LinearEquation {
    LinearEquation rightSide;

    Function(double b, double c, double d, double constant) {
        super(b, c, d, constant);
    }

    public double[] getArrayForm() {
        return new double[]{b, d, c,
                            -rightSide.b, -rightSide.c, -rightSide.d,
                            -constant
                            };
    }

    public double[] getMoveForwardArrayForm() {
        return new double[]{0, 0, 0,
                b, c, d,
                -constant
        };
    };

    public Function equals(LinearEquation equalLinearEquation) {
        rightSide = equalLinearEquation;

        constant = constant - equalLinearEquation.constant;
        equalLinearEquation.constant = 0;

        return this;
    }
}