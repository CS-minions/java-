// 定义研究生类 PostGraduateStudent
class PostGraduateStudent {
    String ID;           // 学号
    String name;         // 姓名
    double math;         // 数学成绩
    double programming;  // 编程成绩
    double english;      // 英语成绩

    // 设置学生信息的方法
    void setInfo(String ID, String name, double math, double programming, double english) {
        this.ID = ID;                     // 设置学号
        this.name = name;                 // 设置姓名
        this.math = math;                 // 设置数学成绩
        this.programming = programming;   // 设置编程成绩
        this.english = english;           // 设置英语成绩
    }

    // 计算三门课程总分的方法
    double comSum() {
        return math + programming + english; // 返回总分
    }

    // 计算三门课程平均分的方法
    double comAvg() {
        return comSum() / 3; // 返回平均分
    }

    // 计算三门课程最高分的方法
    double comMax() {
        // 使用 Math.max 方法比较成绩
        return Math.max(math, Math.max(programming, english)); // 返回最高分
    }

    // 比较两个学生总分的方法，参数是另一个 PostGraduateStudent 对象
    int compare(PostGraduateStudent other) {
        double totalScoreThis = this.comSum();      // 当前学生的总分
        double totalScoreOther = other.comSum();    // 另一个学生的总分

        // 比较总分大小
        if (totalScoreThis > totalScoreOther) {
            return 1;   // 当前学生总分较高，返回 1
        } else if (totalScoreThis < totalScoreOther) {
            return -1;  // 当前学生总分较低，返回 -1
        } else {
            return 0;   // 总分相等，返回 0
        }
    }

    // 将学生信息转换为字符串的方法
    public String toString() {
        // 构建包含学生信息的字符串并返回
        return "学号: " + ID + ", 姓名: " + name +
                ", 数学: " + math + ", 编程: " + programming + ", 英语: " + english +
                ", 总分: " + comSum() + ", 平均分: " + comAvg() + ", 最高分: " + comMax();
    }
}

// 测试类
public class TestPostGraduateStudent {
    public static void main(String[] args) {
        // 创建两个 PostGraduateStudent 对象
        PostGraduateStudent student1 = new PostGraduateStudent(); // 学生1
        PostGraduateStudent student2 = new PostGraduateStudent(); // 学生2

        // 设置学生1的信息
        student1.setInfo("2021001", "张三", 85.5, 90.0, 78.0);
        // 设置学生2的信息
        student2.setInfo("2021002", "李四", 88.0, 85.5, 80.0);

        // 输出学生1的信息
        System.out.println(student1);
        // 输出学生2的信息
        System.out.println(student2);

        // 比较两个学生的总分
        int comparisonResult = student1.compare(student2);

        // 根据比较结果输出信息
        if (comparisonResult > 0) {
            System.out.println(student1.name + " 的总分更高。");
        } else if (comparisonResult < 0) {
            System.out.println(student2.name + " 的总分更高。");
        } else {
            System.out.println("两位学生的总分相等。");
        }
    }
}
