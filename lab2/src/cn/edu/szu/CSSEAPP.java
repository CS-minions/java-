package cn.edu.szu;

import java.util.ArrayList;
import java.util.List;

// 研究所/中心类
class Institute {
    private String name;
    private String head;

    public Institute(String name, String head) {
        this.name = name;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public String getHead() {
        return head;
    }
}

// 教学系类
class Department {
    private String name;
    private String dean;
    private List<String> viceDeans;
    private String major;
    private List<String> specialClasses;

    public Department(String name, String dean, List<String> viceDeans, String major, List<String> specialClasses) {
        this.name = name;
        this.dean = dean;
        this.viceDeans = viceDeans;
        this.major = major;
        this.specialClasses = specialClasses;
    }

    public String getName() {
        return name;
    }

    public String getDean() {
        return dean;
    }

    public List<String> getViceDeans() {
        return viceDeans;
    }

    public String getMajor() {
        return major;
    }

    public List<String> getSpecialClasses() {
        return specialClasses;
    }
}

// 行政办公室类
class AdministrativeOffice {
    private String name;

    public AdministrativeOffice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// 实验中心类
class LabCenter {
    private String name;

    public LabCenter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// 期刊编辑部类
class JournalEditorialOffice {
    private String name;

    public JournalEditorialOffice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// 计算机与软件学院类
class CSSE {
    private List<Institute> institutes = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();
    private List<AdministrativeOffice> administrativeOffices = new ArrayList<>();
    private List<LabCenter> labCenters = new ArrayList<>();
    private List<JournalEditorialOffice> journalEditorialOffices = new ArrayList<>();

    public CSSE() {
        institutes = new ArrayList<>();
        departments = new ArrayList<>();
        administrativeOffices = new ArrayList<>();
        labCenters = new ArrayList<>();
        journalEditorialOffices = new ArrayList<>();
    }

    // 添加研究所/中心
    public void addInstitute(Institute institute) {
        institutes.add(institute);
    }

    // 添加教学系
    public void addDepartment(Department department) {
        departments.add(department);
    }

    // 添加行政办公室
    public void addAdministrativeOffice(AdministrativeOffice office) {
        administrativeOffices.add(office);
    }

    // 添加实验中心
    public void addLabCenter(LabCenter center) {
        labCenters.add(center);
    }

    // 添加期刊编辑部
    public void addJournalEditorialOffice(JournalEditorialOffice office) {
        journalEditorialOffices.add(office);
    }

    // 获取所有研究所/中心的名字及负责人
    public void getInstituteNames() {
        System.out.println("研究所/中心的名字及负责人：");
        for (Institute institute : institutes) {
            System.out.println(institute.getName() + " - 负责人: " + institute.getHead());
        }
    }

    // 获取所有教学系的名字及系主任、副系主任、专业和特色班
    public void getDepartmentNames() {
        System.out.println("教学系的名字及系主任：");
        for (Department department : departments) {
            System.out.println(department.getName() + " - 系主任: " + department.getDean());
            System.out.println("副系主任: " + String.join("、", department.getViceDeans()));
            System.out.println("专业: " + department.getMajor());
            System.out.println("特色班: " + String.join("、", department.getSpecialClasses()));
            System.out.println();
        }
    }

    // 获取所有行政办公室的名字
    public void getAdministrativeOfficeNames() {
        System.out.println("行政办公室的名字：");
        for (AdministrativeOffice office : administrativeOffices) {
            System.out.println(office.getName());
        }
    }

    // 获取所有实验中心的名字
    public void getLabCenterNames() {
        System.out.println("实验中心的名字：");
        for (LabCenter center : labCenters) {
            System.out.println(center.getName());
        }
    }

    // 获取所有期刊编辑部的名字
    public void getJournalEditorialOfficeNames() {
        System.out.println("期刊编辑部的名字：");
        for (JournalEditorialOffice office : journalEditorialOffices) {
            System.out.println(office.getName());
        }
    }

    // 获取研究所/中心的数量
    public int getInstituteNumber() {
        return institutes.size();
    }

