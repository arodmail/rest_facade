package org.rest.facade.response;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

/**
 * Extends HttpServletResponseWrapper to apply gzip compression to
 * response payloads.
 *
 * @author A. Rodriguez
 */
public class GzipResponse extends HttpServletResponseWrapper {

    private PrintWriter pw;

    private GzipResponse(HttpServletResponse response) {
        super(response);
        try {
            pw = new PrintWriter(new GZIPOutputStream(response.getOutputStream()));
        } catch (Exception e) {
        }
    }

    public static GzipResponse wrap(HttpServletResponse response) {
        return new GzipResponse(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return pw;
    }
}