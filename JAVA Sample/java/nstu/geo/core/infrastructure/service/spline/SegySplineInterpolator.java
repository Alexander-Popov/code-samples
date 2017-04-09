package nstu.geo.core.infrastructure.service.spline;


import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class SegySplineInterpolator {
    private PolynomialSplineFunction splineFunction;

    public void interpolate(int[] xF, float[] yF) {
        double[] x = new double[xF.length];
        for (int i = 0 ; i < xF.length; i++)
        {
            x[i] = (double) xF[i];
        }
        double[] y = new double[yF.length];
        for (int k = 0 ; k < yF.length; k++)
        {
            y[k] = (double) yF[k];
        }

        SplineInterpolator interpolator = new SplineInterpolator();
        splineFunction = interpolator.interpolate(x, y);
    }

    public double value(double x) {
        return splineFunction.value(x);
    }
}
