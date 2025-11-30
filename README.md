🌊 Swello – Backend

Bienvenido al backend de Swello, una plataforma diseñada para ofrecer datos, servicios y conectividad de forma rápida, organizada y optimizada.

Este repositorio contiene el servidor backend en Node.js + Express, junto con la estructura de base de datos en MySQL para levantar el proyecto desde cero.

🚀 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

Node.js (v14+ recomendado)

npm

MySQL Server

MySQL Workbench (opcional pero recomendado)

VS Code (o cualquier editor de preferencia)

📦 Instalación y Configuración
1️⃣ Clonar o descargar el proyecto
git clone https://github.com/tu-repo/swello_backend.git


O descarga el ZIP desde GitHub.

2️⃣ Descomprimir el archivo

Descomprime el archivo swello_backend.zip y ábrelo en VS Code.

3️⃣ Configurar la base de datos

Abre MySQL Workbench

Crea una nueva conexión llamada:
swello_db

Usuario: root

Password: root

Abre el archivo .sql incluido en el proyecto.

Ejecuta el script completo.

Esto creará automáticamente la base de datos, tablas y datos necesarios.

4️⃣ Instalar dependencias

En la carpeta raíz del proyecto, ejecuta:

npm install

5️⃣ Iniciar el servidor

Una vez instalados los módulos:

npm start


El servidor debería iniciar en:

http://localhost:3000

📁 Estructura del Proyecto
swello_backend/
│
├── src/
│   ├── controllers/
│   ├── routes/
│   ├── helpers/
│   ├── database/
│   └── app.js
│
├── package.json
├── .env (si aplica)
└── swello.sql

🛠️ Tecnologías Utilizadas

Node.js

Express

MySQL

JWT (si aplica)

dotenv

bcrypt / crypto

Y más…

🤝 Contribuciones

¡Las contribuciones son bienvenidas!
Haz un fork, crea una rama y envía un pull request.

🐛 Problemas o Sugerencias

Si encuentras un bug o quieres proponer una mejora, abre un Issue en este repositorio.

📜 Licencia

Este proyecto está bajo la licencia MIT.
Eres libre de usarlo, mejorarlo y compartirlo.

💙 Gracias por usar Swello

Si este proyecto te fue útil, dale una ⭐ en GitHub para apoyar el desarrollo.
