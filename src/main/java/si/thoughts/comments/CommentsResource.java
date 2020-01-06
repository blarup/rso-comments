package si.thoughts.comments;

import com.kumuluz.ee.logs.cdi.Log;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("comments")
@Log
public class CommentsResource {
    @Inject
    private ConfigurationProperties cfg;

    @POST
    public Response addComment(@QueryParam("textId") int textId,
                               @QueryParam("content") String content){
        try(
                Connection con = DriverManager.getConnection(cfg.getDbUrl(),cfg.getDbUser(),cfg.getDbPassword());
                Statement stmt = con.createStatement();
        ){
            stmt.executeUpdate("INSERT INTO comments (textId,content,created)"
                    + " VALUES ('" + textId + "', '" + content + "', now())");
        }catch (SQLException e){
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok().build();
    }

    @GET
    public Response getComments(){
        List<Comment> comments = new LinkedList<Comment>();

        try(
                Connection con = DriverManager.getConnection(cfg.getDbUrl(),cfg.getDbUser(),cfg.getDbPassword());
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM comments");
        ){
            while(rs.next()){
                Comment comment = new Comment();
                comment.setId(rs.getInt(1));
                comment.setTextId(rs.getInt(2));
                comment.setContent(rs.getString(3));
                comment.setCreated(rs.getTimestamp(4).toInstant());
                comments.add(comment);
            }
        }
        catch(SQLException e){
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(comments).build();
    }
}
