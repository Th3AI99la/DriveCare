document.addEventListener('DOMContentLoaded', function() {
    // Função para animar a entrada dos cards de veículos
    function animateVehicleCards() {
        const vehicleCards = document.querySelectorAll('.animate-card');

        if (vehicleCards.length === 0) {
            return; 
        }

        vehicleCards.forEach((card, index) => {
            // Define o estado inicial (invisível e ligeiramente menor)
            card.style.opacity = '0';
            card.style.transform = 'scale(0.95)';
            card.style.transition = 'opacity 0.4s ease-out, transform 0.4s ease-out';

            // Aplica o estado final com um atraso escalonado
            setTimeout(() => {
                card.style.opacity = '1';
                card.style.transform = 'scale(1)';
            }, index * 75); // Atraso de 75ms entre cada card
        });
    }

    // Executa a animação assim que a página for carregada
    animateVehicleCards();
});