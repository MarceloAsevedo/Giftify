document.addEventListener('DOMContentLoaded', function() {
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault();
            fetch('/usuarios/logout', {
                method: 'GET'
            }).then(response => {
                if (response.ok) {
                    sessionStorage.removeItem('usuario'); // Elimina el usuario del sessionStorage
                    window.location.href = 'login.html'; // Redirige a login.html
                } else {
                    // Maneja los errores
                    response.json().then(data => {
                        alert('Error al cerrar sesión: ' + (data.message || 'Error desconocido'));
                    }).catch(() => {
                        alert('Error al cerrar sesión: respuesta no válida del servidor');
                    });
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('Error al cerrar sesión: ' + error.message);
            });
        });
    }
});
