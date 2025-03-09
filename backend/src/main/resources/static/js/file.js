src="https://cdn.jsdelivr.net/npm/chart.js"
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
  var buttonDeleteRoutine = document.querySelectorAll(".goToDeleteRoutine");
  buttonDeleteRoutine.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        let confirmation = confirm('¿Estas seguro de querer eliminar esta rutina?');
        if(confirmation) {
          window.location.href = "/trainings/delete/" + trainingId; // Send the url to the controller to see the edit training view
        }
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });

  var buttonSuscribeRoutine = document.querySelectorAll(".goToSubscribeRoutine");
  buttonSuscribeRoutine.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        let confirmation = confirm('¿Estas seguro de querer suscribirte esta rutina?');
        if(confirmation) {
          window.location.href = "/trainings/subscribe/" + trainingId;
        }
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });

  var buttonUnsubscribeRoutine = document.querySelectorAll(".goToUnsubscribeRoutine");
  buttonUnsubscribeRoutine.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        let confirmation = confirm('¿Estas seguro de querer desuscribirte de esta rutina?');
        if(confirmation) {
          window.location.href = "/trainings/unsubscribe/" + trainingId;
        }
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });

  var buttonDeleteRoutineFromList = document.querySelectorAll(".goToDeleteRoutineFromList");
  buttonDeleteRoutineFromList.forEach(function (button) {
    button.addEventListener("click", function () {
      var trainingId = button.dataset.trainingId; // We get de id from data-attribute

      if (trainingId) {
        let confirmation = confirm('¿Estas seguro de querer desuscribirte de esta rutina?');
        if(confirmation) {
          window.location.href = "/trainings/deleteFromList/" + trainingId;
        }
      } else {
        console.warn("ID del entrenamiento no encontrado.");
      }
    });
  });


  var buttonShowDiet = document.querySelectorAll(".goToShowDiet");
  buttonShowDiet.forEach(function (button) {
    button.addEventListener("click", function () {
      var nutritionId = button.dataset.nutritionId; // We get de id from data-attribute

      if (nutritionId) {
        window.location.href = "/nutritions/" + nutritionId; // Send the url to the controller to see the training view
      } else {
        console.warn("ID de la nutricion no encontrado.");
      }
    });
  });

  var buttonEditDiet = document.querySelectorAll(".goToEditDiet");
  buttonEditDiet.forEach(function (button) {
    button.addEventListener("click", function () {
      var nutritionId = button.dataset.nutritionId; // We get de id from data-attribute

      if (nutritionId) {
        window.location.href = "/nutritions/editNutrition/" + nutritionId; // Send the url to the controller to see the edit training view
      } else {
        console.warn("ID de la nutricion no encontrado.");
      }
    });
  });
  var buttonDeleteDiet = document.querySelectorAll(".goToDeleteDiet");
  buttonDeleteDiet.forEach(function (button) {
    button.addEventListener("click", function () {
      var nutritionId = button.dataset.nutritionId; // We get de id from data-attribute

      if (nutritionId) {
        let confirmation = confirm('¿Estas seguro de querer eliminar esta dieta?');
        if(confirmation) {
          window.location.href = "/nutritions/delete/" + nutritionId; // Send the url to the controller to see the edit training view
        }
      } else {
        console.warn("ID de la nutricion no encontrado.");
      }
    });
  });

  var buttonSuscribeDiet = document.querySelectorAll(".goToSubscribeDiet");
  buttonSuscribeDiet.forEach(function (button) {
    button.addEventListener("click", function () {
      var nutritionId = button.dataset.nutritionId; // We get de id from data-attribute

      if (nutritionId) {
        let confirmation = confirm('¿Estas seguro de querer suscribirte a esta dieta?');
        if(confirmation) {
          window.location.href = "/nutritions/subscribe/" + nutritionId;
        }
      } else {
        console.warn("ID de la nutricion no encontrado.");
      }
    });
  });

  var buttonUnsubscribeDiet = document.querySelectorAll(".goToUnsubscribeDiet");
  buttonUnsubscribeDiet.forEach(function (button) {
    button.addEventListener("click", function () {
      var nutritionId = button.dataset.nutritionId; // We get de id from data-attribute

      if (nutritionId) {
        let confirmation = confirm('¿Estas seguro de querer desuscribirte de esta dieta?');
        if(confirmation) {
          window.location.href = "/nutritions/unsubscribe/" + nutritionId;
        }
      } else {
        console.warn("ID de la nutricion no encontrado.");
      }
    });
  });

  var buttonDeleteDietFromList = document.querySelectorAll(".goToDeleteDietFromList");
  buttonDeleteDietFromList.forEach(function (button) {
    button.addEventListener("click", function () {
      var nutritionId = button.dataset.nutritionId; // We get de id from data-attribute

      if (nutritionId) {
        let confirmation = confirm('¿Estas seguro de querer desuscribirte de esta dieta?');
        if(confirmation) {
          window.location.href = "/nutritions/deleteFromList/" + nutritionId;
        }
      } else {
        console.warn("ID de la nutricion no encontrado.");
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
  if(name.length > 30){
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

function validateNutritionForm(){

  let name = document.forms["editForm"]["name"].value;
  console.log(dietName)
  if(name === ""){
    alert("Longitud en el nombre de la dieta no válida (nombre vacío).");
    return false;
  }
  if(name.length > 30){
    alert("Longitud en el nombre de la dieta no válida (muy largo).");
    return false;
  }
  let calories = document.forms["editForm"]["calories"].value;
  if(calories < 50 ){
    alert("Calorias no válidas. Las calorias deben ser más de 50 por un tema de salud");
    return false;
  }
  if(calories > 2500){
    alert("No debes exceder las 2500 calorias, esto puede ser malo para tu salud");
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

function editImage(){
  let trainingImage = document.getElementById("uTrainingImage");
  let input = document.getElementById("editTrainingImageInput");
  let trainingImageInput = document.getElementById("trainingImage");

  if(input.files && input.files[0]){
    let reader = new FileReader();
    reader.onload = function(e){
      trainingImage.src = e.target.result;
      trainingImageInput.value = e.target.result;
    };
    reader.readAsDataURL(input.files[0]);
  }else{
    trainingImageInput.value = trainingImageInput.getAttribute("data-training-image");
  }
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

function redirectFromEditComments(event, preventer) {
  event.preventDefault();
  let pathSegments = window.location.pathname.split("/"); 
  pathSegments.pop();
  if (preventer){
    pathSegments.pop();
    let newUrl = pathSegments.join("/");
    window.location.href = newUrl;
  } else {
    let newUrl = pathSegments.join("/");
    let form = document.getElementById("commentForm");
    form.action = newUrl;
    form.submit();
  }
}

document.addEventListener("DOMContentLoaded", function() {
  const chartElement = document.getElementById("reportGraphicChart");
  if (chartElement) { // Solo ejecuta si está en la página correcta
      const reportedCount = parseInt(chartElement.dataset.reported, 10) || 0;
      const nonReportedCount = parseInt(chartElement.dataset.notReported, 10) || 0;
      renderReportChart(reportedCount, nonReportedCount);
  }
});

function renderReportChart(reportedCount, nonReportedCount) {
  const ctx = document.getElementById("reportGraphicChart").getContext("2d");
  new Chart(ctx, {
      type: "pie",
      data: {
          labels: ["Reportados", "No Reportados"],
          datasets: [{
              data: [reportedCount, nonReportedCount],
              backgroundColor: ["red", "blue"]
          }]
      }
  });
}