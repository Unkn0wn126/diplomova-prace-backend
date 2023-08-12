package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.filters;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Component
public class UserFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(UserFilter.class);

    private final ActiveClientsService activeClientsService;

    public UserFilter(ActiveClientsService activeClientsService) {
        this.activeClientsService = activeClientsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String userID = request.getHeader("client-id");
            if (userID == null) {
                throw new AccessDeniedException("No client id present");
            }
            UUID uuid = UUID.fromString(userID);

            if (!activeClientsService.isClientRegistered(uuid)) {
                throw new AccessDeniedException("No such client");
            }

            logger.info(userID);
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException | RuntimeException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }
}
