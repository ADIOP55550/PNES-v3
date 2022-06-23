package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

class MaxSNorm extends SNorm {
    @Override
    public double applyAsDouble(double left, double right) {
        System.out.println("MaxSNorm.applyAsDouble");
        System.out.println("left = " + left + ", right = " + right);
        return Math.max(left, right);
    }
}
