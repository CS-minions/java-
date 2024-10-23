package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WordFrequencyCounter {
    public static void main(String[] args) {
        // 网页内容作为字符串读取
        String content = "The Computer Science and Technology discipline at Shenzhen University was founded with support from Tsinghua University in 1983 when the Shenzhen University was just established. College of Computer Science and Software Engineering was formally established in 2008, which was headed by Guoliang Chen, the Academician of the Chinese Academy of Sciences, as its founding Dean. Over the years' development, CSSE has now led by Professor Hui Huang and become a prominent college in education and research nationally and globally.\n" +
                "The platforms include two national-level research platforms (National Engineering Laboratory for the Big Data System Computing Technology, MOE-GD Collaborative Innovation Center for GD-HK Modern Information Service), three national-level teaching platforms (Computer Experimental Teaching Center, Virtual Simulation Experimental Center for Network Engineering, and Tencent Cloud AI College), the Guangdong Laboratory of Artificial Intelligence and Digital Economy (Shenzhen), ten other provincial-level and ten municipal-level platforms."; // 请将此处替换为实际网页文本内容

        // 将字符串转为小写，并分割为单词数组
        String[] words = content.toLowerCase().split("[^a-zA-Z]+");

        // 使用HashMap统计单词出现次数
        Map<String, Integer> wordCountMap = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }

        // 将HashMap转为List以便排序
        List<Map.Entry<String, Integer>> wordCountList = new ArrayList<>(wordCountMap.entrySet());

        // 按出现次数从大到小排序，若次数相同则按字母顺序排序
        Collections.sort(wordCountList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                int countComparison = entry2.getValue().compareTo(entry1.getValue());
                if (countComparison == 0) {
                    return entry1.getKey().compareTo(entry2.getKey());
                }
                return countComparison;
            }
        });

        // 输出出现次数最多的10个单词
        System.out.println("出现次数最多的10个英文单词：");
        for (int i = 0; i < Math.min(10, wordCountList.size()); i++) {
            Map.Entry<String, Integer> entry = wordCountList.get(i);
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
