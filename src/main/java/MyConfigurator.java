import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @ClassName Configurator
 * @Description TODO
 * @Author pyt
 * @Date 2019/3/28 15:55
 * @Version
 */
public class MyConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put("ClientIP", httpSession.getAttribute("ClientIP"));//把HttpSession中保存的ClientIP放到ServerEndpointConfig中，关键字可以跟之前不同
    }
}
