package org.pablos.frontendservice.service;

import jakarta.servlet.http.HttpServletRequest;

public class BackendCountingService {

    // resttemplate webclient

    public void putStatistics(HttpServletRequest request){


        // Получаем IP-адрес пользователя
        String ipAddress = request.getHeader("X-Forwarded-For") == null ? request.getRemoteAddr() : request.getHeader("X-Forwarded-For");
        // Получаем User-Agent пользователя
        String userAgent = request.getHeader("User-Agent");
        String Referer = request.getHeader("Referer");
        String Language = request.getHeader("Accept-Language");
    }

}
