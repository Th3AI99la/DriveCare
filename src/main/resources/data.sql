-- =================================================================
--  SCRIPT GIGANTE PARA POPULAR O BANCO DE DADOS DRIVECARE
-- =================================================================

-- Deleta dados existentes para uma carga limpa (USAR APENAS EM DESENVOLVIMENTO)
-- DELETE FROM manutencao_realizada;
-- DELETE FROM agendamento_manutencao;
-- DELETE FROM vehicle;
-- DELETE FROM users;

-- Resetando as sequências de ID para evitar conflitos ao reinserir (específico do PostgreSQL)
-- ALTER SEQUENCE users_id_seq RESTART WITH 1;
-- ALTER SEQUENCE vehicle_id_seq RESTART WITH 1;
-- ALTER SEQUENCE agendamento_manutencao_id_seq RESTART WITH 1;
-- ALTER SEQUENCE manutencao_realizada_id_seq RESTART WITH 1;

--  VEÍCULOS
-- =========
-- Assumindo que a tabela 'vehicle' tem uma coluna 'user_id'
INSERT INTO vehicle (id, user_id, marca, modelo, ano, placa, quilometragem, vehicle_color) VALUES
(1, 1, 'Fiat', 'Strada', 2023, 'RST1A23', 35000, 'Branca'),
(2, 1, 'Yamaha', 'Lander 250', 2022, 'MOT0B45', 28000, 'Azul'),
(3, 2, 'Jeep', 'Compass', 2024, 'JEP2C67', 12000, 'Grafite'),
(4, 2, 'Hyundai', 'HB20', 2021, 'HYU3D89', 62000, 'Prata'),
(5, 3, 'Renault', 'Kwid', 2022, 'REN4E10', 55000, 'Laranja'),
(6, 4, 'Chevrolet', 'Tracker', 2023, 'TRK5F31', 25000, 'Preto'),
(7, 4, 'Honda', 'PCX 150', 2024, 'PCX6G52', 8000, 'Prata'),
(8, 1, 'Volkswagen', 'T-Cross', 2021, 'VWT7H73', 88000, 'Cinza'),
(9, 2, 'Fiat', 'Toro', 2022, 'TOR8I94', 71000, 'Vinho'),
(10, 3, 'BMW', 'G 310 GS', 2021, 'BMW9J15', 41000, 'Branca e Azul');

-- ==========================
--  AGENDAMENTO DE MANUTENÇÃO
-- ==========================
INSERT INTO agendamento_manutencao (id, veiculo_id, tipo_categoria, descricao, orcamento, creation_date, data_agendamento, status_agendamento) VALUES
-- Fiat Strada (veiculo_id = 1)
(1, 1, 'REVISAO', 'Revisão de 30.000km', 850.00, '2025-07-15', '2025-08-01', 'CONCLUIDA'),
(2, 1, 'PREVENTIVA', 'Troca da correia dentada', 600.00, '2025-09-01', '2025-09-15', 'AGENDADA'),

-- Yamaha Lander (veiculo_id = 2)
(3, 2, 'REVISAO', 'Revisão de 25.000km', 350.00, '2025-06-20', '2025-07-10', 'CONCLUIDA'),
(4, 2, 'CORRETIVA', 'Substituir pneu traseiro', 450.00, '2025-08-28', '2025-09-05', 'CONCLUIDA'),
(5, 2, 'PREVENTIVA', 'Lubrificação geral e reaperto', 120.00, '2025-09-02', '2025-09-12', 'AGENDADA'),

-- Jeep Compass (veiculo_id = 3)
(6, 3, 'REVISAO', 'Primeira revisão - 10.000km', 950.00, '2025-08-18', '2025-09-02', 'CONCLUIDA'),
(7, 3, 'PREVENTIVA', 'Cristalização dos vidros', 250.00, '2025-09-10', '2025-09-25', 'AGENDADA'),

