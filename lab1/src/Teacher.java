// 定义 Teacher 类
public class Teacher {
    // 成员变量
    private String name;      // 姓名
    private String title;     // 职位
    private String course;    // 主讲的课程
    private String research;  // 研究方向
    private String office;    // 办公室

    // 设置姓名的方法
    public void setName(String name) {
        this.name = name; // 将参数 name 赋值给成员变量 name
    }

    // 获取姓名的方法
    public String getName() {
        return name; // 返回成员变量 name 的值
    }

    // 设置职位的方法
    public void setTitle(String title) {
        this.title = title; // 将参数 title 赋值给成员变量 title
    }

    // 获取职位的方法
    public String getTitle() {
        return title; // 返回成员变量 title 的值
    }

    // 设置主讲课程的方法
    public void setCourse(String course) {
        this.course = course; // 将参数 course 赋值给成员变量 course
    }

    // 获取主讲课程的方法
    public String getCourse() {
        return course; // 返回成员变量 course 的值
    }

    // 设置研究方向的方法
    public void setResearch(String research) {
        this.research = research; // 将参数 research 赋值给成员变量 research
    }

    // 获取研究方向的方法
    public String getResearch() {
        return research; // 返回成员变量 research 的值
    }

    // 设置办公室的方法
    public void setOffice(String office) {
        this.office = office; // 将参数 office 赋值给成员变量 office
    }

    // 获取办公室的方法
    public String getOffice() {
        return office; // 返回成员变量 office 的值
    }

    // 在 Teacher 类内的 main 方法
    public static void main(String[] args) {
        // 创建 Teacher 类的对象
        Teacher teacher = new Teacher();

        // 设置教师的信息
        teacher.setName("李明");
        teacher.setTitle("教授");
        teacher.setCourse("计算机科学");
        teacher.setResearch("人工智能");
        teacher.setOffice("科技楼 305 室");

        // 获取并输出教师的信息
        System.out.println("姓名：" + teacher.getName());
        System.out.println("职位：" + teacher.getTitle());
        System.out.println("主讲课程：" + teacher.getCourse());
        System.out.println("研究方向：" + teacher.getResearch());
        System.out.println("办公室：" + teacher.getOffice());
    }
}
