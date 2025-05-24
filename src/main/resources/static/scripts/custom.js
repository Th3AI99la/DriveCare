document.addEventListener('DOMContentLoaded', function() {
  // Animação de entrada dos cards
  const cards = document.querySelectorAll('.animate-card');
  cards.forEach((card, index) => {
    card.style.opacity = '0';
    card.style.transform = 'translateY(20px)';
    card.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
    
    setTimeout(() => {
      card.style.opacity = '1';
      card.style.transform = 'translateY(0)';
    }, 100 * index);
  });

  // Configuração dos gráficos
  renderCharts();
  
  // Adiciona classe de animação ao passar o mouse nos links
  const navLinks = document.querySelectorAll('.nav-link');
  navLinks.forEach(link => {
    link.addEventListener('mouseenter', () => {
      link.classList.add('animate__animated', 'animate__pulse');
    });
    
    link.addEventListener('mouseleave', () => {
      link.classList.remove('animate__animated', 'animate__pulse');
    });
  });
});

function renderCharts() {
  // Gráfico de Linha - Status dos Veículos
  const lineCtx = document.getElementById('lineChart').getContext('2d');
  new Chart(lineCtx, {
    type: 'line',
    data: {
      labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
      datasets: [{
        label: 'Veículos em Dia',
        data: [5, 8, 7, 10, 12, 15],
        borderColor: '#4CAF50',
        backgroundColor: 'rgba(76, 175, 80, 0.1)',
        tension: 0.3,
        fill: true
      }, {
        label: 'Veículos com Pendências',
        data: [2, 3, 1, 4, 2, 3],
        borderColor: '#FFC107',
        backgroundColor: 'rgba(255, 193, 7, 0.1)',
        tension: 0.3,
        fill: true
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: {
            color: '#e0e0e0'
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            color: 'rgba(255, 255, 255, 0.1)'
          },
          ticks: {
            color: '#e0e0e0'
          }
        },
        x: {
          grid: {
            color: 'rgba(255, 255, 255, 0.1)'
          },
          ticks: {
            color: '#e0e0e0'
          }
        }
      }
    }
  });

  // Gráfico de Donut - Tipos de Manutenção
  const donutCtx = document.getElementById('donutChart').getContext('2d');
  new Chart(donutCtx, {
    type: 'doughnut',
    data: {
      labels: ['Preventiva', 'Corretiva', 'Revisão', 'Emergencial'],
      datasets: [{
        data: [45, 25, 20, 10],
        backgroundColor: [
          '#4CAF50',
          '#FF5722',
          '#2196F3',
          '#FFC107'
        ],
        borderColor: '#1e1e1e',
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '70%',
      plugins: {
        legend: {
          position: 'right',
          labels: {
            color: '#e0e0e0'
          }
        }
      },
      animation: {
        animateScale: true,
        animateRotate: true
      }
    }
  });

  // Gráfico de Barras - Saúde dos Veículos
  const barCtx = document.getElementById('barChart').getContext('2d');
  new Chart(barCtx, {
    type: 'bar',
    data: {
      labels: ['Veículo A', 'Veículo B', 'Veículo C', 'Veículo D', 'Veículo E'],
      datasets: [{
        label: 'Saúde (%)',
        data: [92, 75, 88, 60, 95],
        backgroundColor: [
          'rgba(54, 162, 235, 0.7)',
          'rgba(54, 162, 235, 0.7)',
          'rgba(54, 162, 235, 0.7)',
          'rgba(54, 162, 235, 0.7)',
          'rgba(54, 162, 235, 0.7)'
        ],
        borderColor: [
          'rgba(54, 162, 235, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(54, 162, 235, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          max: 100,
          grid: {
            color: 'rgba(255, 255, 255, 0.1)'
          },
          ticks: {
            color: '#e0e0e0'
          }
        },
        x: {
          grid: {
            display: false
          },
          ticks: {
            color: '#e0e0e0'
          }
        }
      },
      plugins: {
        legend: {
          display: false
        }
      },
      animation: {
        duration: 2000,
        easing: 'easeOutQuart'
      }
    }
  });
}

// <!-- PRÓXIMAS MANUTENÇÕES -->
function initTableAnimations() {
  const tableRows = document.querySelectorAll('.animate-row');
  
  tableRows.forEach((row, index) => {
    row.style.opacity = '0';
    row.style.transform = 'translateX(-20px)';
    
    setTimeout(() => {
      row.style.transition = 'all 0.5s ease';
      row.style.opacity = '1';
      row.style.transform = 'translateX(0)';
    }, 100 * index);
    
    // Efeito de hover dinâmico baseado no status
    const badge = row.querySelector('.badge');
    if (badge) {
      row.addEventListener('mouseenter', () => {
        if (badge.classList.contains('bg-danger')) {
          row.style.boxShadow = 'inset 4px 0 0 #dc3545';
        } else if (badge.classList.contains('bg-warning')) {
          row.style.boxShadow = 'inset 4px 0 0 #ffc107';
        } else {
          row.style.boxShadow = 'inset 4px 0 0 #0d6efd';
        }
      });
      
      row.addEventListener('mouseleave', () => {
        row.style.boxShadow = 'none';
      });
    }
  });
}

// Chame esta função no DOMContentLoaded
document.addEventListener('DOMContentLoaded', function() {
  // ... outros códigos existentes ...
  initTableAnimations();
});