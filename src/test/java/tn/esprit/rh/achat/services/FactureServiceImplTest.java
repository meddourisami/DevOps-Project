package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.repositories.FactureRepository;

public class FactureServiceImplTest {

    @InjectMocks
    private FactureServiceImpl factureService;

    @Mock
    private FactureRepository factureRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveFacture() {
        // Arrange
        Facture facture = new Facture();
        facture.setIdFacture(1L);
        facture.setMontantRemise(100.0f);
        facture.setMontantFacture(500.0f);
        facture.setDateCreationFacture(new Date());
        facture.setDateDerniereModificationFacture(new Date());
        facture.setArchivee(false);
        facture.setDetailsFacture(new HashSet<>());

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        // Act
        Facture retrievedFacture = factureService.retrieveFacture(1L);

        // Assert
        assertEquals(facture.getIdFacture(), retrievedFacture.getIdFacture());
        assertEquals(facture.getMontantRemise(), retrievedFacture.getMontantRemise());
        assertEquals(facture.getMontantFacture(), retrievedFacture.getMontantFacture());
        verify(factureRepository).findById(1L);
    }
}
