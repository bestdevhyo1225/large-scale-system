rootProject.name = "large-scale-system"

include(
    // order-payment-server
    "order-payment-server",
    "order-payment-server:api",
    "order-payment-server:common",
    "order-payment-server:domain",
    "order-payment-server:infrastructure:jpa",
    // shortened-url-server
    "shortened-url-server",
    // shortened-url-server-ver2
    "shortened-url-server-ver2:command",
    "shortened-url-server-ver2:common",
    "shortened-url-server-ver2:domain:rdbms",
    "shortened-url-server-ver2:domain:nosql",
    "shortened-url-server-ver2:query",
    // shortened-url-server-ver3
    "shortened-url-server-ver3:command",
    "shortened-url-server-ver3:common",
    "shortened-url-server-ver3:domain",
    "shortened-url-server-ver3:infrastructure:jpa",
    "shortened-url-server-ver3:infrastructure:r2dbc",
    "shortened-url-server-ver3:infrastructure:redis",
    "shortened-url-server-ver3:infrastructure:redis-reactive",
    "shortened-url-server-ver3:infrastructure:redisson",
    "shortened-url-server-ver3:query",
    "shortened-url-server-ver3:query-webflux",
    // sns-service
    "sns-service:application:admin",
    "sns-service:application:batch",
    "sns-service:application:command",
    "sns-service:application:query",
    "sns-service:application:query-webflux",
    "sns-service:data:jpa",
    "sns-service:data:redis",
    "sns-service:domain",
    "sns-service:infrastructure:okhttp3",
)
