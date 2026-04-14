package com.example;

import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT");
        if (port == null) port = "8080";

        URI baseUri = URI.create("http://0.0.0.0:" + port + "/");
        ResourceConfig config = new ResourceConfig(TranslatorResource.class);
        JettyHttpContainerFactory.createServer(baseUri, config);
        System.out.println("Server started on port " + port);
        Thread.currentThread().join();
    }
}