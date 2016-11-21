package user;

import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user.model.User;
import user.repository.UserRepository;

@Component
public class SocialUserVerticle extends AbstractVerticle {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        //FIXME POST method should be used for Create handler
        router.post("/api/users").method(HttpMethod.POST).handler(rc -> {

            User user = null;
            try {
                user = objectMapper.readValue(rc.getBodyAsString(), User.class);
                userRepository.create(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rc.response()
                    .setStatusCode(HttpURLConnection.HTTP_CREATED).putHeader("Location", "FIXME");
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(9090);
    }
}
