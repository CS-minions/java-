import java.util.*;

public class SparseMatrix {

    // 定义稀疏矩阵，使用Map表示
    private Map<Integer, Map<Integer, Float>> matrix;

    // 构造函数初始化矩阵
    public SparseMatrix() {
        matrix = new HashMap<>();
    }

    // 向矩阵中添加一个元素
    public void addElement(int row, int col, float value) {
        if (value != 0) {
            matrix.putIfAbsent(row, new HashMap<>());
            matrix.get(row).put(col, value);
        }
    }

    // 输出矩阵的稀疏格式（三元组形式）
    public void printMatrix() {
        for (Map.Entry<Integer, Map<Integer, Float>> rowEntry : matrix.entrySet()) {
            int row = rowEntry.getKey();
            for (Map.Entry<Integer, Float> colEntry : rowEntry.getValue().entrySet()) {
                int col = colEntry.getKey();
                float value = colEntry.getValue();
                System.out.println(row + " " + col + " " + value);
            }
        }
    }

    // 矩阵加法
    public SparseMatrix add(SparseMatrix other) {
        SparseMatrix result = new SparseMatrix();

        // 复制当前矩阵中的元素
        for (Map.Entry<Integer, Map<Integer, Float>> rowEntry : this.matrix.entrySet()) {
            int row = rowEntry.getKey();
            for (Map.Entry<Integer, Float> colEntry : rowEntry.getValue().entrySet()) {
                int col = colEntry.getKey();
                float value = colEntry.getValue();
                result.addElement(row, col, value);
            }
        }

        // 添加另一个矩阵中的元素
        for (Map.Entry<Integer, Map<Integer, Float>> rowEntry : other.matrix.entrySet()) {
            int row = rowEntry.getKey();
            for (Map.Entry<Integer, Float> colEntry : rowEntry.getValue().entrySet()) {
                int col = colEntry.getKey();
                float value = colEntry.getValue();
                result.addElement(row, col, result.matrix.get(row).getOrDefault(col, 0f) + value);
            }
        }

        return result;
    }

    // 矩阵乘法
    public SparseMatrix multiply(SparseMatrix other) {
        SparseMatrix result = new SparseMatrix();

        // 遍历当前矩阵的行
        for (Map.Entry<Integer, Map<Integer, Float>> rowEntry : this.matrix.entrySet()) {
            int row = rowEntry.getKey();
            // 遍历另一个矩阵的列
            for (Map.Entry<Integer, Map<Integer, Float>> otherRowEntry : other.matrix.entrySet()) {
                int col = otherRowEntry.getKey();
                float sum = 0;

                // 对于每一行，计算与另一矩阵列的点积
                for (Map.Entry<Integer, Float> colEntry : rowEntry.getValue().entrySet()) {
                    int currentCol = colEntry.getKey();
                    float value = colEntry.getValue();
                    if (otherRowEntry.getValue().containsKey(currentCol)) {
                        sum += value * otherRowEntry.getValue().get(currentCol);
                    }
                }

                if (sum != 0) {
                    result.addElement(row, col, sum);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // 示例矩阵1
        SparseMatrix matrix1 = new SparseMatrix();
        matrix1.addElement(1, 1, 1.2f);
        matrix1.addElement(2, 3, 3.1f);
        matrix1.addElement(3, 4, 2.2f);

        // 示例矩阵2
        SparseMatrix matrix2 = new SparseMatrix();
        matrix2.addElement(1, 2, 1.1f);
        matrix2.addElement(1, 3, 1.5f);
        matrix2.addElement(2, 3, 3.2f);
        matrix2.addElement(3, 2, 1.3f);
        matrix2.addElement(3, 5, -3.2f);
        matrix2.addElement(4, 1, -1.0f);
        matrix2.addElement(4, 2, 6.2f);

        // 矩阵乘法
        SparseMatrix product = matrix1.multiply(matrix2);
        System.out.println("Matrix multiplication result:");
        product.printMatrix();

        // 示例矩阵3
        SparseMatrix matrix3 = new SparseMatrix();
        matrix3.addElement(1, 1, 1.2f);
        matrix3.addElement(2, 3, 3.1f);
        matrix3.addElement(3, 4, 2.2f);

        // 示例矩阵4
        SparseMatrix matrix4 = new SparseMatrix();
        matrix4.addElement(1, 1, 1.2f);
        matrix4.addElement(1, 2, -3f);
        matrix4.addElement(1, 3, -5.3f);
        matrix4.addElement(2, 2, 1f);
        matrix4.addElement(2, 3, 0.1f);
        matrix4.addElement(2, 4, -0.4f);
        matrix4.addElement(3, 1, 2f);
        matrix4.addElement(3, 2, 2f);
        matrix4.addElement(3, 3, 1f);
        matrix4.addElement(3, 4, 0.2f);

        // 矩阵加法
        SparseMatrix sum = matrix3.add(matrix4);
        System.out.println("Matrix addition result:");
        sum.printMatrix();
    }
}
