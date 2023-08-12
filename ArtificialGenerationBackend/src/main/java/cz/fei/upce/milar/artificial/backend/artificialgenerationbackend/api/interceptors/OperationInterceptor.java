package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.interceptors;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.UUID;

public class OperationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OperationInterceptor.class);

    private final TempFileService tempFileService;

    public OperationInterceptor(TempFileService tempFileService) {
        this.tempFileService = tempFileService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        Map map = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String operationIDAsString = (String) map.get("operationId");
        String clientIDAsString = request.getHeader("client-id");
        UUID clientID = UUID.fromString(clientIDAsString);
        UUID operationID = UUID.fromString(operationIDAsString);

        /*try {
            if(!tempFileService.removeFirstTempFile(clientID, operationID)) {
                throw new RuntimeException("File was not deleted");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }*/
        // HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("logging...");
        // HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
