docker container run --name postgresdb -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
docker cp proyect_db.sql postgresdb:/
psql -U postgres --file proyect_db.sql
psql -U user_watcher --file init-script.sql
#psql -h localhost -d expensetrackerdb -U expensetracker -p 5432
#psql -h localhost -d project_db -U user_watcher -p 5432
docker container exec -it postgresdb psql -h localhost -d project_db -U user_watcher -p 5432
docker build -t alejandroqo/service_users .
docker run -p 8080:8080 alejandroqo/service_users
docker network create mi-red
docker run --name postgresdb -e POSTGRES_PASSWORD=admin -d -p 5432:5432 --network mi-red postgres
docker run --name mi-servicio -e DB_HOST=postgresdb -e DB_PORT=5432 -e DB_USERNAME=user_watcher -e DB_PASSWORD=password -d -p 8080:8080 --network mi-red alejandroqo/service_users

export SERVICE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].port}" services mytestchart-nginx)
export SERVICE_IP=$(kubectl get svc --namespace default mytestchart-nginx -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
echo "http://${SERVICE_IP}:${SERVICE_PORT}"



