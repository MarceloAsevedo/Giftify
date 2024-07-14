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
            document.querySelector('.descripcionPerfil').textContent = data.descripcion;
            document.querySelector('.linkPerfil').textContent = data.link;
            document.querySelector('.linkPerfil').href = data.link;
        })
        .catch(error => {
            console.error('Error:', error);
        });

    document.getElementById('editarForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const form = event.target;
        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const fechaNacimiento = document.getElementById('fechanacimiento').value;
        const fotoPerfil = document.getElementById('fotoPerfil').files[0];
        const descripcion = document.getElementById('descripcion').value;
        const link = document.getElementById('link').value;

        const formData = new FormData();
        formData.append('nombre', nombre);
        formData.append('apellido', apellido);
        formData.append('fechaNacimiento', fechaNacimiento);
        formData.append('link', link);
        formData.append('descripcion', descripcion);
        if (fotoPerfil) {
            formData.append('fotoPerfil', fotoPerfil);
        }

        fetch(`/perfiles/editarperfil/${id}`, {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok) {
                form.reset(); // Limpia el formulario
                alert('Perfil editado correctamente');
                window.location.href = 'perfil.html'; // Redirecciona a perfil.html
            } else {
                response.text().then(text => {
                    alert(text); // Muestra el mensaje de error
                });
            }
        }).catch(error => {
            console.error('Error al editar el perfil:', error);
            alert('Ocurrió un error al intentar editar el perfil');
        });
    });
});
