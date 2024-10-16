package main;

public class Complex {
    private double realPart;
    private double imagePart;

    // 构造方法，将实数部分和虚数部分都置为0
    public Complex() {
        this.realPart = 0;
        this.imagePart = 0;
    }

    // 构造方法，将实数部分置为r，虚数部分置为i
    public Complex(double r, double i) {
        this.realPart = r;
        this.imagePart = i;
    }

    // 将当前复数对象与形参复数对象相减
    public Complex complexSub(Complex c) {
        return new Complex(this.realPart - c.realPart, this.imagePart - c.imagePart);
    }

    // 将当前复数对象与形参复数对象相乘
    public Complex complexMult(Complex c) {
        double real = this.realPart * c.realPart - this.imagePart * c.imagePart;
        double imag = this.realPart * c.imagePart + this.imagePart * c.realPart;
        return new Complex(real, imag);
    }

    // 重写toString方法，返回复数的字符串形式
    @Override
    public String toString() {
        return realPart + "+" + imagePart + "i";
    }
    // Getter for realPart
    public double getRealPart() {
        return realPart;
    }

    // Getter for imagePart
    public double getImagePart() {
        return imagePart;
    }
}
