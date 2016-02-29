package nju.tb.net;


import org.apache.http.HttpResponse;

public class Parse {
    interface ParseHttp{
        void parseHttpResponse(HttpResponse httpResponse);
    }
}
