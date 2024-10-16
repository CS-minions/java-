package main;


// 定义运动员类
public class Athlete {
    private String name;
    private String gender;
    private int age;
    private String item;
    private int medal;

    // 构造函数，初始化运动员的姓名、性别、年龄、比赛项目和奖牌数量
    public Athlete(String name, String gender, int age, String item, int medal) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.item = item;
        this.medal = medal;
    }

    // 获取运动员的姓名
    public String getName() {
        return name;
    }

    // 设置运动员的姓名
    public void setName(String name) {
        this.name = name;
    }

    // 获取运动员的性别
    public String getGender() {
        return gender;
    }

    // 设置运动员的性别
    public void setGender(String gender) {
        this.gender = gender;
    }

    // 获取运动员的年龄
    public int getAge() {
        return age;
    }

    // 设置运动员的年龄
    public void setAge(int age) {
        this.age = age;
    }

    // 获取运动员最擅长的比赛项目
    public String getItem() {
        return item;
    }

    // 设置运动员最擅长的比赛项目
    public void setItem(String item) {
        this.item = item;
    }

    // 获取运动员在2024巴黎奥运会获得的奖牌数量
    public int getMedal() {
        return medal;
    }

    // 设置运动员在2024巴黎奥运会获得的奖牌数量
    public void setMedal(int medal) {
        this.medal = medal;
    }

    // 重写Object类的toString()方法，输出运动员的详细信息
    @Override
    public String toString() {
        return "Athlete{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", item='" + item + '\'' +
                ", medal=" + medal +
                '}';
    }
}
