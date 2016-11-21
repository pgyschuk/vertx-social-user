package user.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import user.model.User;


public class UserRepository {
    private Vertx vertx;

    public UserRepository(Vertx vertx) {
        this.vertx = vertx;
    }

    public void create(User user) throws SQLException {
        JsonObject config = new JsonObject()
                .put("url", "jdbc:postgresql://localhost:5432/social")
                .put("driver_class", "org.postgresql.Driver")
                .put("username", "postgres")
                .put("password", "postgres")
                .put("max_pool_size", 30);
        JDBCClient client = JDBCClient.createShared(vertx, config);
        client.getConnection(hr -> {
            if (hr.succeeded()) {
                SQLConnection sqlConnection = hr.result();
                JsonArray params = new JsonArray()
                        .add(user.getEmail())
                        .add(user.getFirstName())
                        .add(user.getLastName())
                        .add(user.isAccountVerified())
                        .add(user.getDateOfBirth())
                        .add(user.getAvatarUrl())
                        .add(new Date())
                        .add(user.isDeactivated())
                        .add(user.getPhoneNumber())
                        .add(user.getVerion());
                String query = "INSERT INTO public.users(" +
                        "email, firstname, lasttname, accountverified, dob, avatarurl, created,  deactivated,  phonenumber, version) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                sqlConnection.queryWithParams(query, params, handler -> {
                    System.out.println("Insert Result : "+ handler.result());
                });
            }
        });

    }

}
