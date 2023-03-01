package com.leyunone.codex.model.vo;

import com.leyunone.codex.model.ChartBean;
import lombok.Data;

import java.util.List;

@Data
public class ChartVO {

    /**
     * 标题
     */
    private String title;

    /**
     * 横坐标
     */
    private List<String> xChart;

    /**
     * 数据
     */
    private List<ChartBean> series;

    private Object [] [] seriesData;
}
