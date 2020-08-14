package com.zitengguo.test;

import com.zitengguo.algo.ModifiedAlgorithm;
import com.zitengguo.algo.OriginalAlgorithm;
import com.zitengguo.algo.Solution;

import java.util.List;

import static java.util.Arrays.sort;


/**
 * 测试
 **/
public class Test {

    /**
     * 非支配排序找出前沿方案集
     **/
    public static void nondominatedSort(List<Solution> solutions) {
        int n = solutions.size() - 1;
        int i = 0, j;
        int orderNum1, orderNum2;

        while (i < n) {
            j = i + 1;
            // 获取方案i实际分配的订单个数
            orderNum1 = calculateOrderNum(solutions.get(i).scheme);
            while (j <= n) {
                // 获取方案j实际分配的订单个数
                orderNum2 = calculateOrderNum(solutions.get(j).scheme);
                // 当方案至少有一个目标优于另一方案且没有目标劣于另一个方案时，前者支配后者
                // 两个目标：最大化实际分配订单数、最大化母料利用率
                if (orderNum1 > orderNum2) {
                    if (solutions.get(i).utilization >= solutions.get(j).utilization) {
                        solutions.remove(j);
                        n--;
                        j--;
                    }
                } else if (orderNum1 == orderNum2) {
                    if (solutions.get(i).utilization < solutions.get(j).utilization) {
                        solutions.remove(i);
                        n--;
                        i--;
                        break;
                    } else if (solutions.get(i).utilization > solutions.get(j).utilization) {
                        solutions.remove(j);
                        n--;
                        j--;
                    }
                } else {
                    if (solutions.get(i).utilization <= solutions.get(j).utilization) {
                        solutions.remove(i);
                        i--;
                        n--;
                        break;
                    }
                }
                j++;
            }
            i++;
        }
    }

    /**
     * 获取实际分配的订单个数
     **/
    public static int calculateOrderNum(int[] orders) {
        int counter = 0;
        for (int order : orders) {
            if (order > 0) {
                counter++;
            }
        }
        return counter;
    }

    public static void main(String[] args) {

        // 开始计时
        long beginTime = System.nanoTime();

        /**
         * 实际案例
         * 母料宽度：1000
         * 订单宽度：83, 92, 64, 93, 91, 75, 63
         * 最大通道数：13
         * 母料利用率下限：97.5%
         * 效果：
         * 1. 0.03s -> 1.6s  优化98%(在排产系统中会存在差异)
         * 2. 非支配排序后，方案由6049降低至11
         **/
        double[] orderWidth = {83, 92, 64, 93, 91, 75, 63};
        // 必须先根据订单宽度排序，否则算法可能不会发挥作用
        sort(orderWidth);

        // 改进算法
        ModifiedAlgorithm algo1 = new ModifiedAlgorithm(1000, orderWidth, 13, 0.975);
        algo1.modified(0);
        List<Solution> recommended = algo1.solutions;
        nondominatedSort(recommended);

        /*
        // 原有算法
        OriginalAlgorithm algo2 = new OriginalAlgorithm(1000, orderWidth, 13, 0.975);
        algo2.original(0);
        List<Solution> recommended = algo2.solutions;
        nondominatedSort(recommended);
        */

        // 结束计时
        long endTime = System.nanoTime();
        double totalTime = (endTime - beginTime) / (1e9);
        System.out.println("Total run time: " + totalTime + "s");
    }
}
