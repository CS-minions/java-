public class Tom{
    int leg;
    String head;
    final int MAX = 100;        // 实例常量变量 'MAX'，每个对象都有自己的值
    final static int MIN = 20;  // 静态常量变量 'MIN'，所有实例共享
    void cry(String s){
        System.out.println(s);
    }
}
class Example{
    public static void main (String args[])
    { Tom cat;
        cat=new Tom();
        cat.leg=4;
        cat.head="猫头";
        System.out.println("腿:"+cat.leg+"条");
        System.out.println("头:"+cat.head);
        cat.cry("我今天要和Jerry拼了");
    }
}