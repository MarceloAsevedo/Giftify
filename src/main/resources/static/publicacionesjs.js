 document.addEventListener("DOMContentLoaded", function() {
      fetch('http://localhost:8082/publicaciones/todas')
        .then(response => {
          if (!response.ok) {
            throw new Error('Error en la respuesta de la red');
          }
          return response.json();
        })
        .then(data => {
          console.log('Datos recibidos:', data); // Log para verificar los datos recibidos
          const publicacionesDiv = document.getElementById('publicaciones');
          if (data.length === 0) {
            publicacionesDiv.innerHTML = '<p>No hay publicaciones disponibles.</p>';
            return;
          }
          data.forEach(publicacion => {
            const pubDiv = document.createElement('div');
            pubDiv.className = 'publicacion';
            pubDiv.innerHTML = `
              <div class="perfil-info">
                ${publicacion.fotoPerfil ? `<img src="${publicacion.fotoPerfil}" alt="Foto de perfil">` : ''}
                <p>${publicacion.nombrePerfil} ${publicacion.apellidoPerfil}</p>
              </div>
              <div class="publicacion-descripcion">
                <p>${publicacion.descripcionPublicacion}</p>
              </div>
              ${publicacion.fotoPublicacion ? `
                <div class="publicacion-foto">
                  <img src="${publicacion.fotoPublicacion}" alt="Foto de la publicación">
                </div>` : ''}
              <div class="publicacion-fecha">
                <p>${new Date(publicacion.fechaHoraPublicado).toLocaleString()}</p>
              </div>
            `;
            publicacionesDiv.appendChild(pubDiv);
          });
        })
        .catch(error => {
          console.error('Error al obtener publicaciones:', error);
          const publicacionesDiv = document.getElementById('publicaciones');
          publicacionesDiv.innerHTML = '<p>Error al cargar publicaciones. Inténtalo más tarde.</p>';
        });
    });