package org.rest.facade;

import org.junit.Test;
import org.rest.facade.http.RestControlServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteTest {

    @Test
    public void testDeleteResource() throws Exception {

        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getMethod()).thenReturn("DELETE");
        when(request.getRequestURL()).thenReturn(new StringBuffer("/resource/123"));
        when(request.getPathInfo()).thenReturn("/resource/123");

        RestControlServlet servlet = new RestControlServlet();
        servlet.init();
        servlet.doDelete(request, response);
        servlet.destroy();
        writer.flush();

    }

}
