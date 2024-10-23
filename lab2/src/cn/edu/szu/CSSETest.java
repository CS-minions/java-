package cn.edu.szu;

import java.util.List;
import java.util.ArrayList;

public class CSSETest {
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
