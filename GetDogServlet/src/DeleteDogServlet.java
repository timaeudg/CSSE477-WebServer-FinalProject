import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.Protocol;
import request.processing.Servlet;
import response.commands.ResponseCommand200;


public class DeleteDogServlet extends Servlet {


    @Override
    public void serveHttpRequest(HttpRequest request, OutputStream output) {
        
        String requestUri = request.getUri();
        List<String> uri = Arrays.asList(requestUri.split("/"));

        List<String> relevantUri = uri.subList(2, uri.size());
        
        String jsonResponseObject = null;
        if(checkRelevantUriForGetDog(relevantUri)) {
            boolean result = deleteDog(relevantUri.get(1));
        }
        
        HttpResponse response = null;
        // Lets create 200 OK response
        response = new ResponseCommand200().createResponse((File) null, Protocol.CLOSE);
        try {
            response.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean checkRelevantUriForGetDog(List<String> uri) {
        System.out.println(uri);
        if(uri.get(0).equals("v1") && uri.size() == 2) {
            return true;
        }
        
        return false;
    }
    
    private boolean deleteDog(String dogName) {
        Connection c = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            Path dbPath = Paths.get("../../webserver.db");
            dbPath = dbPath.toAbsolutePath().normalize();
            c = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toString());
            c.setAutoCommit(false);
            System.out.println("connected to database successfully");
            
            statement = c.createStatement();
            String sql = "DELETE FROM dogs WHERE name=\"" + dogName + "\";";
            statement.executeUpdate(sql);
            c.commit();
            
            statement.close();
            c.close();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
