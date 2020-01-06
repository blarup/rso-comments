package si.thoughts.comments;

import com.kumuluz.ee.grpc.annotations.GrpcService;
import io.grpc.stub.StreamObserver;
import javax.enterprise.inject.spi.CDI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@GrpcService
public class GrpcInterface extends CommentsGrpc.CommentsImplBase{
    ConfigurationProperties cfg;

    @Override
    public void commentCleanUp(CommentsService.CleanUpRequest request,
                                StreamObserver<CommentsService.CleanUpResponse> responseObserver){
        Integer status = 0;

        cfg = CDI.current().select(ConfigurationProperties.class).get();

        try(
                Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
                Statement stmt = con.createStatement();
                ){
            stmt.executeUpdate("DELETE FROM comments WHERE textId = " + request.getId());
        }catch(SQLException e){
            System.err.println(e);
            status = -1;
        }

        CommentsService.CleanUpResponse response = CommentsService.CleanUpResponse.newBuilder()
                .setStatus(status).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
