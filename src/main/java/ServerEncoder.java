import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import net.sf.json.JSONObject;

public class ServerEncoder implements Encoder.Text<Messagepojo> {

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public String encode(Messagepojo object) throws EncodeException {
		JSONObject obj = JSONObject.fromObject(object);
		return obj.toString();

	}

}
