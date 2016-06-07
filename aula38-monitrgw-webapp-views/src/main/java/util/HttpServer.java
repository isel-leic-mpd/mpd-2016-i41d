package util;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Function;

/**
 * @author Miguel Gamboa
 *         created on 03-06-2016
 */
public class HttpServer {
    private final Server server;
    private final ServletHandler container;

    public HttpServer(int port) {
        server = new Server(port);  // Http Server no port
        container = new ServletHandler(); // Contentor de Servlets
        server.setHandler(container);
    }

    public HttpServer addHandler(String path, Function<HttpServletRequest, String> handler) {
        /*
         * Associação entre Endpoint <-> Servlet
         */
        container.addServletWithMapping(new ServletHolder(new TimeServlet(handler)), path);
        return this;
    }

    public void run() throws Exception {
        server.start();
        server.join();
    }
}

class TimeServlet extends HttpServlet{
    private final Function<HttpServletRequest, String> handler;

    public TimeServlet(Function<HttpServletRequest, String> handler) {
        this.handler = handler;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, IOException {
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));

        String respBody = handler.apply(req);

        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setStatus(200);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}