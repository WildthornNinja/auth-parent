package com.atguigu.system.aspect;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.helper.JwtHelper;
import com.atguigu.common.util.IpUtil;
import com.atguigu.model.system.SysOperLog;
import com.atguigu.system.annotation.Log;
import com.atguigu.system.service.OperLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 日志切面
 */
@Aspect  //声明当前类是一个切面
@Component //将切面交给IOC管理
public class LogAspect {

    @Autowired
    private OperLogService operLogService;

    /**
     * 返回通知
     * @param joinPoint
     * @param controllerLog
     * @param jsonResult
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        //调用处理日志的方法
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }
    /**
     * 异常通知
     * @param joinPoint
     * @param controllerLog
     * @param e
     */
    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        //调用处理日志的方法
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * 处理日志的方法
     * @param joinPoint
     * @param controllerLog
     * @param e
     * @param jsonResult
     */
    private void handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {
        ////获取HttpServletRequst对象 获取request对象
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();



        //创建SysOperLog对象//创建SysOperLog对象
        SysOperLog sysOperLog = new SysOperLog();
        //设置操作状态
        sysOperLog.setStatus(1);
        if(e!= null){
            sysOperLog.setStatus(0);
            //设置错误信息
            sysOperLog.setErrorMsg("操作失败");
        }
        //获取请求方式
        String requestMethod= request.getMethod();
        //设置请求方式
        sysOperLog.setMethod(requestMethod);
        //获取token中的用户名
        String token = request.getHeader("token");
        String username = JwtHelper.getUsername(token);
        //设置操作人员
        sysOperLog.setOperName(username);
        //获取请求地址
        String requestURI = request.getRequestURI();
        //设置请求地址
        sysOperLog.setOperUrl(requestURI);
        //获取ip地址
        String ipAddress = IpUtil.getIpAddress(request);
        //设置ip地址
        sysOperLog.setOperIp(ipAddress);


        //获取处理器的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取处理器中处理请求的方法名
        String methodName = joinPoint.getSignature().getName();
        //设置方法名
        sysOperLog.setMethod(className+"."+methodName+"()");

        //设置模块标题
        sysOperLog.setTitle(controllerLog.title());
        //设置业务类型
        sysOperLog.setBusinessType(controllerLog.businessType().name());
        //设置操作人员类型
        sysOperLog.setOperatorType(controllerLog.operatorType().name());

        //判断是否需要保存请求参数
        if(controllerLog.isSaveRequestData()){
            //需要保存请求参数，调用设置请求参数的方法
            setRequestData(joinPoint,sysOperLog);
        }
        //判断是否需要保存响应数据
        if(controllerLog.isSaveResponseData()&&!StringUtils.isEmpty(jsonResult)){
            //设置响应数据
            sysOperLog.setJsonResult(JSON.toJSONString(jsonResult));
        }

        //调用OperLogService中保存操作日志的方法
        operLogService.saveSysOperLog(sysOperLog);
    }
    /**
     * 设置请求数据的方法
     * @param joinPoint
     * @param sysOperLog
     */
    private void setRequestData(JoinPoint joinPoint, SysOperLog sysOperLog) {
        String requestMethod = sysOperLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            sysOperLog.setOperParam(params);
        }
    }

    /**
     * 参数拼装的方法
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!StringUtils.isEmpty(o) && !isFilterObject(o)) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
