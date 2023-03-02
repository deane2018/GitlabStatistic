package com.leyunone.codex.control;

import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.ChartVO;
import com.leyunone.codex.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 统计接口
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsControl {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 人员基本信息统计
     * @return
     */
    @GetMapping("/userbasecode")
    public DataResponse statisticsUserBaseCode(CodeTimeQuery query){
        ChartVO chartVO = statisticsService.userBaseCode(query);
        return DataResponse.of(chartVO);
    }

    /**
     * 小组基本信息统计
     * @param query
     * @return
     */
    @GetMapping("/groupbasecode")
    public DataResponse statisticsGroupBaseCode(CodeTimeQuery query){
        ChartVO chartVO = statisticsService.groupBaseCode(query);
        return DataResponse.of(chartVO);
    }

    /**
     * 代码随时间提交统计
     * @param query
     * @return
     */
    @GetMapping("/userProjectTimeCode")
    public DataResponse userProjectTimeCode(CodeTimeQuery query){
        ChartVO chartVO = statisticsService.userProjectTimeCode(query);
        return DataResponse.of(chartVO);
    }

    /**
     * 小组随时间提交统计
     * @param query
     * @return
     */
    @GetMapping("/groupTimeCode")
    public DataResponse groupTimeCode(CodeTimeQuery query){
        ChartVO chartVO = statisticsService.groupTimeCode(query);
        return DataResponse.of(chartVO);
    }
}
