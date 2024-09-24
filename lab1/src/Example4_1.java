class Ladder
{
    double above, bottom, height; // 声明三个 double 类型的变量，表示梯形的上底、下底和高

    Ladder(){} // 无参数构造方法

    Ladder(double a, double b, double h) // 带参数的构造方法，使用参数初始化上底、下底和高
    {
        above = a;   // 将参数 a 赋值给 above（上底）
        bottom = b;  // 将参数 b 赋值给 bottom（下底）
        height = h;  // 将参数 h 赋值给 height（高）
    }

    public void setAbove(double a) // 设置上底的方法
    {
        above = a; // 将参数 a 赋值给 above
    }

    public void setBottom(double b) // 设置下底的方法
    {
        bottom = b; // 将参数 b 赋值给 bottom
    }

    public void setHeight(double h) // 设置高的方法
    {
        height = h; // 将参数 h 赋值给 height
    }

    double computeArea() // 计算梯形面积的方法
    {
        return (above + bottom) * height / 2.0; // 根据梯形面积公式计算并返回面积
    }
}

public class Example4_1
{
    public static void main(String args[])
    {
        double area1 = 0, area2 = 0; // 声明两个 double 类型的变量，用于存储面积
        Ladder ladderOne, ladderTwo; // 声明两个 Ladder 类型的对象引用

        ladderOne = new Ladder(); // 创建一个无参数的 Ladder 对象实例 ladderOne
        ladderTwo = new Ladder(10, 88, 20); // 创建一个带参数的 Ladder 对象实例 ladderTwo，初始化上底、下底和高

        ladderOne.setAbove(16);   // 设置 ladderOne 的上底为 16
        ladderOne.setBottom(26);  // 设置 ladderOne 的下底为 26
        ladderOne.setHeight(100); // 设置 ladderOne 的高为 100

        ladderTwo.setAbove(300);  // 修改 ladderTwo 的上底为 300
        ladderTwo.setBottom(500); // 修改 ladderTwo 的下底为 500

        area1 = ladderOne.computeArea(); // 计算 ladderOne 的面积并赋值给 area1
        area2 = ladderTwo.computeArea(); // 计算 ladderTwo 的面积并赋值给 area2

        System.out.println(area1); // 输出 ladderOne 的面积
        System.out.println(area2); // 输出 ladderTwo 的面积
    }
}
