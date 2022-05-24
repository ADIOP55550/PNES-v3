package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

class MinSNorm implements Aggregation {
    @Override
    public double applyAsDouble(double left, double right) {
        return Math.min(left, right);
    }
}
