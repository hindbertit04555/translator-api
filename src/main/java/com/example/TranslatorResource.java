package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/translate")
public class TranslatorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response translate(@QueryParam("text") String text) {

        if (text == null || text.isBlank()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"No text provided\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "Authorization, Content-Type")
                    .build();
        }

        String translation = GeminiService.translate(text);
        String json = "{\"translation\":\"" + translation + "\"}";

        return Response.ok(json)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Authorization, Content-Type")
                .build();
    }

    @jakarta.ws.rs.OPTIONS
    @Path("{path:.*}")
    public Response handleOptions() {
        return Response.ok()
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .header("Access-Control-Allow-Headers", "Authorization, Content-Type")
                .build();
    }
}