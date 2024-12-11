import java.io.*;
import java.util.*;

public class AmazonProductSimilarityAnalyzer {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Watches.txt"))) {
            // 使用TreeSet来自动排序，实现Comparable接口来定义排序规则
            Set<Review> reviews = new TreeSet<>();
            
            String line;
            String currentUserId = null;
            String currentProductId = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    // 当遇到空行时，如果已收集到必要信息则添加到集合
                    if (currentUserId != null && currentProductId != null 
                            && !currentUserId.equals("unknown")) {
                        reviews.add(new Review(currentUserId, currentProductId));
                    }
                    // 重置当前记录
                    currentUserId = null;
                    currentProductId = null;
                    continue;
                }
                
                String[] parts = line.split(": ", 2);
                if (parts.length != 2) continue;
                
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                switch (key) {
                    case "review/userId":
                        currentUserId = value;
                        break;
                    case "product/productId":
                        currentProductId = value;
                        break;
                }
            }
            
            // 处理最后一条记录
            if (currentUserId != null && currentProductId != null 
                    && !currentUserId.equals("unknown")) {
                reviews.add(new Review(currentUserId, currentProductId));
            }
            
            // 写入排序后的记录到文件
            try (PrintWriter reviewWriter = new PrintWriter("reviews.txt")) {
                for (Review review : reviews) {
                    reviewWriter.println(review.userId + ";" + review.productId);
                }
            }
            
            System.out.println("文件处理完成！");
            System.out.println("已生成 reviews.txt 文件");
            System.out.println("总记录数: " + reviews.size());
            
        } catch (IOException e) {
            System.err.println("处理文件时发生错误：");
            e.printStackTrace();
        }
    }
    
    // 内部类用于存储评论信息并实现排序
    private static class Review implements Comparable<Review> {
        String userId;
        String productId;
        
        Review(String userId, String productId) {
            this.userId = userId;
            this.productId = productId;
        }
        
        @Override
        public int compareTo(Review other) {
            // 首先按userId排序
            int userCompare = this.userId.compareTo(other.userId);
            if (userCompare != 0) {
                return userCompare;
            }
            // userId相同时按productId排序
            return this.productId.compareTo(other.productId);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Review review = (Review) o;
            return Objects.equals(userId, review.userId) && 
                   Objects.equals(productId, review.productId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(userId, productId);
        }
    }
} 