    // 获取教学系的数量
    public int getDepartmentNumber() {
        return departments.size();
    }

    // 获取行政办公室的数量
    public int getAdministrativeOfficeNumber() {
        return administrativeOffices.size();
    }

    // 获取实验中心的数量
    public int getLabCenterNumber() {
        return labCenters.size();
    }

    // 获取期刊编辑部的数量
    public int getJournalEditorialOfficeNumber() {
        return journalEditorialOffices.size();
    }
}

public class CSSEAPP {
    public static void main(String[] args) {
        CSSE csse = new CSSE();

        // 添加研究所/中心
        csse.addInstitute(new Institute("高性能计算研究所", "陈国良"));
        csse.addInstitute(new Institute("大数据技术与应用研究所", "黄哲学"));
        csse.addInstitute(new Institute("未来媒体技术与计算研究所", "江健民"));
        csse.addInstitute(new Institute("网络与信息安全研究所", "李坚强"));
        csse.addInstitute(new Institute("计算机视觉研究所", "沈琳琳"));
        csse.addInstitute(new Institute("可视计算研究中心", "黄惠"));
        csse.addInstitute(new Institute("智能服务计算研究中心", "张良杰"));
        csse.addInstitute(new Institute("智能技术与系统集成研究所", "朱安民"));
        csse.addInstitute(new Institute("软件工程研究中心", "明仲"));

        // 添加教学系
        List<String> csViceDeans = List.of("邹永攀", "高灿", "刘刚", "赵阳");
        List<String> csSpecialClasses = List.of("高性能计算特色班", "国际接轨班");
        csse.addDepartment(new Department("计算机科学与技术系", "潘微科", csViceDeans, "计算机科学与技术", csSpecialClasses));

        List<String> seViceDeans = List.of("张昊迪", "车越岭", "徐鹏飞", "刘涵");
        List<String> seSpecialClasses = new ArrayList<>();
        csse.addDepartment(new Department("软件工程系", "张良杰", seViceDeans, "软件工程", seSpecialClasses));

        List<String> aiViceDeans = List.of("吴惠思", "冯禹洪", "王旭", "戴涛");
        List<String> aiSpecialClasses = List.of("腾班", "卓越班");
        csse.addDepartment(new Department("人工智能系", "王熙照", aiViceDeans, "人工智能", aiSpecialClasses));

        // 添加行政办公室
        csse.addAdministrativeOffice(new AdministrativeOffice("综合办公室"));
        csse.addAdministrativeOffice(new AdministrativeOffice("人事办公室"));

        // 添加实验中心
        csse.addLabCenter(new LabCenter("计算机实验中心"));
        csse.addLabCenter(new LabCenter("软件工程实验中心"));

        // 添加期刊编辑部
        csse.addJournalEditorialOffice(new JournalEditorialOffice("计算机科学期刊编辑部"));

        // 输出所有研究所/中心的名字及负责人
        csse.getInstituteNames();
        System.out.println();

        // 输出所有教学系的名字及系主任、副系主任、专业和特色班
        csse.getDepartmentNames();
        System.out.println();

        // 输出所有行政办公室的名字
        csse.getAdministrativeOfficeNames();
        System.out.println();

        // 输出所有实验中心的名字
        csse.getLabCenterNames();
        System.out.println();

        // 输出所有期刊编辑部的名字
        csse.getJournalEditorialOfficeNames();
        System.out.println();

        // 输出研究所/中心的数量
        System.out.println("研究所/中心的数量: " + csse.getInstituteNumber());

        // 输出教学系的数量
        System.out.println("教学系的数量: " + csse.getDepartmentNumber());

        // 输出行政办公室的数量
        System.out.println("行政办公室的数量: " + csse.getAdministrativeOfficeNumber());

        // 输出实验中心的数量
        System.out.println("实验中心的数量: " + csse.getLabCenterNumber());

        // 输出期刊编辑部的数量
        System.out.println("期刊编辑部的数量: " + csse.getJournalEditorialOfficeNumber());
    }
}
