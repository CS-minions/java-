import java.util.Comparator;
import java.util.TreeSet;

public class CountryApp {

    // 创建一个Country类来表示国家
    static class Country implements Comparable<Country> {
        String name;
        double GDP2023;
        int Olympics2024;

        public Country(String name, double GDP2023, int Olympics2024) {
            this.name = name;
            this.GDP2023 = GDP2023;
            this.Olympics2024 = Olympics2024;
        }

        // 输出国家信息
        public String toString() {
            return "Country: " + name + ", GDP2023: " + GDP2023 + " Million USD, Olympics2024 Medals: " + Olympics2024;
        }

        // 使用GDP2023进行排序（降序）
        @Override
        public int compareTo(Country other) {
            // 按照GDP2023从大到小排序
            return Double.compare(other.GDP2023, this.GDP2023);
        }
    }

    // Olympics2024 排序的比较器（降序）
    static class OlympicsComparator implements Comparator<Country> {
        @Override
        public int compare(Country o1, Country o2) {
            // 按照Olympics2024从大到小排序
            return Integer.compare(o2.Olympics2024, o1.Olympics2024);
        }
    }

    // GDP2023 排序的比较器（降序）
    static class GDPComparator implements Comparator<Country> {
        @Override
        public int compare(Country o1, Country o2) {
            // 按照GDP2023从大到小排序
            return Double.compare(o2.GDP2023, o1.GDP2023);
        }
    }

    public static void main(String[] args) {
        // 创建12个国家的实例，使用提供的真实数据
        Country[] countries = {
                new Country("美国", 26704170, 113),
                new Country("中华人民共和国", 18084810, 93),
                new Country("德国", 5072707, 48),
                new Country("日本", 4231143, 58),
                new Country("英国", 3070677, 51),
                new Country("印度", 3730314, 28),
                new Country("法国", 3171209, 40),
                new Country("意大利", 2610766, 38),
                new Country("加拿大", 2184804, 32),
                new Country("韩国", 1968019, 24),
                new Country("以色列", 554173, 9),
                new Country("俄罗斯", 1799000, 50)
        };

        // 第一种方式：通过实现Comparable接口（按GDP2023排序）
        TreeSet<Country> setByGDP = new TreeSet<>();
        for (Country country : countries) {
            setByGDP.add(country);
        }

        System.out.println("排序方式1：按GDP2023从大到小排序输出：");
        for (Country country : setByGDP) {
            System.out.println(country);
        }

        // 第二种方式：通过实现Comparator接口（按Olympics2024排序）
        TreeSet<Country> setByOlympics = new TreeSet<>(new OlympicsComparator());
        for (Country country : countries) {
            setByOlympics.add(country);
        }

        System.out.println("\n排序方式2：按Olympics2024奖牌数量从大到小排序输出：");
        for (Country country : setByOlympics) {
            System.out.println(country);
        }

        // 第三种方式：通过实现Comparator接口（按GDP2023排序）
        TreeSet<Country> setByGDPComparator = new TreeSet<>(new GDPComparator());
        for (Country country : countries) {
            setByGDPComparator.add(country);
        }

        System.out.println("\n排序方式3：按GDP2023从大到小排序输出（使用Comparator）：");
        for (Country country : setByGDPComparator) {
            System.out.println(country);
        }
    }
}
