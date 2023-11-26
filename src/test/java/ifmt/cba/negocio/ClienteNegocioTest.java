package ifmt.cba.negocio;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ifmt.cba.dto.ClienteDTO;
import ifmt.cba.entity.Cliente;
import ifmt.cba.persistencia.ClienteDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class ClienteNegocioTest {

    private ClienteDAO clienteDAOMock;
    private ClienteNegocio clienteNegocio;

    @Before
    public void setUp() {
        clienteDAOMock = mock(ClienteDAO.class);
        clienteNegocio = new ClienteNegocio(clienteDAOMock);
    }

    @Test
    public void testInserirClienteComSucesso() throws NegocioException, PersistenciaException {
        // Crie um ClienteDTO válido
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("Nome válido");
        // Configurar o mock para simular que não existe cliente com o CPF fornecido
        when(clienteDAOMock.buscarPorCPF(anyString())).thenReturn(null);

        // Chamar o método inserir da classe de negócios
        clienteNegocio.inserir(clienteDTO);

        // Verificar se o método de persistência foi chamado corretamente
        verify(clienteDAOMock, times(1)).beginTransaction();
        verify(clienteDAOMock, times(1)).incluir(any(Cliente.class));
        verify(clienteDAOMock, times(1)).commitTransaction();
    }

    @Test(expected = NegocioException.class)
    public void testInserirClienteComCPFExistente() throws NegocioException, PersistenciaException {
        // Crie um ClienteDTO com CPF existente
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCPF("CPF existente");
        // Configurar o mock para simular que já existe cliente com o CPF fornecido
        when(clienteDAOMock.buscarPorCPF(anyString())).thenReturn(new Cliente());

        // Chamar o método inserir da classe de negócios
        clienteNegocio.inserir(clienteDTO);

        // Verificar se o método de persistência não foi chamado neste caso
        verify(clienteDAOMock, never()).beginTransaction();
        verify(clienteDAOMock, never()).incluir(any(Cliente.class));
        verify(clienteDAOMock, never()).commitTransaction();
    }

    @Test
    public void testAlterarClienteComSucesso() throws NegocioException, PersistenciaException {
        // Crie um ClienteDTO válido
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCodigo(1);
        clienteDTO.setNome("Nome válido");
        // Configurar o mock para simular que existe cliente com o código fornecido
        when(clienteDAOMock.buscarPorCodigo(anyInt())).thenReturn(new Cliente());

        // Chamar o método alterar da classe de negócios
        clienteNegocio.alterar(clienteDTO);

        // Verificar se o método de persistência foi chamado corretamente
        verify(clienteDAOMock, times(1)).beginTransaction();
        verify(clienteDAOMock, times(1)).alterar(any(Cliente.class));
        verify(clienteDAOMock, times(1)).commitTransaction();
    }

    @Test(expected = NegocioException.class)
    public void testAlterarClienteInexistente() throws NegocioException, PersistenciaException {
        // Crie um ClienteDTO com um código que não existe
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCodigo(1);
        // Configurar o mock para simular que não existe cliente com o código fornecido
        when(clienteDAOMock.buscarPorCodigo(anyInt())).thenReturn(null);

        // Chamar o método alterar da classe de negócios
        clienteNegocio.alterar(clienteDTO);

        // Verificar se o método de persistência não foi chamado neste caso
        verify(clienteDAOMock, never()).beginTransaction();
        verify(clienteDAOMock, never()).alterar(any(Cliente.class));
        verify(clienteDAOMock, never()).commitTransaction();
    }
}
