package org.rest.facade;

import com.google.gson.JsonParser;
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

public class PostTest {

    @Test
    public void testPostResource() throws Exception {

        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURL()).thenReturn(new StringBuffer("/resource"));
        when(request.getPathInfo()).thenReturn("/resource");
        when(request.getContentType()).thenReturn("application/json");
        when(request.getInputStream()).thenReturn((ServletInputStream)getInputStreamPayload());

        RestControlServlet servlet = new RestControlServlet();
        servlet.init();
        servlet.doPost(request, response);
        servlet.destroy();
        writer.flush();

        String jsonResponse = stringWriter.toString();
        JsonParser jsonParser = new JsonParser();
        jsonParser.parse(jsonResponse);
    }

    private InputStream getInputStreamPayload() {
        StringBuilder payload = new StringBuilder();
        payload.append("{");
        payload.append("\"id\" : \"1\",");
        payload.append("\"name\" : \"name-1\"");
        payload.append("}");

        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(payload.toString().getBytes(StandardCharsets.UTF_8));

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
