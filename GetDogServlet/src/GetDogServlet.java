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

public class GetDogServlet extends Servlet {

    @Override
    public void serveHttpRequest(HttpRequest request, OutputStream output) {
        System.out.println("Got the request in the GetDogServlet");
        
        String requestUri = request.getUri();
        List<String> uri = Arrays.asList(requestUri.split("/"));

        List<String> relevantUri = uri.subList(2, uri.size());
        
        String jsonResponseObject = null;
        if(checkRelevantUriForGetDog(relevantUri)) {
            jsonResponseObject = getDog(relevantUri.get(1));
        }
        
        HttpResponse response = null;
        // Lets create 200 OK response
        response = new ResponseCommand200().createResponse(jsonResponseObject, Protocol.CLOSE);
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
    
    private String getDog(String dogName) {
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
            String sql = "SELECT * FROM dogs WHERE name=\"" + dogName + "\";";
            ResultSet rs = statement.executeQuery(sql);
            JSONObject dowg = new JSONObject();
            while(rs.next()) {
                System.out.println("ResultSet: " + rs.toString());
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                String breed = rs.getString("breed");
                String description = rs.getString("description");
                
                dowg.put("name", name);
                dowg.put("age",  age);
                dowg.put("breed", breed);
                dowg.put("description", description);
            }
            String stringDowg = dowg.toJSONString();
            
            rs.close();
            statement.close();
            c.close();
            return stringDowg;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
