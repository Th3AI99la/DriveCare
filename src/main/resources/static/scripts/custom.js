document.addEventListener('DOMContentLoaded', function() {
  // Animação de entrada dos cards

  animateCardsOnLoad();
  
  renderStaticCharts(); // Renomeado para clareza
  
  // Inicializa animações de tabela, se a função existir
  if (typeof initTableAnimations === 'function') {
    initTableAnimations();
  }
});

function animateCardsOnLoad() {
  const cards = document.querySelectorAll('.animate-card');
  cards.forEach((card, index) => {
    card.style.opacity = '0';
    card.style.transform = 'translateY(20px)';
    // A transição pode ser definida no CSS para melhor organização
    card.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
    
    setTimeout(() => {
      card.style.opacity = '1';
      card.style.transform = 'translateY(0)';
    }, 100 * index); // escalonamento da animação
  });
}

// // Esta função parece estar correta para animar as linhas da tabela.
function initTableAnimations() {
  const tableRows = document.querySelectorAll('.table .animate-row'); // Tornando o seletor mais específico
  
  tableRows.forEach((row, index) => {
    row.style.opacity = '0';
    row.style.transform = 'translateX(-20px)';
    // A transição pode ser definida no CSS para melhor organização
    row.style.transition = 'opacity 0.4s ease-out, transform 0.4s ease-out';
    
    setTimeout(() => {
      row.style.opacity = '1';
      row.style.transform = 'translateX(0)';
    }, 50 * index + 200); // Atraso inicial e escalonamento
    
    // Efeito de hover dinâmico baseado no status do badge
    const badge = row.querySelector('.badge');
    if (badge) {
      // Estas classes de box-shadow podem ser adicionadas/removidas via JS
      // ou definidas como classes CSS e alternadas.
      row.addEventListener('mouseenter', () => {
        if (badge.classList.contains('bg-danger')) {
          row.style.boxShadow = 'inset 4px 0 0 #dc3545'; // Vermelho para danger
        } else if (badge.classList.contains('bg-warning')) {
          row.style.boxShadow = 'inset 4px 0 0 #ffc107'; // Amarelo para warning
        } else if (badge.classList.contains('bg-success')) {
          row.style.boxShadow = 'inset 4px 0 0 #198754'; // Verde para success
        } else {
          row.style.boxShadow = 'inset 4px 0 0 #0d6efd'; // Azul padrão
        }
      });
      
      row.addEventListener('mouseleave', () => {
        row.style.boxShadow = 'none';
      });
    }
  });
}

