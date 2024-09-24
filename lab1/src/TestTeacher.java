// 定义测试类
public class TestTeacher {
    public static void main(String[] args) {
        // 创建 Teacher 类的对象
        Teacher teacher = new Teacher();

        // 设置教师的信息
        teacher.setName("王华");
        teacher.setTitle("副教授");
        teacher.setCourse("软件工程");
        teacher.setResearch("大数据");
        teacher.setOffice("实验楼 202 室");

        // 获取并输出教师的信息
        System.out.println("姓名：" + teacher.getName());
        System.out.println("职位：" + teacher.getTitle());
        System.out.println("主讲课程：" + teacher.getCourse());
        System.out.println("研究方向：" + teacher.getResearch());
        System.out.println("办公室：" + teacher.getOffice());
    }
}
