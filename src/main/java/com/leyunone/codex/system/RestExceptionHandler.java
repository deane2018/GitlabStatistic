package com.leyunone.codex.system;

import com.leyunone.codex.model.DataResponse;
import com.xxl.job.core.context.XxlJobHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
public class RestExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public DataResponse handleValidationException(Exception e, HttpServletResponse response){
        logger.error(e.getMessage());
        e.printStackTrace();
        try {
            XxlJobHelper.handleFail(e.getMessage());
        }catch (Exception ignored){

        }
        return DataResponse.buildFailure(e.getMessage());
    }

}
