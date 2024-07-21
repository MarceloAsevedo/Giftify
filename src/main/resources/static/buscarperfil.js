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
                    `;
                    resultDiv.addEventListener('click', () => {
                        window.location.href = `otroperfil.html?id=${perfil.idPerfil}`;
                    });
                    searchResultsDiv.appendChild(resultDiv);
                }
            });
        });
    } else {
        document.getElementById('searchResults').innerHTML = '';
    }
});

