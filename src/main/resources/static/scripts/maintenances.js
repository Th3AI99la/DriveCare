document.addEventListener('DOMContentLoaded', function() {
    // Seleciona todas as linhas da tabela que têm a classe 'animate-row'
    const tableRows = document.querySelectorAll('.table tbody tr');

    tableRows.forEach((row, index) => {
        // Define o estado inicial da animação (invisível e ligeiramente abaixo)
        row.style.opacity = '0';
        row.style.transform = 'translateY(10px)';
        row.style.transition = 'opacity 0.4s ease-out, transform 0.4s ease-out';

        // Aplica o estado final com um pequeno atraso escalonado
        setTimeout(() => {
            row.style.opacity = '1';
            row.style.transform = 'translateY(0)';
        }, index * 50); // Atraso de 50ms entre cada linha
    });
});