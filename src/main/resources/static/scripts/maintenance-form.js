document.addEventListener('DOMContentLoaded', function() {

    const orcamentoDisplayInput = document.getElementById('orcamento_display'); // Campo visível
    const orcamentoHiddenInput = document.getElementById('orcamento');         // Campo oculto

    if (orcamentoDisplayInput && orcamentoHiddenInput) {

        // Formata o valor enquanto o usuário digita
        orcamentoDisplayInput.addEventListener('input', function(e) {
            // 1. Pega o valor e remove tudo que não for dígito
            let valorPuro = e.target.value.replace(/\D/g, '');

            // 2. Atualiza o campo oculto com o valor em centavos (ex: 12345)
            orcamentoHiddenInput.value = valorPuro;

            // 3. Formata o número para exibição como moeda (R$ 1.234,56)
            if (valorPuro) {
                // Converte o valor para número e divide por 100 para tratar os centavos
                const valorNumerico = parseFloat(valorPuro) / 100;
                // Formata como moeda brasileira
                const valorFormatado = new Intl.NumberFormat('pt-BR', {
                    style: 'currency',
                    currency: 'BRL'
                }).format(valorNumerico);
                e.target.value = valorFormatado;
            } else {
                e.target.value = ''; // Limpa se o usuário apagar tudo
            }
        });

        // Garante que o valor inicial (em caso de edição) seja formatado
        if (orcamentoHiddenInput.value) {
            const event = new Event('input');
            orcamentoDisplayInput.value = orcamentoHiddenInput.value;
            orcamentoDisplayInput.dispatchEvent(event);
        }
    }
});