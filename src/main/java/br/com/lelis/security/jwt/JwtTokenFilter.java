package br.com.lelis.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // Obtém o token do cabeçalho Authorization
        String token = tokenProvider.resolveToken(request);
        System.out.println("Token = "  + token);

        if (token != null && tokenProvider.validateToken(token)) {
            // Se o token for válido, obtém a autenticação e a armazena no contexto do Spring Security
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if (token != null) {
            // Se o token for inválido ou expirado, retorna um erro 401 (não autorizado)
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
            return; // Impede que o filtro continue para as próximas etapas
        }

        // Caso contrário, continua o fluxo normal da requisição
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

