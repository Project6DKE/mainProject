package MainProject;

public class TrapezoidIntegration {
    private final double h;
    private final double[] y;

    public TrapezoidIntegration(double h, double[] y) {
        this.h = h;
        this.y = y;
    }

    public double get() {
        int startIndex = 0;
        int endIndex = y.length - 1;

        double sum = 0;

        for (int i = 1; i < endIndex; i++ ) {
            sum += y[i];
        }

        return h * (0.5 * y[startIndex] + sum + 0.5 * y[endIndex]);
    }
}
