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
    const kmDisplayInput = document.getElementById('quilometragem_display'); // Campo visível
    const kmHiddenInput = document.getElementById('quilometragem');         // Campo oculto

    if (kmDisplayInput && kmHiddenInput) {
        
        kmDisplayInput.addEventListener('input', function(e) {
            // Pega o valor digitado e remove tudo que não for número
            const valorPuro = e.target.value.replace(/\D/g, '');

            // Atualiza o valor do campo OCULTO com o número puro
            kmHiddenInput.value = valorPuro;

            // Formata o número para exibição no campo VISÍVEL
            if (valorPuro) {
                const valorFormatado = new Intl.NumberFormat('pt-BR').format(valorPuro);
                e.target.value = valorFormatado;
            } else {
                e.target.value = ''; // Limpa se o usuário apagar tudo
            }
        });
    }

    // --- FORMATAÇÃO DO CAMPO COR ---
     const corInput = document.getElementById('cor');
    if (corInput) {
        corInput.addEventListener('input', function(e) {
            let value = e.target.value;
            
            // 1. Remove todos os caracteres que não são letras ou espaços
            value = value.replace(/[^a-zA-Z\s]/g, '');

            // 2. Capitaliza a primeira letra e força o resto para minúsculas
            if (value.length > 0) {
                value = value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
            }

            // 3. Atualiza o valor no campo
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