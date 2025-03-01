package simulator.misc;

public class Vector {

    private final int dimension; // Dimension of the vector
    private final double[] components; // Array of vector's components

    // Create the zero vector of length n
    public Vector(int dimension) {
        this.dimension = dimension;
        this.components = new double[dimension];
    }

    // Copy constructor
    public Vector(Vector v) {
        this.dimension = v.dimension;
        this.components = v.components.clone();
    }

    // Create a vector from an array
    public Vector(double[] components) {
        this.dimension = components.length;
        this.components = components.clone();
    }

    // Return the dimension of the vector
    public int dim() {
        return dimension;
    }

    // Return the inner product of this Vector and another
    public double dot(Vector that) {
        if (dim() != that.dim()) {
            throw new IllegalArgumentException("Dimensions disagree");
        }
        double sum = 0.0;
        for (int i = 0; i < dimension; i++) {
            sum += components[i] * that.components[i];
        }
        return sum;
    }

    // Return the length of the vector (Euclidean norm)
    public double magnitude() {
        return Math.sqrt(dot(this));
    }

    // Return the distance between this and another vector (Euclidean)
    public double distanceTo(Vector that) {
        if (dim() != that.dim()) {
            throw new IllegalArgumentException("Dimensions disagree");
        }
        return minus(that).magnitude();
    }

    // Create and return a new vector whose value is (this + that)
    public Vector plus(Vector that) {
        if (dim() != that.dim()) {
            throw new IllegalArgumentException("Dimensions disagree");
        }
        Vector result = new Vector(dimension);
        for (int i = 0; i < dimension; i++) {
            result.components[i] = components[i] + that.components[i];
        }
        return result;
    }

    // Create and return a new vector whose value is (this - that)
    public Vector minus(Vector that) {
        if (dim() != that.dim()) {
            throw new IllegalArgumentException("Dimensions disagree");
        }
        Vector result = new Vector(dimension);
        for (int i = 0; i < dimension; i++) {
            result.components[i] = components[i] - that.components[i];
        }
        return result;
    }

    // Return the corresponding coordinate
    public double coordinate(int i) {
        return components[i];
    }

    // Create and return a new vector whose value is (this * factor)
    public Vector scale(double factor) {
        Vector result = new Vector(dimension);
        for (int i = 0; i < dimension; i++) {
            result.components[i] = factor * components[i];
        }
        return result;
    }

    // Return the corresponding unit vector
    public Vector direction() {
        double magnitude = magnitude();
        if (magnitude > 0.0) {
            return scale(1.0 / magnitude);
        }
        return new Vector(this);
    }

    // Return a string representation of the vector
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < dimension; i++) {
            sb.append(components[i]);
            if (i < dimension - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }
}