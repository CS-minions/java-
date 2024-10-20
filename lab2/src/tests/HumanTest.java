package tests;

import static org.junit.Assert.*;

import main.Chinese;
import main.Human;
import main.Italian;
import main.Spaniard;
import org.junit.Test;

public class HelloTest {

    @Test
    public void testSayHello() {
        // 创建不同国家的人
        Human chinese = new Chinese("韩令帅");
        Human spaniard = new Spaniard("fskhf");
        Human italian = new Italian("jhjhjh");

        // 使用 ByteArrayOutputStream 捕获输出
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // 测试各个 sayHello 方法的输出
        chinese.sayHello();
        spaniard.sayHello();
        italian.sayHello();

        // 获取输出结果
        String output = outContent.toString().trim();

        // 验证输出
        String expectedOutput = "你好\nHola\nCiao";
        assertEquals(expectedOutput, output);
    }
}
