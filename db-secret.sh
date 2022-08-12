kubectl create secret generic mdacosta-simple-autos-db \
	--from-literal=DB_USER=mdacosta \
	--from-literal=DB_PWD=0lDWR6cakwC7h \
	--from-literal=DB_HOST=vmware-db.galvanizelabs.net:3306 \
	--from-literal=DB_NAME=mdacosta_db
