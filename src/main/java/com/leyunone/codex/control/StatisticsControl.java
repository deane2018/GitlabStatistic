package com.leyunone.codex.control;

import com.leyunone.codex.model.DataResponse;
import com.leyunone.codex.model.query.CodeTimeQuery;
import com.leyunone.codex.model.vo.ChartVO;
import com.leyunone.codex.model.vo.UserVO;
import com.leyunone.codex.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @RequestMapping("/userbasecode")
    public DataResponse statisticsUserBaseCode(CodeTimeQuery query){
        ChartVO chartVO = statisticsService.userBaseCode(query);
        return DataResponse.of(chartVO);
    }

    /**
     * 代码随时间提交统计
     * @param query
     * @return
     */
    @RequestMapping("/userProjectTimeCode")
    public DataResponse userProjectTimeCode(CodeTimeQuery query){
        ChartVO chartVO = statisticsService.userProjectTimeCode(query);
        return DataResponse.of(chartVO);
    }
}
