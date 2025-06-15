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


  document.addEventListener('DOMContentLoaded', function() {
    
    // --- LÓGICA PARA O CAMPO FOTO DE PERFIL ---
    const photoInput = document.getElementById('profilePhoto');
    const photoPreview = document.getElementById('photoPreview');

    if (photoInput) {
        photoInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (!file) return;

            // ... (lógica de validação e preview da foto que já existe) ...
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

    // LÓGICA CAMPO TELEFONE ---
    const phoneInput = document.getElementById('phoneNumber'); // Procura pelo ID do HTML
    if(phoneInput) {
        // Este evento 'input' é acionado a cada tecla digitada
        phoneInput.addEventListener('input', function (e) {
            // Pega o valor e remove tudo que não for número
            let value = e.target.value.replace(/\D/g, '');
            
            // Limita o tamanho para 11 dígitos
            value = value.substring(0, 11);
            
            // Aplica a formatação dinamicamente
            if (value.length > 6) {
                value = `(${value.substring(0, 2)}) ${value.substring(2, 7)}-${value.substring(7)}`;
            } else if (value.length > 2) {
                value = `(${value.substring(0, 2)}) ${value.substring(2)}`;
            } else if (value.length > 0) {
                value = `(${value}`;
            }
            
            // Devolve o valor formatado para o campo
            e.target.value = value;
        });
    }
});
});