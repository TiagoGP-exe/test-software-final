package ifmt.cba.persistencia;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.Before;
import org.junit.Test;

import ifmt.cba.entity.Entregador;

public class EntregadorDAOTest {

    private EntityManager entityManagerMock;
    private EntregadorDAO entregadorDAO;

    @Before
    public void setUp() throws PersistenciaException {
        entityManagerMock = mock(EntityManager.class);
        entregadorDAO = new EntregadorDAO(entityManagerMock);
    }

    @Test
    public void testBuscarPorCodigoComSucesso() throws PersistenciaException {
        // Configurar o mock para simular um Entregador sendo retornado pelo EntityManager
        Entregador entregadorMock = new Entregador();
        when(entityManagerMock.find(Entregador.class, 1)).thenReturn(entregadorMock);

        // Chamar o método buscarPorCodigo da classe de persistência
        Entregador resultado = entregadorDAO.buscarPorCodigo(1);

        // Verificar se o resultado é o mesmo entregador mockado
        assertSame(entregadorMock, resultado);
    }

    @Test
    public void testBuscarPorCPFComSucesso() throws PersistenciaException {
        // Configurar o mock para simular uma Query retornando um Entregador
        Entregador entregadorMock = new Entregador();
        Query queryMock = mock(Query.class);
        when(entityManagerMock.createQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getSingleResult()).thenReturn(entregadorMock);

        // Chamar o método buscarPorCPF da classe de persistência
        Entregador resultado = entregadorDAO.buscarPorCPF("123456789");

        // Verificar se o resultado é o mesmo entregador mockado
        assertSame(entregadorMock, resultado);
    }

    @Test
    public void testBuscarPorParteNomeComSucesso() throws PersistenciaException {
        // Configurar o mock para simular uma Query retornando uma lista de Entregadores
        List<Entregador> listaEntregadoresMock = new ArrayList<>();
        Query queryMock = mock(Query.class);
        when(entityManagerMock.createQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(listaEntregadoresMock);

        // Chamar o método buscarPorParteNome da classe de persistência
        List<Entregador> resultado = entregadorDAO.buscarPorParteNome("John");

        // Verificar se o resultado é a mesma lista de Entregadores mockada
        assertSame(listaEntregadoresMock, resultado);
    }

    @Test(expected = PersistenciaException.class)
    public void testBuscarPorCodigoComErroNoEntityManager() throws PersistenciaException {
        // Configurar o mock para simular uma exceção ao buscar por código
        when(entityManagerMock.find(Entregador.class, anyInt())).thenThrow(new RuntimeException("Erro no EntityManager"));

        // Chamar o método buscarPorCodigo da classe de persistência
        entregadorDAO.buscarPorCodigo(1);

        // Verificar se a exceção foi lançada corretamente
    }

    @Test(expected = PersistenciaException.class)
    public void testBuscarPorCPFComErroNoQuery() throws PersistenciaException {
        // Configurar o mock para simular uma exceção ao executar a Query
        Query queryMock = mock(Query.class);
        when(entityManagerMock.createQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenThrow(new RuntimeException("Erro na Query"));

        // Chamar o método buscarPorCPF da classe de persistência
        entregadorDAO.buscarPorCPF("123456789");

        // Verificar se a exceção foi lançada corretamente
    }

    @Test(expected = PersistenciaException.class)
    public void testBuscarPorParteNomeComErroNoQuery() throws PersistenciaException {
        // Configurar o mock para simular uma exceção ao executar a Query
        Query queryMock = mock(Query.class);
        when(entityManagerMock.createQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenThrow(new RuntimeException("Erro na Query"));

        // Chamar o método buscarPorParteNome da classe de persistência
        entregadorDAO.buscarPorParteNome("John");

        // Verificar se a exceção foi lançada corretamente
    }
}
