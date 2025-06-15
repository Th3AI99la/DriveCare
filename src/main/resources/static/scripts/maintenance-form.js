document.addEventListener('DOMContentLoaded', function() {
    
    // --- LÓGICA CORRIGIDA PARA FORMATAÇÃO DE ORÇAMENTO ---
    const orcamentoDisplayInput = document.getElementById('orcamento_display'); // Campo visível
    const orcamentoHiddenInput = document.getElementById('orcamento');         // Campo oculto

    if (orcamentoDisplayInput && orcamentoHiddenInput) {
        
        orcamentoDisplayInput.addEventListener('input', function(e) {
            // 1. Pega o valor e remove tudo que não for dígito
            let valorPuro = e.target.value.replace(/\D/g, '');

            if (valorPuro) {
                // 2. Converte o valor para número e divide por 100 para tratar os centavos
                const valorDecimal = parseFloat(valorPuro) / 100;

                // 3. Atualiza o campo OCULTO com o valor decimal correto (ex: 1000.00)
                orcamentoHiddenInput.value = valorDecimal.toFixed(2);

                // 4. Formata o valor decimal como moeda para exibição no campo VISÍVEL (ex: R$ 1.000,00)
                const valorFormatado = new Intl.NumberFormat('pt-BR', {
                    style: 'currency',
                    currency: 'BRL'
                }).format(valorDecimal);
                e.target.value = valorFormatado;

            } else {
                // Limpa os dois campos se o usuário apagar tudo
                e.target.value = '';
                orcamentoHiddenInput.value = '';
            }
        });

        // Garante que o valor inicial (em caso de edição) seja formatado corretamente ao carregar a página
        if (orcamentoHiddenInput.value) {
            const event = new Event('input');
            orcamentoDisplayInput.value = (parseFloat(orcamentoHiddenInput.value) * 100).toString();
            orcamentoDisplayInput.dispatchEvent(event);
        }
    }

    // --- FILTRO DE VEÍCULOS ---
    const searchInput = document.getElementById('veiculoSearchInput');
    const vehicleSelect = document.getElementById('veiculo');

    if (searchInput && vehicleSelect) {
        searchInput.addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const options = vehicleSelect.options;

            for (let i = 0; i < options.length; i++) {
                const option = options[i];
                if (option.value === "") continue;

                const optionText = option.text.toLowerCase();
                option.style.display = optionText.includes(searchTerm) ? '' : 'none';
            }
        });
    }
});