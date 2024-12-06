import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ProductSimilarityMultiThread {
    // 用于存储商品与用户集合映射的map
    private static Map<String, Set<String>> productToUsers = new HashMap<>();
    private static final int MAX_LINES = 100; // 限制最大行数，可根据需要调整

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // 打印当前工作目录，以方便确认文件相对路径问题
        System.out.println("Current working dir: " + new File(".").getAbsolutePath());

        String inputFile = "reviews.txt"; // 请根据实际文件位置修改
        String outputFile = "productNeighborhood.txt";

        // 1. 读入review.txt文件，构建productToUsers映射（限制数据量）
        buildProductToUsers(inputFile);

        List<String> products = new ArrayList<>(productToUsers.keySet());
        Collections.sort(products);

        System.out.println("Number of products: " + products.size());

        // 2. 使用多线程计算两两商品的相似度
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<List<SimilarityResult>>> futures = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            for (int j = i + 1; j < products.size(); j++) {
                String p1 = products.get(i);
                String p2 = products.get(j);
                futures.add(executor.submit(new ComputeTask(p1, p2)));
            }
        }

        Map<String, List<SimilarityResult>> productSimilarities = new HashMap<>();
        for (String p : products) {
            productSimilarities.put(p, new ArrayList<>());
        }

        for (Future<List<SimilarityResult>> f : futures) {
            List<SimilarityResult> resultList = f.get();
            for (SimilarityResult r : resultList) {
                productSimilarities.get(r.product1).add(new SimilarityResult(r.product1, r.product2, r.similarity));
                productSimilarities.get(r.product2).add(new SimilarityResult(r.product2, r.product1, r.similarity));
            }
        }

        executor.shutdown();

        // 3. 对每个商品的相似商品列表进行排序并取前3个
        for (String p : productSimilarities.keySet()) {
            List<SimilarityResult> list = productSimilarities.get(p);
            Collections.sort(list, new Comparator<SimilarityResult>() {
                @Override
                public int compare(SimilarityResult o1, SimilarityResult o2) {
                    int cmp = Double.compare(o2.similarity, o1.similarity);
                    if (cmp != 0) {
                        return cmp;
                    } else {
                        return o1.product2.compareTo(o2.product2);
                    }
                }
            });
            // 排序完成后需要从大到小，当前比较器是o2对o1，从而结果是升序，需要反转
            Collections.reverse(list);
        }

        // 4. 写出结果文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String p : products) {
                List<SimilarityResult> list = productSimilarities.get(p);
                // 取前3个
                List<String> top3 = new ArrayList<>();
                for (int i = 0; i < Math.min(3, list.size()); i++) {
                    top3.add(list.get(i).product2);
                }
                // 不足3个则填空字符串
                while (top3.size() < 3) {
                    top3.add("");
                }
                writer.write(p + ";" + top3.get(0) + ";" + top3.get(1) + ";" + top3.get(2));
                writer.newLine();
            }
        }

        System.out.println("Done. Results written to " + outputFile);
    }

    private static void buildProductToUsers(String inputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null && count < MAX_LINES) {
                String[] parts = line.split(";");
                if (parts.length != 2) continue;
                String user = parts[0].trim();
                String product = parts[1].trim();
                productToUsers.computeIfAbsent(product, k -> new HashSet<>()).add(user);
                count++;
            }
        }
    }

    // 多线程任务：计算两个商品的相似度
    static class ComputeTask implements Callable<List<SimilarityResult>> {
        private String p1;
        private String p2;

        ComputeTask(String p1, String p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public List<SimilarityResult> call() {
            Set<String> users1 = productToUsers.get(p1);
            Set<String> users2 = productToUsers.get(p2);

            if (users1 == null || users2 == null) {
                return Collections.singletonList(new SimilarityResult(p1, p2, 0.0));
            }

            // 计算Jaccard相似度
            int intersectionSize = 0;
            for (String u : users1) {
                if (users2.contains(u)) {
                    intersectionSize++;
                }
            }
            int unionSize = users1.size() + users2.size() - intersectionSize;
            double similarity = unionSize == 0 ? 0.0 : ((double) intersectionSize / unionSize);

            return Collections.singletonList(new SimilarityResult(p1, p2, similarity));
        }
    }

    // 用于存储相似度结果的类
    static class SimilarityResult {
        String product1;
        String product2;
        double similarity;

        SimilarityResult(String p1, String p2, double sim) {
            this.product1 = p1;
            this.product2 = p2;
            this.similarity = sim;
        }
    }
}