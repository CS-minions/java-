
public class Example4_3
{
    public static void main(String args[])
    {
        System.out.println(Tom.MIN);   // 使用类名 'Tom' 输出静态常量 'MIN'，输出 20
        //System.out.println(Tom.MAX); // 错误：不能通过类直接访问实例变量 'MAX'
        Tom cat = new Tom();           // 创建一个名为 'cat' 的 'Tom' 类的实例对象
        System.out.println(cat.MAX);   // 通过对象 'cat' 输出实例常量 'MAX'，输出 100
    }
}
