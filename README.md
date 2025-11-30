# 🌊 **Swello -- Backend**

Bienvenido al backend de **Swello**, una plataforma diseñada para
ofrecer datos, servicios y conectividad de forma rápida, organizada y
optimizada.

Este repositorio contiene el servidor backend en **Node.js + Express**,
junto con la estructura de base de datos en **MySQL** para levantar el
proyecto desde cero.

------------------------------------------------------------------------

## 🚀 **Requisitos Previos**

Antes de comenzar, asegúrate de tener instalado:

-   **Node.js** (v14+ recomendado)\
-   **npm**\
-   **MySQL Server**\
-   **MySQL Workbench**\
-   **VS Code**

------------------------------------------------------------------------

## 📦 **Instalación y Configuración**

### 1️⃣ Clonar o descargar el proyecto

``` bash
git clone https://github.com/tu-repo/swello.git
```

### 2️⃣ Descomprimir el archivo

Descomprime `swello_backend.zip` en un fichero aparte y ábrelo en **VS Code**.

### 3️⃣ Configurar la base de datos

1.  Abre **MySQL Workbench**
2.  Crea una conexión llamada **swello_db**
    -   Usuario: root
    -   Password: root
3.  Abre el archivo `.sql` incluido en el proyecto.
4.  Ejecútalo.

Esto generará la base de datos y sus tablas.

### 4️⃣ Instalar dependencias

En VS Code en una terminal en el directorio creado para el backend
``` bash
npm install
```

### 5️⃣ Iniciar el servidor

``` bash
npm start
```

Servidor disponible en:

    http://localhost:3000

------------------------------------------------------------------------

## 📁 Estructura del Proyecto

    swello_backend/
    ├── src/
    │   ├── controllers/
    │   ├── routes/
    │   ├── helpers/
    │   ├── database/
    │   └── app.js
    ├── package.json
    ├── .env
    └── swello.sql

------------------------------------------------------------------------

## 🛠️ Tecnologías utilizadas

-   Node.js\
-   Express\
-   MySQL\
-   JWT\
-   dotenv\
-   bcrypt / crypto

------------------------------------------------------------------------

## 🤝 Contribuciones

¡Bienvenidas!\
Haz un fork, crea una rama y envía un PR.

------------------------------------------------------------------------

## 🐛 Problemas o sugerencias

Abre un **Issue** en el repositorio.

------------------------------------------------------------------------

## 📜 Licencia

Proyecto bajo licencia **MIT**.

------------------------------------------------------------------------

## 💙 Gracias por usar Swello

Si te gustó el proyecto, deja una ⭐ en GitHub.
