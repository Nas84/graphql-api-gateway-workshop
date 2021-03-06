package workshop.gateway;

import graphql.GraphQL;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.ErrorHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.reactivex.ext.web.handler.graphql.GraphiQLHandler;

public class Step1Server extends WorkshopVerticle {

  protected Router createRouter() {
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    GraphQL graphQL = setupGraphQLJava("musicstore.graphql");
    router.route("/graphql").handler(GraphQLHandler.create(graphQL));

    router.get("/graphiql/*").handler(GraphiQLHandler.create());

    router.get().handler(StaticHandler.create());
    router.get().handler(WorkshopVerticle::rerouteToVueIndex);

    router.route().failureHandler(ErrorHandler.create());

    return router;
  }

  protected RuntimeWiring runtimeWiring() {
    return RuntimeWiring.newRuntimeWiring()
      .type("Query", this::query)
      .build();
  }

  private TypeRuntimeWiring.Builder query(TypeRuntimeWiring.Builder builder) {
    return builder
      .dataFetcher("genres", new GenresDataFetcher(genresRepository))
      ;
  }
}
