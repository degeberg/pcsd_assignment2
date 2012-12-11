import java.io.IOException;

import javax.xml.ws.Endpoint;

import assignmentImplementation.KeyValueBaseService;


public class Main {

    public static void main(String[] args) throws Exception {
        Endpoint.publish("http://localhost:8080/kv",
                new KeyValueBaseService());

    }

}
