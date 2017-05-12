package com.burrsutter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

  @Override
  public void start() {
    String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
    Router router = Router.router(vertx);

    router.get("/").handler(ctx -> {
        ctx.response().end("Aloha Burr Vert.x on " + hostname + " " + new java.util.Date() + "\n");
    });

    router.get("/api/health").handler(ctx -> {
      // returns 200
      ctx.response().end("I'm ok");
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }

}