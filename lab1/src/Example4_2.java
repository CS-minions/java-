class Circle
{
    double radius; // 声明一个双精度类型的变量 'radius'，用于存储圆的半径
    double getArea()
    {
        double area = 3.14 * radius * radius; // 计算圆的面积，公式为 π * r * r
        return area; // 返回计算得到的面积值
    }
}

public class Example4_2
{
    public static void main(String args[])
    {
        Circle circle; // 声明一个 Circle 类型的对象引用 'circle'
        circle = new Circle(); // 创建一个新的 Circle 对象，并将其引用赋给 'circle'
        circle.radius = 1; // 将 'circle' 对象的 'radius' 属性设置为 1
        double area = circle.getArea(); // 调用 'circle' 对象的 'getArea' 方法，计算面积并赋值给变量 'area'
        System.out.println(area); // 在控制台输出面积的值
    }
}
