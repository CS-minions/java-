class Box {
    int length; // 长方体的长
    int width;  // 长方体的宽
    int height; // 长方体的高

    // 设置长、宽、高的方法
    void setInfo(int length, int width, int height) {
        this.length = length; // 将参数 length 赋值给实例变量 length
        this.width = width;   // 将参数 width 赋值给实例变量 width
        this.height = height; // 将参数 height 赋值给实例变量 height
    }

    // 计算体积的方法
    int volume() {
        return length * width * height; // 返回体积，公式：长 * 宽 * 高
    }

    // 计算表面积的方法
    int area() {
        // 返回表面积，公式：2 * (长 * 宽 + 长 * 高 + 宽 * 高)
        return 2 * (length * width + length * height + width * height);
    }

    // 将长方体的信息转换为字符串的方法
    public String toString() {
        // 构建包含长、宽、高、体积和表面积的字符串并返回
        return "长: " + length + ", 宽: " + width + ", 高: " + height +
                ", 体积: " + volume() + ", 表面积: " + area();
    }
}

// 测试类
public class TestBox {
    public static void main(String[] args) {
        Box box = new Box();      // 创建一个 Box 类的对象实例
        box.setInfo(5, 4, 3);     // 调用 setInfo 方法设置长方体的长、宽、高

        System.out.println(box);  // 输出 Box 对象的信息，调用 toString 方法
    }
}