-- Hyundai HB20 (veiculo_id = 4)
(8, 4, 'REVISAO', 'Revisão de 60.000km', 1200.00, '2025-07-25', '2025-08-15', 'CONCLUIDA'),
(9, 4, 'CORRETIVA', 'Troca de amortecedores dianteiros', 980.00, '2025-08-16', '2025-09-18', 'AGENDADA'),
(10, 4, 'CORRETIVA', 'Reparo no ar-condicionado', 450.00, '2025-08-20', '2025-09-01', 'CANCELADA'),

-- Renault Kwid (veiculo_id = 5)
(11, 5, 'REVISAO', 'Revisão de 50.000km', 750.00, '2025-08-05', '2025-08-22', 'CONCLUIDA'),

-- Chevrolet Tracker (veiculo_id = 6)
(12, 6, 'REVISAO', 'Revisão de 20.000km', 900.00, '2025-08-11', '2025-08-29', 'CONCLUIDA'),
(13, 6, 'PREVENTIVA', 'Higienização do ar-condicionado', 180.00, '2025-09-03', '2025-09-20', 'AGENDADA'),

-- Honda PCX (veiculo_id = 7)
(14, 7, 'REVISAO', 'Revisão de 5.000km', 280.00, '2025-08-25', '2025-09-08', 'CONCLUIDA'),

-- VW T-Cross (veiculo_id = 8)
(15, 8, 'CORRETIVA', 'Troca de bateria', 550.00, '2025-08-30', '2025-09-04', 'CONCLUIDA'),
(16, 8, 'REVISAO', 'Revisão de 90.000km', 1500.00, '2025-09-05', '2025-10-01', 'AGENDADA'),

-- Fiat Toro (veiculo_id = 9)
(17, 9, 'REVISAO', 'Revisão de 70.000km', 1100.00, '2025-09-01', '2025-09-26', 'AGENDADA'),

-- BMW G 310 GS (veiculo_id = 10)
(18, 10, 'REVISAO', 'Revisão de 40.000km', 950.00, '2025-08-14', '2025-09-03', 'CONCLUIDA');

-- ======================
--  MANUTENÇÃO REALIZADA
-- ======================
INSERT INTO manutencao_realizada (id, veiculo_id, manutencao_agendada_id, data_execucao, descricao_servico_realizado, tipo_manutencao, custo_real) VALUES
-- Agendamento ID 1 (Fiat Strada)
(1, 1, 1, '2025-08-01', 'Revisão completa realizada. Troca de óleo, filtros de ar e combustível.', 'REVISAO', 875.50),
-- Agendamento ID 3 (Yamaha Lander)
(2, 2, 3, '2025-07-10', 'Revisão de 25k km, troca de óleo e lubrificação da corrente.', 'REVISAO', 340.00),
-- Agendamento ID 4 (Yamaha Lander)
(3, 2, 4, '2025-09-05', 'Pneu traseiro Metzeler Tourance substituído.', 'CORRETIVA', 480.00),
-- Agendamento ID 6 (Jeep Compass)
(4, 3, 6, '2025-09-02', 'Revisão inicial conforme manual do proprietário.', 'REVISAO', 950.00),
-- Agendamento ID 8 (Hyundai HB20)
(5, 4, 8, '2025-08-15', 'Revisão geral, troca de velas e limpeza de bicos injetores.', 'REVISAO', 1350.00),
-- Agendamento ID 11 (Renault Kwid)
(6, 5, 11, '2025-08-22', 'Revisão completa, incluindo troca de fluido de freio.', 'REVISAO', 780.00),
-- Agendamento ID 12 (Chevrolet Tracker)
(7, 6, 12, '2025-08-29', 'Revisão de 20k km realizada em concessionária.', 'REVISAO', 920.00),
-- Agendamento ID 14 (Honda PCX)
(8, 7, 14, '2025-09-08', 'Revisão e troca de óleo CVT.', 'REVISAO', 275.00),
-- Agendamento ID 15 (VW T-Cross)
(9, 8, 15, '2025-09-04', 'Bateria Moura substituída.', 'CORRETIVA', 580.00),
-- Agendamento ID 18 (BMW G 310 GS)
(10, 10, 18, '2025-09-03', 'Revisão completa, com troca de óleo Motul e verificação de fluidos.', 'REVISAO', 1100.00);