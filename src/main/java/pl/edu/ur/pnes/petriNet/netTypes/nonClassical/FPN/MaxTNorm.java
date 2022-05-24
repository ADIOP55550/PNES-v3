package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

class MaxTNorm implements Aggregation {
    @Override
    public double applyAsDouble(double left, double right) {
        return Math.max(left, right);
    }
}
