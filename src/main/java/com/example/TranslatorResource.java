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
    @Produces(MediaType.APPLICATION_JSON)
    public Response translate(@QueryParam("text") String text) {

        if (text == null || text.isBlank()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"No text provided\"}")
                    .build();
        }

        String translation = GeminiService.translate(text);
        String json = "{\"translation\":\"" + translation + "\"}";
        return Response.ok(json).build();
    }
}