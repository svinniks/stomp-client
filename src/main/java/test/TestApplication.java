package test;

import stomp.StompClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TestApplication {

    public static void main(String[] args) throws Exception {

        WebSocketStompTransport transport = new WebSocketStompTransport(new URI("ws://chsrv001:9200/messages"));
        transport.connectBlocking();

        StompClient client = new StompClient(transport);

        Map<String, String> connectHeaders = new HashMap<>();
        connectHeaders.put("X-Access-Token", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ0QzdfT21kWlFVWUhYWklTUmtFZ09pZVNKZ2RwUHdaVVVYTVZkVXh4N18wIn0.eyJqdGkiOiIyMGYyNDQxOS1lMGE3LTQxMWEtYTI4Mi03ODExMjg4YzAxM2IiLCJleHAiOjE1NTgwNDA0NzIsIm5iZiI6MCwiaWF0IjoxNTU3NjA4NDcyLCJpc3MiOiJodHRwOi8vY2hzcnYwMDEuaW50cnVtLm5ldC9hdXRoL3JlYWxtcy9tYXRjaGVyLXNlcnZpY2UiLCJhdWQiOiJtYXRjaGVyLXNlcnZpY2UtYXBpIiwic3ViIjoiODVhMDY2MGItZjVmOS00ZWM0LWIxODYtY2E0YjM4YTMzODdkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibWF0Y2hlci1zZXJ2aWNlLWFwaSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjlkNjljOTg4LTdjZDMtNGY2Zi05Yjg3LTA5YTk4MDExZTRjYSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJtYXRjaGVyLXNlcnZpY2UtYXBpIjp7InJvbGVzIjpbIlNVUEVSVVNFUiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwicHJlZmVycmVkX3VzZXJuYW1lIjoidnoxNmxrbXh3NWRuZ2hqdiJ9.IK1xWvVs-HOAhy4pUcP94_g0AvqxE35lkg-VzqoSZPGEHfSxaZB60L8ssnBeiAQ5mJDXiidz9UZ9PVrLl4nUHFHZu8YeOad0APTHcy0NR3sn2HmIHfB_zsVCQJlVM2_8KVg8YDw778GTjB_bMl1_thRHto6F782ywFpbNoOi4n8oe8n6KhxDwMR6ypD6y_s_F5DiGZthEg_0vMxJcH8fc9hc1HfMq9txyiOwXNPz-LB8uK8UGwiRWq15-JPyD_zhmxW7fLHJLSpC71fW3tFZ6E4WKBjpRso_FC65ddVcNVhGG3kSkZgVTCb2GCSTXKgCry8uU6GJRWNA-Cqr3JAvCQ");

        client.connect(connectHeaders);
        client.subscribe("/batches", message -> System.out.println(message.getBody()));

        Thread.sleep(30000);

//        transport.closeBlocking();
//        System.out.println("Closed");

    }

}
