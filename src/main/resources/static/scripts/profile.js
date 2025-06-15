document.addEventListener('DOMContentLoaded', function() {
  const photoInput = document.getElementById('profilePhoto');
  const photoPreview = document.getElementById('photoPreview');

  if (photoInput) {
    photoInput.addEventListener('change', function(event) {
      const file = event.target.files[0];
      if (!file) return;

      const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
      if (!validTypes.includes(file.type)) {
        alert('Tipo de arquivo inválido. Por favor, envie um JPG, PNG ou GIF.');
        photoInput.value = '';
        return;
      }

      const reader = new FileReader();
      reader.onload = function(e) {
        photoPreview.innerHTML = '';
        const img = document.createElement('img');
        img.src = e.target.result;
        img.alt = 'Pré-visualização da foto do perfil';
        photoPreview.appendChild(img);
      };
      reader.readAsDataURL(file);
    });
  }
});