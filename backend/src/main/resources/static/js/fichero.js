document.addEventListener("DOMContentLoaded", function () {
  console.log("Página cargada, ejecutando scripts...");

  // Función para agregar eventos a botones
  function addNavigationEvent(buttonId, targetPage) {
    var button = document.getElementById(buttonId);
    if (button) {
      button.addEventListener("click", function () {
        window.location.href = targetPage;
      });
    } else {
      console.warn(`Botón '${buttonId}' no encontrado en esta página.`);
    }
  }

  // ✅ Agregamos eventos para los botones necesarios
  addNavigationEvent("newRoutine", "newRoutine.html");
  addNavigationEvent("addDiet", "newDiet.html");

  // ✅ Verificar si el modal de error existe antes de usarlo
  var errorModalElement = document.getElementById("errorModal");
  if (errorModalElement) {
    var errorModal = new bootstrap.Modal(errorModalElement);
    errorModal.show();
    console.log("Modal de error mostrado.");

    errorModalElement.addEventListener("hidden.bs.modal", function () {
      let errorContent = document.getElementById("errorContent");
      let loadingSpinner = document.getElementById("loadingSpinner");
      let errorCard = document.getElementById("errorCard");

      if (errorContent && loadingSpinner && errorCard) {
        errorContent.style.display = "block";

        setTimeout(() => {
          loadingSpinner.style.display = "none";
          errorCard.style.display = "block";
        }, 2000);
      } else {
        console.warn("Elementos de error no encontrados.");
      }
    });
  } else {
    console.warn("Modal 'errorModal' no encontrado en esta página.");
  }

  var buttonShowRoutine = document.querySelectorAll(".goToShowRoutine");
  buttonShowRoutine.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        window.location.href = "/trainings/" + trainingId; // Send the url to the controller to see the training view
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });

  var buttonEditRoutine = document.querySelectorAll(".goToEditRoutine");
  buttonEditRoutine.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        window.location.href = "/trainings/editTraining/" + trainingId; // Send the url to the controller to see the edit training view
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });
  var buttonEditTaining = document.querySelectorAll(".goToEditRoutine");
  buttonEditRoutine.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        window.location.href = "/trainings/editTraining/" + trainingId; // Send the url to the controller to see the edit training view
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });

});


function validateRoutineForm(){

  let name = document.forms["editForm"]["name"].value;
  console.log(routineName)
  if(name === ""){
    alert("Longitud en el nombre de la rutina no válida (nombre vacío).");
    return false;
  }
  if(routineName.length > 30){
    alert("Longitud en el nombre de la rutina no válida (muy largo).");
    return false;
  }
  let duration = document.forms["editForm"]["duration"].value;
  if(duration < 1){
    alert("Duración no válida. Recuerda que la duración se mide en números (minutos).");
    return false;
  }
  if(duration > 200){
    alert("No debes exceder las 6 horas de entrenamiento, esto puede ser malo para tu salud");
    return false;
  }
  let description = document.forms["editForm"]["description"].value;
  if(description.length < 5){
    alert("Longitud incorrecta, demasiado corta. Por favor rellene este campo.");
    return false;
  }
  if(description.length > 255){
    alert("Longitud excedida: 255 caracteres como máximo");
    return false;
  }
  return true;
}

function redirectFromComments() {
  let pathSegments = window.location.pathname.split("/"); 
  let lastSegment = pathSegments.pop(); 
  let firstSegment = pathSegments.pop().replace("Comments", "s"); 
  window.location.href = `/${firstSegment}/${lastSegment}`;
}

function redirectFromNewComments(event, preventer) {
  event.preventDefault();
  let pathSegments = window.location.pathname.split("/"); 
  pathSegments.pop();
  let newUrl = pathSegments.join("/");
  if (preventer){
    window.location.href = newUrl;
  } else {
    let form = document.getElementById("commentForm");
    form.action = newUrl;
    form.submit();
  }
  
}