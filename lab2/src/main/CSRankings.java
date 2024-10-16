package main;

import java.util.HashMap;
import java.util.Map;

public class CSRankings {
    private Map<String, String[]> rankings;

    // 构造方法，初始化研究方向和对应的会议名称及网址
    public CSRankings() {
        rankings = new HashMap<>();
        rankings.put("Machine Learning & Data Mining", new String[]{
            "会议名称：ICML 网址：dblp.org/db/conf/icml/index.html",
            "会议名称：KDD 网址：dblp.org/db/conf/kdd/index.html",
            "会议名称：NeurIPS 网址：dblp.org/db/conf/nips/index.html"
        });
        rankings.put("Databases", new String[]{
            "会议名称：SIGMOD 网址：dblp.org/db/conf/sigmod/index.html",
            "会议名称：VLDB 网址：dblp.org/db/conf/vldb/index.html",
            "会议名称：ICDE 网址：dblp.org/db/conf/icde/index.html"
        });
        rankings.put("Software Engineering", new String[]{
            "会议名称：ICSE 网址：dblp.org/db/conf/icse/index.html",
            "会议名称：FSE 网址：dblp.org/db/conf/sigsoft/index.html",
            "会议名称：ASE 网址：dblp.org/db/conf/kbse/index.html"
        });
        rankings.put("The Web & Information Retrieval", new String[]{
            "会议名称：WWW 网址：dblp.org/db/conf/www/index.html",
            "会议名称：SIGIR 网址：dblp.org/db/conf/sigir/index.html",
            "会议名称：WSDM 网址：dblp.org/db/conf/wsdm/index.html"
        });
        rankings.put("Computer Graphics", new String[]{
            "会议名称：SIGGRAPH 网址：dblp.org/db/conf/siggraph/index.html",
            "会议名称：Eurographics 网址：dblp.org/db/conf/eurographics/index.html",
            "会议名称：Pacific Graphics 网址：dblp.org/db/conf/pg/index.html"
        });
    }

    // 返回某一研究方向的相关信息
    public String getResearchAreaInfo(String researchArea) {
        String[] conferences = rankings.get(researchArea);
        if (conferences == null) {
            return "No information available for the given research area.";
        }
        StringBuilder result = new StringBuilder();
        for (String conference : conferences) {
            result.append(conference).append("\n");
        }
        return result.toString();
    }

    // 重写toString方法
    @Override
    public String toString() {
        return "CSRankings class provides information about top conferences in various research areas.";
    }

    // 主方法用于测试
    public static void main(String[] args) {
        CSRankings csRankings = new CSRankings();
        System.out.println(csRankings.getResearchAreaInfo("Machine Learning & Data Mining"));
        System.out.println(csRankings.getResearchAreaInfo("Databases"));
        System.out.println(csRankings.getResearchAreaInfo("Software Engineering"));
        System.out.println(csRankings.getResearchAreaInfo("The Web & Information Retrieval"));
        System.out.println(csRankings.getResearchAreaInfo("Computer Graphics"));
    }
}