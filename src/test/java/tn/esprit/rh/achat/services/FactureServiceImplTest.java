package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.rh.achat.entities.*;
import tn.esprit.rh.achat.repositories.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FactureServiceImplTest {

    @Mock
    private FactureRepository factureRepository;

    @Mock
    private OperateurRepository operateurRepository;

    @Mock
    private DetailFactureRepository detailFactureRepository;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ReglementServiceImpl reglementService;

    @InjectMocks
    private FactureServiceImpl factureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllFactures() {
        // Arrange
        List<Facture> factures = Arrays.asList(
                new Facture(1L, 0, 1000, new Date(), new Date(), false, new HashSet<>()),
                new Facture(2L, 50, 1500, new Date(), new Date(), false, new HashSet<>())
        );
        when(factureRepository.findAll()).thenReturn(factures);

        // Act
        List<Facture> result = factureService.retrieveAllFactures();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(factureRepository, times(1)).findAll();
    }

    @Test
    void testAddFacture() {
        // Arrange
        Facture facture = new Facture(1L, 0, 1000, new Date(), new Date(), false, new HashSet<>());
        when(factureRepository.save(facture)).thenReturn(facture);

        // Act
        Facture result = factureService.addFacture(facture);

        // Assert
        assertNotNull(result);
        assertEquals(facture.getIdFacture(), result.getIdFacture());
        verify(factureRepository, times(1)).save(facture);
    }

    @Test
    void testCancelFacture() {
        // Arrange
        Facture facture = new Facture(1L, 0, 1000, new Date(), new Date(), false, new HashSet<>());
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        // Act
        factureService.cancelFacture(1L);

        // Assert
        assertTrue(facture.getArchivee());
        verify(factureRepository, times(1)).save(facture);
    }

    @Test
    void testRetrieveFacture() {
        // Arrange
        Facture facture = new Facture(1L, 0, 1000, new Date(), new Date(), false, new HashSet<>());
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        // Act
        Facture result = factureService.retrieveFacture(1L);

        // Assert
        assertNotNull(result);
        assertEquals(facture.getIdFacture(), result.getIdFacture());
        verify(factureRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFacturesByFournisseur() {
        // Arrange
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        Set<Facture> factures = new HashSet<>();
        factures.add(new Facture(1L, 0, 1000, new Date(), new Date(), false, new HashSet<>()));
        fournisseur.setFactures(factures);
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        // Act
        List<Facture> result = factureService.getFacturesByFournisseur(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(fournisseurRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignOperateurToFacture() {
        // Arrange
        Facture facture = new Facture(1L, 0, 1000, new Date(), new Date(), false, new HashSet<>());
        Operateur operateur = new Operateur();
        operateur.setIdOperateur(1L);
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));
        when(operateurRepository.findById(1L)).thenReturn(Optional.of(operateur));

        // Act
        factureService.assignOperateurToFacture(1L, 1L);

        // Assert
        assertTrue(operateur.getFactures().contains(facture));
        verify(operateurRepository, times(1)).save(operateur);
    }

    @Test
    void testPourcentageRecouvrement() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();
        when(factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate)).thenReturn(1000f);
        when(reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(800f);

        // Act
        float result = factureService.pourcentageRecouvrement(startDate, endDate);

        // Assert
        assertEquals(80, result);
        verify(factureRepository, times(1)).getTotalFacturesEntreDeuxDates(startDate, endDate);
        verify(reglementService, times(1)).getChiffreAffaireEntreDeuxDate(startDate, endDate);
    }
}
