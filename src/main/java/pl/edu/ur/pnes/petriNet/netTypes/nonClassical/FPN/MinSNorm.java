package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

class MinSNorm extends SNorm {
    @Override
    public double applyAsDouble(double left, double right) {
        return Math.min(left, right);
    }
}
