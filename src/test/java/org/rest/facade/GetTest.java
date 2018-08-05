package org.rest.facade;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.mockito.Mockito;
import org.rest.facade.http.RestControlServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

public class GetTest extends Mockito {

    @Test
    public void testGetResource() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(new StringBuffer("/resource"));
        when(request.getPathInfo()).thenReturn("/resource");

        RestControlServlet servlet = new RestControlServlet();
        servlet.init();
        servlet.doGet(request, response);
        servlet.destroy();
        writer.flush();

        String jsonArray = stringWriter.toString();
        JsonArray arr = new JsonArray();
        arr.add(jsonArray);
    }


}