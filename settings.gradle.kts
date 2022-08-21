rootProject.name = "large-scale-system"

include(
    "shortened-url-server",
    "shortened-url-server-ver2:command",
    "shortened-url-server-ver2:common",
    "shortened-url-server-ver2:domain:rdbms",
    "shortened-url-server-ver2:domain:nosql",
    "shortened-url-server-ver2:query",
)
