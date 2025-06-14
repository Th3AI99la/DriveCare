package com.drivecare.project.controller;

import com.drivecare.project.model.Vehicle;
import com.drivecare.project.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;
    private static final int ITENS_POR_PAGINA = 5; // Constante de itens por página

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Método GET para exibir a lista de veículos com paginação
    @GetMapping("/vehicles")
    public String listarVeiculos(@RequestParam(name = "pagina", defaultValue = "1") int paginaAtualParam, Model model) {

        // Lógica de paginação
        int paginaAtual = Math.max(1, paginaAtualParam);
        Page<Vehicle> paginaDeVeiculos = vehicleService.getVeiculosPaginados(paginaAtual - 1, ITENS_POR_PAGINA);

        // Dados para exibição na tela
        model.addAttribute("veiculos", paginaDeVeiculos.getContent());
        model.addAttribute("totalContagem", paginaDeVeiculos.getTotalElements());
        model.addAttribute("contagemAtual", paginaDeVeiculos.getNumberOfElements());
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", paginaDeVeiculos.getTotalPages() == 0 ? 1 : paginaDeVeiculos.getTotalPages());

        return "vehicles"; // Página de listagem de veículos
    }

    // Método GET para mostrar a página de cadastro de veículos
    @GetMapping("/vehicle/cadastrar")
    public String mostrarTelaCadastroVeiculo(Model model) {
        model.addAttribute("vehicle", new Vehicle()); // Adiciona um veículo vazio para o formulário
        return "vehicle/cadastrar";  // Página de cadastro de veículo
    }

    // Método POST para processar o cadastro de um novo veículo
    @PostMapping("/vehicle/cadastrar")
    public String cadastrarVeiculo(Vehicle vehicle, Model model) {
        try {
            vehicleService.salvarVeiculo(vehicle);  // Salva o veículo no banco
            model.addAttribute("message", "Veículo cadastrado com sucesso!"); // Mensagem de sucesso
            model.addAttribute("vehicle", new Vehicle()); // Limpa o formulário
        } catch (Exception e) {
            model.addAttribute("message", "Erro ao cadastrar o veículo."); // Mensagem de erro
        }
        return "vehicle/cadastrar";  // Retorna para a página de cadastro
    }

    // Método GET para exibir os detalhes de um veículo
    @GetMapping("/vehicle/{id}")
    public String exibirDetalhesVeiculo(@PathVariable("id") Long id, Model model) {
        Vehicle veiculo = vehicleService.buscarVeiculoPorId(id); // Busca o veículo pelo ID
        model.addAttribute("veiculo", veiculo); // Adiciona o veículo no modelo
        return "vehicle/detalhes";  // Página de detalhes do veículo
    }
}
