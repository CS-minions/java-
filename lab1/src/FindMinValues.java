import java.util.Arrays;

public class FindMinValues {
    public static void main(String[] args) {
        // 记录程序开始时间
        long startTime = System.currentTimeMillis();

        // 创建1000×1000×100的三维数组
        int x = 1000, y = 1000, z = 100;
        float[][][] array = new float[x][y][z];

        // 使用Math.random()给数组中的每个元素赋值0到1之间的随机数
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    array[i][j][k] = (float) Math.random();
                }
            }
        }

        // 创建一个大小为15的数组，来存储当前找到的最小值
        float[] minValues = new float[15];
        // 将minValues数组初始化为正无穷大
        Arrays.fill(minValues, Float.MAX_VALUE);

        // 遍历三维数组并找到最小的15个数
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    float currentValue = array[i][j][k];

                    // 如果当前值比已记录的最大值小，则将其插入minValues数组
                    if (currentValue < minValues[14]) {
                        // 将currentValue插入到合适位置，保持数组从小到大排序
                        for (int m = 0; m < 15; m++) {
                            if (currentValue < minValues[m]) {
                                // 将较大的值依次后移
                                System.arraycopy(minValues, m, minValues, m + 1, 14 - m);
                                minValues[m] = currentValue; // 插入当前值
                                break;
                            }
                        }
                    }
                }
            }
        }

        // 输出找到的最小的15个数
        System.out.println("最小的15个数：");
        for (float value : minValues) {
            System.out.println(value);
        }

        // 记录程序结束时间
        long endTime = System.currentTimeMillis();

        // 输出程序耗费的时间
        System.out.println("程序运行时间：" + (endTime - startTime) + " 毫秒");
    }
}
