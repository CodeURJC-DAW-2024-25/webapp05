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
});
