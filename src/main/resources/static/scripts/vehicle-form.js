document.addEventListener('DOMContentLoaded', function() {
    // --- LÓGICA PARA O CAMPO ANO ---
    const anoInput = document.getElementById('ano');
    if (anoInput) {
        // Este evento é acionado a cada tecla digitada
        anoInput.addEventListener('input', function(e) {
            // Se o comprimento do valor inserido for maior que 4...
            if (e.target.value.length > 4) {
                // ...corta o valor para manter apenas os primeiros 4 dígitos.
                e.target.value = e.target.value.slice(0, 4);
            }
        });
    }

    // --- FORMATAÇÃO DO CAMPO PLACA ---
    const placaInput = document.getElementById('placa');
    if (placaInput) {
        // Converte o texto para maiúsculas enquanto o usuário digita
        placaInput.addEventListener('input', function(e) {
            e.target.value = e.target.value.toUpperCase();
        });
    }

    // --- FORMATAÇÃO DO CAMPO QUILOMETRAGEM ---
    const kmInput = document.getElementById('quilometragem');
    if (kmInput) {
        kmInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, ''); // Remove tudo que não for dígito
            if (value) {
                value = new Intl.NumberFormat('pt-BR').format(value);
            }
            e.target.value = value;
        });
    }

    // --- LIMPEZA ANTES DE ENVIAR O FORMULÁRIO (PARTE MAIS IMPORTANTE) ---
    const vehicleForm = document.getElementById('vehicleForm');
    if (vehicleForm) {
        vehicleForm.addEventListener('submit', function(e) {
            if (kmInput) {
                // Isso garante que o backend receberá "88290" em vez de "88.290"
                const valorNumerico = kmInput.value.replace(/\D/g, '');
                kmInput.value = valorNumerico;
            }
        });
    }
});