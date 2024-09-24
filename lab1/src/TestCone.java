class Cone {
    double radius; // 圆锥的半径
    double height; // 圆锥的高

    // 设置半径和高的方法
    void setInfo(double radius, double height) {
        this.radius = radius; // 将参数 radius 赋值给实例变量 radius
        this.height = height; // 将参数 height 赋值给实例变量 height
    }

    // 计算体积的方法
    double volume() {
        // 体积公式：V = 1/3 * π * r^2 * h
        return (1.0 / 3) * Math.PI * radius * radius * height; // 返回计算得到的体积
    }

    // 计算表面积的方法
    double area() {
        // 计算斜高 l，公式：l = sqrt(r^2 + h^2)
        double slantHeight = Math.sqrt(radius * radius + height * height); // 计算斜高
        // 表面积公式：A = π * r * (r + l)
        return Math.PI * radius * (radius + slantHeight); // 返回计算得到的表面积
    }

    // 将圆锥的属性和计算结果转换为字符串的方法
    public String toString() {
        // 构建包含半径、高、体积和表面积的字符串并返回
        return "半径: " + radius + ", 高: " + height +
                ", 体积: " + volume() + ", 表面积: " + area();
    }
}

// 测试类
public class TestCone {
    public static void main(String[] args) {
        Cone cone = new Cone();     // 创建一个 Cone 类的对象实例
        cone.setInfo(3, 5);         // 设置圆锥的半径为 3，高为 5

        System.out.println(cone);   // 输出 Cone 对象的信息，自动调用 toString 方法
    }
}
