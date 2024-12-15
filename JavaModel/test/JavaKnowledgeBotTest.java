import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Java知识问答机器人测试类
 * 测试不同类型的Java编程题目
 */
public class JavaKnowledgeBotTest {
    private final JavaKnowledgeBot bot = new JavaKnowledgeBot();

    /**
     * 测试选择题回答
     */
    @Test
    public void testMultipleChoice() {
        // 测试例1：Java基础概念
        String question1 = "Java中，下列哪个不是基本数据类型？\n" +
                "A. int\n" +
                "B. String\n" +
                "C. double\n" +
                "D. boolean";
        String response1 = bot.processQuestion(question1);
        assertTrue(response1.contains("B. String"));
        assertTrue(response1.contains("引用类型"));

        // 测试例2：面向对象概念
        String question2 = "关于Java接口，下列说法正确的是？\n" +
                "A. 接口可以包含普通方法\n" +
                "B. 接口中的变量默认是private的\n" +
                "C. 一个类可以实现多个接口\n" +
                "D. 接口可以继承多个类";
        String response2 = bot.processQuestion(question2);
        assertTrue(response2.contains("C"));
        assertTrue(response2.contains("多实现"));
    }

    /**
     * 测试判断题回答
     */
    @Test
    public void testTrueFalse() {
        // 测试例1：Java语言特性
        String question1 = "判断题：Java是一种解释型语言。";
        String response1 = bot.processQuestion(question1);
        assertTrue(response1.contains("正确"));
        assertTrue(response1.contains("JVM"));

        // 测试例2：面向对象特性
        String question2 = "判断题：Java中，一个类可以同时继承多个父类。";
        String response2 = bot.processQuestion(question2);
        assertTrue(response2.contains("错误"));
        assertTrue(response2.contains("单继承"));
    }

    /**
     * 测试程序填空题回答
     */
    @Test
    public void testCodeCompletion() {
        // 测试例1：基本语法
        String question1 = "填空题：补充下面代码中缺失的部分：\n" +
                "public class Hello {\n" +
                "    public static ___ main(String[] args) {\n" +
                "        System.out.println(\"Hello World\");\n" +
                "    }\n" +
                "}";
        String response1 = bot.processQuestion(question1);
        assertTrue(response1.contains("void"));
        assertTrue(response1.contains("主方法"));

        // 测试例2：异常处理
        String question2 = "填空题：补充try-catch语句中缺失的部分：\n" +
                "try {\n" +
                "    int result = 10 / 0;\n" +
                "} _____ (_____ e) {\n" +
                "    System.out.println(e.getMessage());\n" +
                "}";
        String response2 = bot.processQuestion(question2);
        assertTrue(response2.contains("catch"));
        assertTrue(response2.contains("ArithmeticException"));
    }

    /**
     * 测试编程题回答
     */
    @Test
    public void testProgramming() {
        // 测试例1：简单算法
        String question1 = "编程题：编写一个方法，计算1到n的和。";
        String response1 = bot.processQuestion(question1);
        assertTrue(response1.contains("public static int sum(int n)"));
        assertTrue(response1.contains("return"));

        // 测试例2：数据结构
        String question2 = "编程题：实现一个简单的栈数据结构。";
        String response2 = bot.processQuestion(question2);
        assertTrue(response2.contains("class Stack"));
        assertTrue(response2.contains("push"));
        assertTrue(response2.contains("pop"));
    }

    /**
     * 测试答案解析
     */
    @Test
    public void testExplanation() {
        // 测试选择题解析
        String question = "为什么String不是基本数据类型？";
        String response = bot.processQuestion(question);
        assertTrue(response.contains("引用类型"));
        assertTrue(response.contains("对象"));
        assertTrue(response.contains("String类"));

        // 测试编程题解析
        String question2 = "解释栈的实现原理。";
        String response2 = bot.processQuestion(question2);
        assertTrue(response2.contains("LIFO"));
        assertTrue(response2.contains("后进先出"));
        assertTrue(response2.contains("数据结构"));
    }
} 