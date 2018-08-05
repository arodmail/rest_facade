package org.rest.facade;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.rest.facade.http.RestControlServlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PutTest {

    @Test
    public void testPostResource() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURL()).thenReturn(new StringBuffer("/resource/1"));
        when(request.getPathInfo()).thenReturn("/resource/1");
        when(request.getContentType()).thenReturn("application/json");
        when(request.getInputStream()).thenReturn((ServletInputStream)getInputStreamPayload());

        RestControlServlet servlet = new RestControlServlet();
        servlet.init();
        servlet.doPut(request, response);
        servlet.destroy();
        writer.flush();

        String jsonArray = stringWriter.toString();
        JsonArray arr = new JsonArray();
        arr.add(jsonArray);
    }

    private InputStream getInputStreamPayload() {
        StringBuilder payLoadStr = new StringBuilder();
        payLoadStr.append("{");
        payLoadStr.append("\"name\" : \"name-1\",");
        payLoadStr.append("\"id\" : \"1\"");
        payLoadStr.append("}");

        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(payLoadStr.toString().getBytes(StandardCharsets.UTF_8));

        ServletInputStream servletInputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

        };

        return servletInputStream;
    }

}
