document.addEventListener('DOMContentLoaded', function() {
    const usuarioStr = sessionStorage.getItem('usuario');
    if (!usuarioStr) {
        alert('No se encontró el objeto de usuario en sessionStorage');
        return;
    }

    const usuario = JSON.parse(usuarioStr);
    const id = usuario.id;

    if (!id) {
        alert('No se encontró el ID del perfil en sessionStorage');
        return;
    }

    fetch(`http://localhost:8082/perfiles/perfil/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los datos del perfil');
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos del perfil:', data); // Depuración
            document.querySelector('.profile-img').src = data.fotoPerfil;
            document.querySelector('.profile .nombreCompleto').textContent = `${data.nombre} ${data.apellido}`;
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
