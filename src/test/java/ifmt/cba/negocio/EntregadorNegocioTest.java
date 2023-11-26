package ifmt.cba.negocio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


import org.junit.Before;
import org.junit.Test;

import ifmt.cba.dto.EntregadorDTO;
import ifmt.cba.entity.Entregador;
import ifmt.cba.persistencia.EntregadorDAO;
import ifmt.cba.persistencia.PersistenciaException;

public class EntregadorNegocioTest {

    private EntregadorDAO entregadorDAOMock;
    private EntregadorNegocio entregadorNegocio;

    @Before
    public void setUp() {
        entregadorDAOMock = mock(EntregadorDAO.class);
        entregadorNegocio = new EntregadorNegocio(entregadorDAOMock);
    }

    @Test
    public void testInserirEntregadorComSucesso() throws NegocioException, PersistenciaException {
        // Crie um EntregadorDTO válido
        EntregadorDTO entregadorDTO = new EntregadorDTO();
        entregadorDTO.setNome("Nome válido");
        // Configurar o mock para simular que não existe entregador com o CPF fornecido
        when(entregadorDAOMock.buscarPorCPF(anyString())).thenReturn(null);

        // Chamar o método inserir da classe de negócios
        entregadorNegocio.inserir(entregadorDTO);

        // Verificar se o método de persistência foi chamado corretamente
        verify(entregadorDAOMock, times(1)).beginTransaction();
        verify(entregadorDAOMock, times(1)).incluir(any(Entregador.class));
        verify(entregadorDAOMock, times(1)).commitTransaction();
    }

    @Test(expected = NegocioException.class)
    public void testInserirEntregadorComCPFExistente() throws NegocioException, PersistenciaException {
        // Crie um EntregadorDTO com CPF existente
        EntregadorDTO entregadorDTO = new EntregadorDTO();
        entregadorDTO.setCPF("CPF existente");
        // Configurar o mock para simular que já existe entregador com o CPF fornecido
        when(entregadorDAOMock.buscarPorCPF(anyString())).thenReturn(new Entregador());

        // Chamar o método inserir da classe de negócios
        entregadorNegocio.inserir(entregadorDTO);

        // Verificar se o método de persistência não foi chamado neste caso
        verify(entregadorDAOMock, never()).beginTransaction();
        verify(entregadorDAOMock, never()).incluir(any(Entregador.class));
        verify(entregadorDAOMock, never()).commitTransaction();
    }

     @Test
    public void testAlterarEntregadorComSucesso() throws NegocioException, PersistenciaException {
        // Crie um EntregadorDTO válido
        EntregadorDTO entregadorDTO = new EntregadorDTO();
        entregadorDTO.setCodigo(1);
        entregadorDTO.setNome("Nome válido");
        // Configurar o mock para simular que existe entregador com o código fornecido
        when(entregadorDAOMock.buscarPorCodigo(anyInt())).thenReturn(new Entregador());

        // Chamar o método alterar da classe de negócios
        entregadorNegocio.alterar(entregadorDTO);

        // Verificar se o método de persistência foi chamado corretamente
        verify(entregadorDAOMock, times(1)).beginTransaction();
        verify(entregadorDAOMock, times(1)).alterar(any(Entregador.class));
        verify(entregadorDAOMock, times(1)).commitTransaction();
    }

    @Test(expected = NegocioException.class)
    public void testAlterarEntregadorInexistente() throws NegocioException, PersistenciaException {
        // Crie um EntregadorDTO com um código que não existe
        EntregadorDTO entregadorDTO = new EntregadorDTO();
        entregadorDTO.setCodigo(1);
        // Configurar o mock para simular que não existe entregador com o código fornecido
        when(entregadorDAOMock.buscarPorCodigo(anyInt())).thenReturn(null);

        // Chamar o método alterar da classe de negócios
        entregadorNegocio.alterar(entregadorDTO);

        // Verificar se o método de persistência não foi chamado neste caso
        verify(entregadorDAOMock, never()).beginTransaction();
        verify(entregadorDAOMock, never()).alterar(any(Entregador.class));
        verify(entregadorDAOMock, never()).commitTransaction();
    }
}
