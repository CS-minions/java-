package tests;

import main.Athlete;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AthleteTest {

    @Test
    public void testAthleteDetails() {
        // 曹缘，中国男子跳水运动员，赢得了2024年巴黎奥运会的金牌
        Athlete athlete = new Athlete("Cao Yuan", "Male", 29, "Diving", 1);

        // 验证姓名
        assertEquals("Cao Yuan", athlete.getName());

        // 验证性别
        assertEquals("Male", athlete.getGender());

        // 验证年龄
        assertEquals(29, athlete.getAge());

        // 验证擅长的项目
        assertEquals("Diving", athlete.getItem());

        // 验证奖牌数量
        assertEquals(1, athlete.getMedal());
    }
}
