import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import request.processing.Servlet;
import response.commands.ResponseCommand201;
import response.commands.ResponseCommand400;
import response.commands.ResponseCommand409;

public class PostDogServlet extends Servlet {

    @Override
    public void serveHttpRequest(HttpRequest request, OutputStream output) {
        HttpResponse response = null;

        String requestUri = request.getUri();
        List<String> uri = Arrays.asList(requestUri.split("/"));
        List<String> relevantUri = uri.subList(2, uri.size());

        String body = String.copyValueOf(request.getBody());
        JSONParser parser = new JSONParser();
        JSONObject requestJSON;
        try {
            requestJSON = (JSONObject) parser.parse(body);
        } catch (ParseException e1) {
            response = new ResponseCommand400().createResponse((File) null, Protocol.CLOSE);
            try {
                response.write(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        boolean result = false;
        if (checkRelevantUriForGetDog(relevantUri)) {
            result = createDog(requestJSON);
        }

        if (result) {
            // Lets create 201 response
            response = new ResponseCommand201().createResponse((File) null, Protocol.CLOSE);
        } else {
            response = new ResponseCommand409().createResponse((File) null, Protocol.CLOSE);
        }
        try {
            response.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkRelevantUriForGetDog(List<String> uri) {
        System.out.println(uri);
        if (uri.get(0).equals("v1") && uri.size() == 1) {
            return true;
        }

        return false;
    }

    private boolean createDog(JSONObject dogToInsert) {
        Connection c = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            Path dbPath = Paths.get("../../webserver.db");
            dbPath = dbPath.toAbsolutePath().normalize();
            c = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString());
            c.setAutoCommit(false);
            System.out.println("connected to database successfully");

            String name = (String) dogToInsert.get("name");
            Long age = (Long) dogToInsert.get("age");
            String breed = (String) dogToInsert.get("breed");
            String desc = (String) dogToInsert.get("description");

            if (desc != null) {
                desc = "\"" + desc + "\"";
            }

            statement = c.createStatement();
            String sql = "INSERT INTO dogs VALUES(\"" + name + "\"," + age + ",\"" + breed + "\"," + desc + ");";
            statement.executeUpdate(sql);
            c.commit();

            statement.close();
            c.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
