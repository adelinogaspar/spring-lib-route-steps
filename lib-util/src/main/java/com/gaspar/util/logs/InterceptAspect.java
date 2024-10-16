package com.gaspar.util.logs;

import com.gaspar.util.logs.helper.CorrelationIdHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.gaspar.util.logs.helper.CorrelationIdHolder.generateCorrelationId;
import static com.gaspar.util.logs.helper.CorrelationIdHolder.getCorrelationId;
import static java.util.Objects.nonNull;

@Aspect
@Component
public class InterceptAspect {
    private static final Logger logger = LoggerFactory.getLogger(InterceptAspect.class);
    private Map<String, Map<String, Object>> logsMap = new HashMap<>();

/*    @Before("@within(restController) || @annotation(restMapping)")
    public void interceptRestControllers(RestController restController, RequestMapping restMapping) {
        if (getCorrelationId() == null) {
            generateCorrelationId();
        }
        String correlationId = getCorrelationId();
        logsMap.putIfAbsent(correlationId, new HashMap<>());
        logsMap.get(correlationId).put("controller", new HashMap<>());
    }

    @Around("execution(* org.springframework.kafka.core.KafkaTemplate.send(..))")
    public Object interceptKafkaProducer(ProceedingJoinPoint joinPoint) throws Throwable {
        String correlationId = getCorrelationId();
        Object[] args = joinPoint.getArgs();
        KafkaTemplate<?, ?> kafkaTemplate = (KafkaTemplate<?, ?>) joinPoint.getTarget();
        String topic = (String) args[0];
        Object message = args[1];

        Map<String, Object> kafkaLog = new HashMap<>();
        kafkaLog.put("topic", topic);
        kafkaLog.put("message-content", message.toString());

        logsMap.get(correlationId).put("kafka-producer", kafkaLog);

        try {
            Object result = joinPoint.proceed();
            kafkaLog.put("status", "success");
            return result;
        } catch (Exception ex) {
            kafkaLog.put("error", ex.getMessage());
            throw ex;
        }
    }*/

    @Around("execution(* feign.Client.*(..))")
    public Object interceptFeignClient(ProceedingJoinPoint joinPoint) throws Throwable {
        String correlationId = getCorrelationId();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //FeignClient feignClient = signature.getMethod().getAnnotation(FeignClient.class);

        String url = (String) joinPoint.getArgs()[0];  // Extract the URL from method args
        Map<String, Object> feignLog = new HashMap<>();
        feignLog.put("url", url);
        feignLog.put("method", signature.getMethod().getName());

        logsMap.get(correlationId).put("feign", feignLog);

        try {
            Object result = joinPoint.proceed();
            feignLog.put("status", "success");
            return result;
        } catch (Exception ex) {
            feignLog.put("error", ex.getMessage());
            throw ex;
        }
    }

    @AfterReturning(value = "execution(* org.springframework.web.bind.annotation.*.*(..))", returning = "response")
    public void logRestControllerResponse(Object response) {
        String correlationId = getCorrelationId();
        //@SuppressWarnings("unchecked")
        Map<String, Object> logsController = (Map<String, Object>) logsMap.get(correlationId).get("controller");
        if (nonNull(logsController)) {
            logsController.put("response", response);
        }

        // Log the entire interaction
        logger.info("Final correlation={} Log: {}", correlationId, logsMap.get(correlationId));

        // Clean up
        CorrelationIdHolder.clear();
        logsMap.remove(correlationId);
    }

    // OK funcionando
    @Around("@annotation(com.gaspar.util.logs.annotation.Loggable)")
    public Object loggableMethodAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        String correlationId = getCorrelationId();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Log method execution
        logger.info("Intercepted {} method using Loggable: {}", correlationId, methodName);
        logger.info("Arguments: {}", args);

        try {
            Object result = joinPoint.proceed();
            // Logging the response details
            logger.info("Loggable Response: {}", result.toString());
            return result;
        } catch (Throwable throwable) {
            // Logging the error if any
            logger.error("Error in Loggable method: {}", methodName, throwable);
            throw throwable;
        }
    }

    // OK funcionando
    // Pointcut to intercept all methods within classes annotated with @RestController
    @Around("execution(* *..*.*(..)) && @within(org.springframework.web.bind.annotation.RestController)")
    public Object restControllerAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String correlationId = generateCorrelationId();

        // Logging the request details
        logger.info("Intercepted {} method in RestController: {}", correlationId, methodName);
        logger.info("Arguments: {}", args);

        try {
            // Proceed with the original method execution
            Object result = joinPoint.proceed();

            // Logging the response details
            logger.info("RestController Response: {}", result.toString());
            return result;
        } catch (Throwable throwable) {
            // Logging the error if any
            logger.error("Error in RestController method: {}", methodName, throwable);
            throw throwable;
        }
    }

    // OK funcionando
    // Pointcut to intercept all methods within Feign clients
    @Around("execution(* *..*.*(..)) && @within(org.springframework.cloud.openfeign.FeignClient)")
    public Object feignClientAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        // You can log the method, arguments, etc.
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String correlationId = getCorrelationId();

        logger.info("Intercepted {} Feign Client Method: {}", correlationId, methodName);
        logger.info("Feign Client Args: {}", args);

        try {
            // Proceed with the original Feign client call
            Object result = joinPoint.proceed();

            // Log or handle the result
            logger.info("Feign Client Result: {}", result.toString());
            return result;
        } catch (Throwable throwable) {
            // Log any errors
            logger.error("Error in Feign Client Call: ", throwable);
            throw throwable;
        }
    }
}