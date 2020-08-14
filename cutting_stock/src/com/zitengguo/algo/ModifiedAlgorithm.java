package com.zitengguo.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 采用dfs改进原先逻辑
 **/
public class ModifiedAlgorithm {

    public double batchWidth;           // 母卷宽度
    public double[] orderWidth;         // 订单宽度
    public int orderNumber;             // 订单数量
    public int maxPort;                 // 最大通道数
    public double utilLimit;            // 成品率下限
    public int[] orderMaxPort;          // 订单通道最大占用数量
    public int totalPort;               // 总通道占用数量
    public double totalWidth;           // 总切割宽度
    public int[] visited;               // 订单通道组合
    public List<Solution> solutions;    // 可行方案

    public ModifiedAlgorithm(double batchWidth, double[] orderWidth, int maxPort, double utilLimit) {
        this.batchWidth = batchWidth;
        this.orderWidth = orderWidth;
        this.maxPort = maxPort;
        this.utilLimit = utilLimit;
        this.solutions = new ArrayList<>();
        this.orderNumber = orderWidth.length;
        this.visited = new int[orderNumber];
        this.orderMaxPort = new int[orderNumber];
        for (int i = 0; i < orderNumber; i++) {
            this.orderMaxPort[i] = (int) (batchWidth / orderWidth[i]);
        }
    }

    /**
     * 计算总切割宽度和总占用通道数
     *
     * @param x 订单编号
     **/
    private void sumFunc(int x) {
        totalWidth = 0;    // 重置总切割宽度
        totalPort = 0;     // 重置总占用通道数
        for (int i = 0; i <= x; i++) {
            totalWidth += (visited[i] * orderWidth[i]);
            totalPort += visited[i];
        }
    }

    /**
     * 保存可行方案
     **/
    private void saveScheme() {
        if (totalWidth / batchWidth >= utilLimit) {
            Solution solution = new Solution();
            solution.scheme = new int[orderNumber];
            System.arraycopy(visited, 0, solution.scheme, 0, orderNumber);
            solution.utilization = totalWidth / batchWidth;
            solutions.add(solution);
        }
    }

    /**
     * 深度优先搜索
     *
     * @param x 订单编号
     **/
    public void modified(int x) {
        // 递归终止条件
        if (x >= orderWidth.length) {
            saveScheme();
            return;
        }
        for (int i = 0; i <= orderMaxPort[x]; i++) {
            visited[x] = i;
            sumFunc(x);
            if (totalWidth <= batchWidth && totalPort <= maxPort) {
                modified(x + 1);
            }
        }
    }

}
