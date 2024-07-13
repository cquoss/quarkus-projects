package de.quoss.quarkus.undertow;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/")
public class Servlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Servlet.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.getWriter().write("Hello from Quarkus Undertow!");
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final String methodName = "do-post(http-servlet-request, http-servlet-response)";
        final List<String> headerNames = new LinkedList<>();
        Enumeration<String> headerNamesEnumeration = request.getHeaderNames();
        while (headerNamesEnumeration.hasMoreElements()) {
            headerNames.add(headerNamesEnumeration.nextElement());
        }
        LOGGER.info("{} ["
                + "request.content-type={},"
                + "request.content-length-long={},"
                + "header-names={}"
                + "]",
                methodName,
                request.getContentType(),
                request.getContentLengthLong(),
                headerNames
                );
    }

}