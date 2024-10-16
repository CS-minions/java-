package main;

public class Item {
    // 比赛项目的名称
    private String name;

    // 比赛项目的场地
    private String venue;

    // 比赛项目的日期
    private String date;

    // 构造函数，初始化比赛项目的名称、场地和日期
    public Item(String name, String venue, String date) {
        this.name = name;
        this.venue = venue;
        this.date = date;
    }

    // 获取比赛项目的名称
    public String getName() {
        return name;
    }

    // 设置比赛项目的名称
    public void setName(String name) {
        this.name = name;
    }

    // 获取比赛项目的场地
    public String getVenue() {
        return venue;
    }

    // 设置比赛项目的场地
    public void setVenue(String venue) {
        this.venue = venue;
    }

    // 获取比赛项目的日期
    public String getDate() {
        return date;
    }

    // 设置比赛项目的日期
    public void setDate(String date) {
        this.date = date;
    }

    // 开始比赛项目，打印比赛项目的详细信息
    public void startEvent() {
        System.out.println("The event " + name + " is starting at " + venue + " on " + date);
    }
}