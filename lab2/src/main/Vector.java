package main;

interface Computable {
    int[] add(int[] vector);
    int[] minus(int[] vector);
    int[] elementwiseProduct(int[] vector);
    int innerProduct(int[] vector);
    double norm();
    int compare(Vector other);
}

public class Vector implements Computable {
    private int[] components;

    public Vector(int[] components) {
        if (components.length != 4) {
            throw new IllegalArgumentException("The vector must be a 4-dimensional vector.");
        }
        this.components = components;
    }

    @Override
    public int[] add(int[] vector) {
        if (vector.length != 4) {
            throw new IllegalArgumentException("The vector must be a 4-dimensional vector.");
        }
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.components[i] + vector[i];
        }
        return result;
    }

    @Override
    public int[] minus(int[] vector) {
        if (vector.length != 4) {
            throw new IllegalArgumentException("The vector must be a 4-dimensional vector.");
        }
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.components[i] - vector[i];
        }
        return result;
    }

    @Override
    public int[] elementwiseProduct(int[] vector) {
        if (vector.length != 4) {
            throw new IllegalArgumentException("The vector must be a 4-dimensional vector.");
        }
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = this.components[i] * vector[i];
        }
        return result;
    }

    @Override
    public int innerProduct(int[] vector) {
        if (vector.length != 4) {
            throw new IllegalArgumentException("The vector must be a 4-dimensional vector.");
        }
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += this.components[i] * vector[i];
        }
        return sum;
    }

    @Override
    public double norm() {
        int sum = 0;
        for (int component : this.components) {
            sum += component * component;
        }
        return Math.sqrt(sum);
    }

    @Override
    public int compare(Vector other) {
        double thisNorm = this.norm();
        double otherNorm = other.norm();
        return Double.compare(thisNorm, otherNorm);
    }

    public static void main(String[] args) {
        Vector v1 = new Vector(new int[]{3, 9, 2, 7});
        Vector v2 = new Vector(new int[]{2, -8, -1, 6});

        int[] sum = v1.add(v2.components);
        int[] difference = v1.minus(v2.components);
        int[] product = v1.elementwiseProduct(v2.components);
        int innerProduct = v1.innerProduct(v2.components);
        double norm = v1.norm();
        int comparison = v1.compare(v2);

        System.out.println("Sum: " + java.util.Arrays.toString(sum));
        System.out.println("Difference: " + java.util.Arrays.toString(difference));
        System.out.println("Element-wise Product: " + java.util.Arrays.toString(product));
        System.out.println("Inner Product: " + innerProduct);
        System.out.println("Norm: " + norm);
        System.out.println("Comparison (v1 vs v2): " + (comparison == 0 ? "Equal" : (comparison > 0 ? "v1 is larger" : "v2 is larger")));
    }
}
