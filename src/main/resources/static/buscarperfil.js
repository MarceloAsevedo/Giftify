document.getElementById('searchInput').addEventListener('input', function() {
    const usuarioStr = sessionStorage.getItem('usuario');
    if (!usuarioStr) {
        alert('No se encontró el objeto de usuario en sessionStorage');
        return;
    }

    const usuario = JSON.parse(usuarioStr);
    const perfilId = usuario.id;

    if (!perfilId) {
        alert('No se encontró el ID del perfil en sessionStorage');
        return;
    }

    const query = this.value;
    if (query.length > 0) {
        fetch(`http://localhost:8082/perfil/buscar?query=${query}&perfilId=${perfilId}`)
        .then(response => response.json())
        .then(data => {
            const searchResultsDiv = document.getElementById('searchResults');
            searchResultsDiv.innerHTML = '';
            data.forEach(perfil => {
                if (perfil.idPerfil !== perfilId) {
                    const resultDiv = document.createElement('div');
                    resultDiv.className = 'search-result';
                    resultDiv.innerHTML = `
                        <img src="${perfil.fotoPerfil}" alt="Foto de perfil">
                        <div class="profile-info">
                            <p>${perfil.nombre} ${perfil.apellido}</p>
                        </div>
                        <button class="add-friend-button" onclick="agregarAmigo(${perfil.idPerfil}, this)">Agregar amigo</button>
                    `;
                    searchResultsDiv.appendChild(resultDiv);
                }
            });
        });
    } else {
        document.getElementById('searchResults').innerHTML = '';
    }
});

function agregarAmigo(amigoId, button) {
    const usuarioStr = sessionStorage.getItem('usuario');
    const usuario = JSON.parse(usuarioStr);
    const perfilId = usuario.id;

    fetch(`http://localhost:8082/perfil/${perfilId}/amigos`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `amigoId=${amigoId}`
    })
    .then(response => {
        if (response.ok) {
            alert('Amigo agregado con éxito');
            button.disabled = true;
        } else {
            alert('Error al agregar amigo');
        }
    });
}
