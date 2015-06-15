package com.example;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SlowCoachFilter implements Filter {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        StringBuffer url = ((HttpServletRequest) servletRequest).getRequestURL();
        System.out.println("hi " + url);
        ScheduledFuture<?> future = executor.schedule(new SlowLogger(Thread.currentThread(), url), 1, TimeUnit.SECONDS);
        filterChain.doFilter(servletRequest, servletResponse);
        future.cancel(false);
        System.out.println("bye");
    }

    @Override
    public void destroy() {
        executor.shutdownNow();
    }

    private static class SlowLogger implements Runnable {

        private final Thread target;
        private final StringBuffer url;

        public SlowLogger(Thread target, StringBuffer url) {
            this.target = target;
            this.url = url;
        }

        @Override
        public void run() {
            System.out.println("Slow! " + target.getName() + " " + target.getState() + " " + url);
            StackTraceElement[] stackTrace = target.getStackTrace();
            for (StackTraceElement e : stackTrace) {
                System.out.println("  " + e);
            }
        }
    }
}
