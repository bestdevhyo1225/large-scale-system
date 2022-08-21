rootProject.name = "large-scale-system"

include(
    // shortened-url-server
    "shortened-url-server",
    // shortened-url-server-ver2
    "shortened-url-server-ver2:command",
    "shortened-url-server-ver2:common",
    "shortened-url-server-ver2:domain:rdbms",
    "shortened-url-server-ver2:domain:nosql",
    "shortened-url-server-ver2:query",
    // shortened-url-server-ver3
    "shortened-url-server-ver3:common",
    "shortened-url-server-ver3:domain",
    "shortened-url-server-ver3:infrastructure:redis",
    "shortened-url-server-ver3:infrastructure:jpa",
    "shortened-url-server-ver3:query",
)
