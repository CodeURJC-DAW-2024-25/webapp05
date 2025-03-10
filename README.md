# AlphaGym

## 📋 Integrantes del equipo de desarrollo

| Nombre        | Apellidos           | Correo Uni  | Cuenta Github|
| ------------- |:-------------:| :---------:|:---------:|
|  Catalin   | Mazarache | c.mazarache.2021@alumnos.urjc.es | CataUrjc|
|  Adrián   | Esteban Martin      |   a.estebanm.2021@alumnos.urjc.es | aadri-2003 |
| Jonathan Xavier | Medina Salas    |   jx.medina@alumnos.urjc.es | XdeXavi |
| Adrián | Dueñas Mínguez  |   a.duenas.2021@alumnos.urjc.es | AdriDM-urjc |
| Víctor | Candel Casado     |   v.candel.2020@alumnos.urjc.es  | victorcc02 |

## 🔗 Herramientas de coordinación
Usamos **Trello** para la organización del equipo. Puedes acceder al tablero público aquí: [GymBros Trello](https://trello.com/w/daw051)

## 🏋️‍♂️ Entidades
Las principales entidades de la aplicación son:
- **Usuario**: Clientes del gimnasio que pueden acceder a entrenamientos y planes de nutrición.
- **Nutrición**: Planes alimenticios personalizados según los objetivos del usuario.
- **Entrenamiento**: Rutinas de ejercicios personalizadas para cada usuario.
- **Comentario Entrenamiento**: Los usuarios pueden publicar comentarios sobre los entrenamientos.
- **Comentario Nutrición**: Los usuarios pueden publicar comentarios sobre la nutrición.
  
![image](https://github.com/user-attachments/assets/4bc5973a-0ea9-4801-81b8-f486ba7dab55)




## 🔑 Permisos de los usuarios
- **Anónimo**: Puede ver los diferentes entreamientos y rutinas disponibles, no puede acceder a los comentarios personalizados de entrenamiento ni a los comentarios de nutrición, puede registrarse
- **Registrado**: Puede ver los diferentes comentarios publicados(Comentario), puede publicar un comentario(Comentario),  puede acceder a sus planes de entrenamiento(Entrenamiento), puede solicitar planes personalizados o automáticos de entrenamiento(Entrenamiento), puede acceder a sus planes de nutrición(Nutrición), puede solicitar planes personalizados o automáticos de nutrición(Nutrición).
- **Admin**: Tiene todos los permisos de un usuario registrado y permisos para crear planes de entrenamiento y de nutrición.

## 🖼️ Imágenes
Las siguientes entidades tendrán imágenes asociadas:
- **Usuario**: Los usuarios podrán tener foto de perfil.
- **Entrenamiento**: Los entrenamientos tendrán fotos asignadas.
- **Nutrición**: La nutrición tendrá imágenes asociadas.

## 📊 Gráficos
Se mostrarán los siguientes gráficos:
- **Nutrición**: El plan de nutrición tendrá un gráfico de sectores donde mostrará información de la dieta.

## 🛠️ Tecnología complementaria
- **Entrenamiento**: Generará PDFs con planes de entrenamiento.
- **Nutrición**: Generará PDFs con planes de nutrición.


## 🤖 Algoritmo o consulta avanzada
- **Entrenamiento**: Hacer un algoritmo que genere un plan de entrenamiento en base a los objetivos y datos de información del usuario.
- **Nutrición**: Hacer un algoritmo que genere un plan de nutrición en base a los objetivos y datos de información del usuario.

# Fase 1 
## 💻 Pantallas

## Pantalla de Inicio:
Nuestra pantalla de inicio de AlphaGym presenta una interfaz acogedora con opciones de navegación. Destacamos las diferentes opciones que presenta nuestro gimnsaio, como rutinas y dietas. Además de la ubicación y más datos informativos sobre nuestro gimnasio.

![127 0 0 1_5500_index html](https://github.com/user-attachments/assets/432209d9-30a9-4233-8721-9afb64a6fedb)

## Pantalla de Inicio de Sesión:
En esta pantalla los usuarios deben poner el correo electrónico y la contraseña correspondiente a sus cuentas, una vez rellenados el usuario clickará en el botón "Login", el usuario tiene la opción de cambiar la contraseña (pulsando en el enlace "Forgot Password?" en caso de que no la recuerde. Los usuarios que no tengan cuenta podrán crearse una a través del link "Sign up".

![image](https://github.com/user-attachments/assets/cf555160-6061-4a81-8612-3a77a37c7018)

## Pantalla de Registro:
Nuestra pantalla de registro se basa en una interfaz muy intuitiva ya que el usuario debe rellenar el formulario que se puede ver a la derecha de la pantalla. El usuario tiene que rellenar el formulario con su nombre completo, un correo electrónico y una contraseña que se le pide que repita para verificar que es la contraseña puesta es correcta. Finalmente hace click en el botón de "Submit" para dar de alta su cuenta.

![127 0 0 1_5500_register html](https://github.com/user-attachments/assets/4efaed7f-74d4-4213-8f7f-ab3bdd4fe278)

## Pantalla de Rutinas:
El usuario puede ver todas las rutinas que ofrece nuestro gimnasio y añadir las suyas propias, esta última opción solo en el caso de que estén registrados. Todos los usuarios podrán acceder a los detalles de la rutina que deseen haciendo click en el botón de dicha rutina.

![image](https://github.com/user-attachments/assets/80b080aa-c31e-4fdc-9677-72d79fc7b6df)

## Pantalla de Detalles de Rutinas:
Dependiendo del tipo de usuario que sea y los permisos que tenga el usuario podrá realizar diferentes acciones en esta pantalla como editar, borrar, comentar y ver otros comentarios o suscribirse a una rutina. Por último, existe un botón para que el usuario pueda regresar a la pantalla anterior.

![image](https://github.com/user-attachments/assets/77d11fd5-2193-44b5-9ac1-6bced72c89c9)

## Pantalla para Añadir Rutinas: 
Esta pantalla será lo que verá el usuario cuando decida añadir una rutina, para ello deberá rellenar un formulario con diferentes campos: el nombre, la intensidad, el objetivo y los ejercicios de la rutina. También tiene la opción de añadir algún comentario a la hora de crearla. Una vez rellenados todos los datos el usuario subirá su rutina a través del botón "Save Routine".

![image](https://github.com/user-attachments/assets/c85c2921-3e5f-4861-8637-3b3f0cd14086)

## Pantalla de Dietas:
El usuario puede ver todas las dietas que ofrece nuestro gimnasio y añadir sus propias dietas, esta última opción solo en el caso de que estén registrados. Todos los usuarios podrán acceder a los detalles de la dieta que deseen haciendo click en el botón de dicha dieta.

![image](https://github.com/user-attachments/assets/57745212-ff0f-42fe-bd9d-698726841611)

## Pantalla de Detalles de Dietas:
Dependiendo del tipo de usuario que sea y los permisos que tenga el usuario podrá realizar diferentes acciones en esta pantalla como editar, borrar, comentar y ver otros comentarios o suscribirse a una dieta. Por último, hay un botón para que el usuario pueda regresar a la pantalla anterior.

![image](https://github.com/user-attachments/assets/87aedbb4-b887-4de8-80fa-9643fd5de293)

## Pantalla para Añadir Dietas:
Esta pantalla será lo que verá el usuario cuando decida añadir una nueva dieta, para ello deberá rellenar un formulario con diferentes campos: el nombre, el número de calorías, el objetivo y las comidas de la rutina. También tiene la opción de añadir algún comentario a la hora de crearla. Una vez rellenados todos los datos el usuario subirá su dieta a través del botón "Save Diet".

![image](https://github.com/user-attachments/assets/4ac22f67-1211-48cb-a159-88fc4ae28c7a)

## Pantalla para Ver Comentarios: 
Esta pantalla podrán usarla todos los usuarios. En ella los usuarios verán los diferentes comentarios realizados sobre una dieta o una rutina. Cada comentario tiene un botón para notificar al administrador en el caso de que el usuario considere inapropiado el comentario. El dueño podra modificar sus comentarios y los admin podran eliminar y editar todos los comentarios.
Habrá una interfaz ligeramente diferente para los comentarios sobre dietas y sobre entrenamientos.

![image](https://github.com/user-attachments/assets/1c4b3d5d-d57b-4a57-afbb-38ede200053e)
![image](https://github.com/user-attachments/assets/6a09f9ca-a0eb-44d0-896a-100cbf1d2952)

## Pantalla para Editar Comentarios: 
Se necesitarán ciertos privilegios para acceder a esta página. Los usuarios podrán editar sus propios comentarios y los admin podrán editar todos.

![image](https://github.com/user-attachments/assets/955c9ca5-6be2-475e-9157-c0a6ae8f0cdd)

## Pantalla para Añadir Comentarios: 
Al igual que la pantalla anterior se necesitan ciertos privilegios para acceder a esta página. Los usuarios podrán realizar comentarios acerca de una rutina o dieta rellenando el formulario que consta de un nombre para el comentario y el propio comentario.

![image](https://github.com/user-attachments/assets/d6bfca46-7d2a-4d65-9802-18f92592fc10)

## Pantalla de Administrador: 
A esta pantalla solo tendrán acceso los administradores. Aquí pueden ver los datos de su perfil, las notificaciones recibidas sobre algún comentario y todas las rutinas y dietas que existen con opción a editarlas o borrarlas en caso de que así lo crean.

![image](https://github.com/user-attachments/assets/9571227d-bf9d-46e2-8d47-fd76d92cee0d)

## Pantalla de Usuario: 
El usuario verá los datos de su perfil y las rutinas y las dietas a las que está suscrito. Desde esta página también puede borrar alguna dieta o rutina a las que esté suscrito.

![127 0 0 1_5500_account html (1)](https://github.com/user-attachments/assets/168866e3-02c8-4f48-90a5-9d61db63ab51)

## Pantalla de error: 
En el caso de que el usuario intente acceder a una pantalla para la que no tenga permisos o surga algún problema con la página se mostrará esta pantalla.

![image](https://github.com/user-attachments/assets/fcd2c7d5-5d1f-4ab1-b42d-9ebfe7d4caaf)


## Diagrama de navegación: 
- **Azul**: Todos los usuarios.
- **Verde**: Usuario registrado y admin.
- **Rojo**: Solo admin.
- **Nota**: Desde todas las páginas se puede acceder a la pantalla de error.

![image](https://github.com/user-attachments/assets/17edecec-d86c-4738-a6df-55e3969d6ebd)

## Diagrama de las entidades de la base de datos: 
Este es el diagrama generado por MySQL Workbench con las entidades que tenemos configuradas en la base de datos y la relacion entre ellas:

![image](https://github.com/user-attachments/assets/7681880b-314a-408d-a5eb-6a743a78afd0)


## Participacion de los miembros

### Adrián Dueñas Minguez

#### Descripcion general:
Principalmente he participado en los comentarios, en su gestión, creación, edición... También he contribuido a otras tareas menores.

#### Mis 5 commits mas relevantes

| Commit | Descripción                          | Link                                                                                                                                                                                                 |
| ------ | ------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| #1     | Create comments                      | [https://github.com/CodeURJC-DAW-2023-24/webapp06/commit/1079ce988111789ac53597399cd1cc77affc1b58](https://github.com/CodeURJC-DAW-2024-25/webapp05/commit/6c4ff4eb3b8c1fab6a0a94c2f46d5bddf79527ad) |
| #2     | Gestión de reports por los admin     | [https://github.com/CodeURJC-DAW-2023-24/webapp06/commit/30dc1d5295eaf569c70949df9d4022b0c87b9b4a](https://github.com/CodeURJC-DAW-2024-25/webapp05/commit/ece055ffa4fa6694f6ea7f4777ad4ec4a8d3b648) |
| #3     | Ajax development                     | [https://github.com/CodeURJC-DAW-2023-24/webapp06/commit/0acc1f72e4650b2b1999ea068373f52b02d2425b](https://github.com/CodeURJC-DAW-2024-25/webapp05/commit/7b0adc21272d4aad93f67e09bdeda9f4770757c5) |
| #4     | Report and delete comments           | [https://github.com/CodeURJC-DAW-2023-24/webapp06/commit/329d4733cb21f99717ba764d50bceb7457a65f13](https://github.com/CodeURJC-DAW-2024-25/webapp05/commit/c127cd78daff735e6894d2504fce7f5b41e704de) |
| #5     | Edit comments                        | [https://github.com/CodeURJC-DAW-2023-24/webapp06/commit/728a361bad3b80b9e13621c33d415272a9fec7ac](https://github.com/CodeURJC-DAW-2024-25/webapp05/commit/48b416a8149d41227c63d2b5ec3c116146f1125e) |

#### Los 5 ficheros que más he modificado

| Número fichero | Fichero                                                                                                                                                                                    |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| #1             | [NutritionCommentController.java](https://github.com/CodeURJC-DAW-2024-25/webapp05/blob/main/backend/src/main/java/es/codeurjc/daw/alphagym/controller/NutritionCommentController.java)    |
| #2             | [TrainingCommentController.java](https://github.com/CodeURJC-DAW-2024-25/webapp05/blob/main/backend/src/main/java/es/codeurjc/daw/alphagym/controller/TrainingCommentController.java)      |
| #3             | [NutritionCommentService.java](https://github.com/CodeURJC-DAW-2024-25/webapp05/blob/main/backend/src/main/java/es/codeurjc/daw/alphagym/service/NutritionCommentService.java)             |
| #4             | [TrainingCommentService.java](https://github.com/CodeURJC-DAW-2024-25/webapp05/blob/main/backend/src/main/java/es/codeurjc/daw/alphagym/service/TrainingCommentService.java)               |
| #5             | [file.js](https://github.com/CodeURJC-DAW-2024-25/webapp05/blob/main/backend/src/main/resources/static/js/file.js)                                                                         |








