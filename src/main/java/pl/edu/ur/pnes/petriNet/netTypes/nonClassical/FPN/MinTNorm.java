package pl.edu.ur.pnes.petriNet.netTypes.nonClassical.FPN;

class MinTNorm extends TNorm {
    @Override
    public double applyAsDouble(double left, double right) {
        System.out.println("MinTNorm.applyAsDouble");
        System.out.println("left = " + left + ", right = " + right);

        return Math.min(left, right);
    }
}
