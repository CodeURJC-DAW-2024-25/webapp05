# Variables
$dockerHubUser = "vcandel"  # <-- CAMBIA esto a tu usuario real
$image = "$dockerHubUser/gymdb:latest"

# Pushear imágenes a Docker Hub
docker push $image