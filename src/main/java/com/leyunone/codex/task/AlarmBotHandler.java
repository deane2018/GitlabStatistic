package com.leyunone.codex.task;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.codex.rule.enter.AlarmRuleEnterService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/3/8 11:00
 */
@Component
public class AlarmBotHandler extends IJobHandler {

    private final AlarmRuleEnterService alarmRuleEnterService;

    public AlarmBotHandler(AlarmRuleEnterService alarmRuleEnterService) {
        this.alarmRuleEnterService = alarmRuleEnterService;
    }

    /**
     * 告警机器人
     */
    @XxlJob(value = "alarm_bot")
    @Override
    public void execute() {

        String jobParam = XxlJobHelper.getJobParam();
        if (StringUtils.isBlank(jobParam)) return;
        try {
            alarmRuleEnterService.run(jobParam);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
