package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.entity.fee.Subscription;
import it.unisalento.music_virus_project.billing_service.domain.entity.fee.Tax;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeePeriod;
import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;
import it.unisalento.music_virus_project.billing_service.dto.fee.*;
import it.unisalento.music_virus_project.billing_service.exceptions.InsufficentBalanceException;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.ISubscriptionRepository;
import it.unisalento.music_virus_project.billing_service.repositories.ITaxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeServiceTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ITaxRepository taxRepository;

    @InjectMocks
    private FeeService feeService;

    private static Subscription subscription(String feePlanId, List<Role> roles) {
        Subscription s = new Subscription();
        s.setFeePlanId(feePlanId);
        s.setIsApplicatedTo(new ArrayList<>(roles));
        s.setAmount(new BigDecimal("9.99"));
        s.setFeePeriod(FeePeriod.MONTHLY);
        s.setActiveSince(Instant.parse("2026-01-01T00:00:00Z"));
        return s;
    }

    private static Tax tax(String feePlanId, TaxEnum name) {
        Tax t = new Tax();
        t.setFeePlanId(feePlanId);
        t.setTaxName(name);
        t.setPercentageOnTotal(new BigDecimal("10"));
        t.setActiveSince(Instant.parse("2026-01-01T00:00:00Z"));
        return t;
    }

    @Test
    void getSubscriptionList_returnsMappedList() {
        when(subscriptionRepository.findAll()).thenReturn(List.of(
                subscription("sub1", List.of(Role.ARTIST)),
                subscription("sub2", List.of(Role.FAN))
        ));

        SubscriptionListResponse res = feeService.getSubscriptionList();

        assertNotNull(res);
        assertNotNull(res.getSubscriptions());
        assertEquals(2, res.getSubscriptions().size());
        assertEquals("sub1", res.getSubscriptions().get(0).getFeePlanId());
        assertEquals("sub2", res.getSubscriptions().get(1).getFeePlanId());
    }

    @Test
    void getTaxList_returnsMappedList() {
        when(taxRepository.findAll()).thenReturn(List.of(
                tax("tax1", TaxEnum.values()[0]),
                tax("tax2", TaxEnum.values()[0])
        ));

        TaxListResponseDTO res = feeService.getTaxList();

        assertNotNull(res);
        assertNotNull(res.getTaxes());
        assertEquals(2, res.getTaxes().size());
        assertEquals("tax1", res.getTaxes().get(0).getFeePlanId());
        assertEquals("tax2", res.getTaxes().get(1).getFeePlanId());
    }

    @Test
    void getArtistSubscription_filtersByRole() {
        when(subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.ARTIST))
                .thenReturn(List.of(subscription("subA", List.of(Role.ARTIST))));

        SubscriptionListResponse res = feeService.getArtistSubscription();

        assertEquals(1, res.getSubscriptions().size());
        assertEquals("subA", res.getSubscriptions().get(0).getFeePlanId());
        assertTrue(res.getSubscriptions().get(0).getIsApplicatedTo().contains(Role.ARTIST));
    }

    @Test
    void getVenuesSubscription_filtersByRole() {
        when(subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.VENUE))
                .thenReturn(List.of(subscription("subV", List.of(Role.VENUE))));

        SubscriptionListResponse res = feeService.getVenuesSubscription();

        assertEquals(1, res.getSubscriptions().size());
        assertEquals("subV", res.getSubscriptions().get(0).getFeePlanId());
        assertTrue(res.getSubscriptions().get(0).getIsApplicatedTo().contains(Role.VENUE));
    }

    @Test
    void getFansSubscription_filtersByRole() {
        when(subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.FAN))
                .thenReturn(List.of(subscription("subF", List.of(Role.FAN))));

        SubscriptionListResponse res = feeService.getFansSubscription();

        assertEquals(1, res.getSubscriptions().size());
        assertEquals("subF", res.getSubscriptions().get(0).getFeePlanId());
        assertTrue(res.getSubscriptions().get(0).getIsApplicatedTo().contains(Role.FAN));
    }

    @Test
    void createSubscription_whenAlreadyExists_throws() {
        SubscriptionCreateRequestDTO dto = new SubscriptionCreateRequestDTO();
        dto.setIsApplicatedTo(List.of(Role.ARTIST));
        dto.setAmount(new BigDecimal("9.99"));
        dto.setFeePeriod(FeePeriod.MONTHLY);
        dto.setActiveSince(Instant.now());

        when(subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.ARTIST))
                .thenReturn(List.of(subscription("existing", List.of(Role.ARTIST))));

        assertThrows(InsufficentBalanceException.class, () -> feeService.createSubscription(dto));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void createSubscription_whenOk_savesAndReturnsDto() {
        SubscriptionCreateRequestDTO dto = new SubscriptionCreateRequestDTO();
        dto.setIsApplicatedTo(List.of(Role.ARTIST));
        dto.setAmount(new BigDecimal("9.99"));
        dto.setFeePeriod(FeePeriod.MONTHLY);
        dto.setActiveSince(Instant.parse("2026-02-01T00:00:00Z"));

        when(subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.ARTIST))
                .thenReturn(List.of());

        when(subscriptionRepository.save(any(Subscription.class)))
                .thenAnswer(inv -> {
                    Subscription s = inv.getArgument(0);
                    s.setFeePlanId("subNEW");
                    return s;
                });

        SubscriptionResponseDTO res = feeService.createSubscription(dto);

        assertNotNull(res);
        assertEquals("subNEW", res.getFeePlanId());
        assertEquals(FeePeriod.MONTHLY, res.getFeePeriod());
        assertEquals(new BigDecimal("9.99"), res.getAmount());
        assertTrue(res.getIsApplicatedTo().contains(Role.ARTIST));
    }

    @Test
    void updateSubscription_whenNotFound_throws() {
        when(subscriptionRepository.findSubscriptionByFeePlanId("missing")).thenReturn(null);

        SubscriptionUpdateRequestDTO dto = new SubscriptionUpdateRequestDTO();
        dto.setAmount(new BigDecimal("12.00"));

        assertThrows(NotFoundException.class, () -> feeService.updateSubscription("missing", dto));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void updateSubscription_whenOk_updatesFields() {
        Subscription existing = subscription("sub1", List.of(Role.ARTIST));
        when(subscriptionRepository.findSubscriptionByFeePlanId("sub1")).thenReturn(existing);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        SubscriptionUpdateRequestDTO dto = new SubscriptionUpdateRequestDTO();
        dto.setAmount(new BigDecimal("15.00"));
        dto.setFeePeriod(FeePeriod.MONTHLY);
        dto.setIsApplicatedTo(List.of(Role.VENUE));

        SubscriptionResponseDTO res = feeService.updateSubscription("sub1", dto);

        assertEquals("sub1", res.getFeePlanId());
        assertEquals(new BigDecimal("15.00"), res.getAmount());
        assertTrue(res.getIsApplicatedTo().contains(Role.VENUE));
    }

    @Test
    void deleteSubscription_whenNotFound_throws() {
        when(subscriptionRepository.findSubscriptionByFeePlanId("missing")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> feeService.deleteSubscription("missing"));
        verify(subscriptionRepository, never()).delete(any());
    }

    @Test
    void deleteSubscription_whenOk_deletesAndReturnsDto() {
        Subscription existing = subscription("sub1", List.of(Role.ARTIST));
        when(subscriptionRepository.findSubscriptionByFeePlanId("sub1")).thenReturn(existing);

        SubscriptionResponseDTO res = feeService.deleteSubscription("sub1");

        verify(subscriptionRepository).delete(existing);
        assertEquals("sub1", res.getFeePlanId());
    }

    @Test
    void createTax_whenOk_savesAndReturnsDto() {
        TaxEnum anyTax = TaxEnum.values()[0];

        TaxCreateRequestDTO dto = new TaxCreateRequestDTO();
        dto.setTaxName(anyTax);
        dto.setPercentageOnTotal(new BigDecimal("10"));
        dto.setActiveSince(Instant.parse("2026-02-01T00:00:00Z"));

        when(taxRepository.save(any(Tax.class)))
                .thenAnswer(inv -> {
                    Tax t = inv.getArgument(0);
                    t.setFeePlanId("taxNEW");
                    return t;
                });

        TaxResponseDTO res = feeService.createTax(dto);

        assertNotNull(res);
        assertEquals("taxNEW", res.getFeePlanId());
        assertEquals(anyTax.toString(), res.getTaxName());
        assertEquals(new BigDecimal("10"), res.getPercentageOnTotal());
    }

    @Test
    void updateTax_whenNotFound_throws() {
        when(taxRepository.findTaxByFeePlanId("missing")).thenReturn(null);

        TaxUpdateRequestDTO dto = new TaxUpdateRequestDTO();
        dto.setPercentageOnTotal(new BigDecimal("12"));

        assertThrows(NotFoundException.class, () -> feeService.updateTax("missing", dto));
        verify(taxRepository, never()).save(any());
    }

    @Test
    void updateTax_whenNameDuplicate_throws() {
        TaxEnum anyTax = TaxEnum.values()[0];
        TaxEnum otherTax = TaxEnum.values().length > 1 ? TaxEnum.values()[1] : anyTax;

        Tax current = tax("tax1", anyTax);
        when(taxRepository.findTaxByFeePlanId("tax1")).thenReturn(current);

        Tax existingOther = tax("tax2", otherTax);
        when(taxRepository.findTaxByTaxName(otherTax)).thenReturn(existingOther);

        TaxUpdateRequestDTO dto = new TaxUpdateRequestDTO();
        dto.setTaxName(otherTax);

        assertThrows(InsufficentBalanceException.class, () -> feeService.updateTax("tax1", dto));
        verify(taxRepository, never()).save(any());
    }

    @Test
    void updateTax_whenOk_updatesPercentageAndName() {
        TaxEnum anyTax = TaxEnum.values()[0];
        TaxEnum otherTax = TaxEnum.values().length > 1 ? TaxEnum.values()[1] : anyTax;

        Tax current = tax("tax1", anyTax);
        when(taxRepository.findTaxByFeePlanId("tax1")).thenReturn(current);
        when(taxRepository.findTaxByTaxName(otherTax)).thenReturn(null);
        when(taxRepository.save(any(Tax.class))).thenAnswer(inv -> inv.getArgument(0));

        TaxUpdateRequestDTO dto = new TaxUpdateRequestDTO();
        dto.setPercentageOnTotal(new BigDecimal("20"));
        dto.setTaxName(otherTax);

        TaxResponseDTO res = feeService.updateTax("tax1", dto);

        assertEquals("tax1", res.getFeePlanId());
        assertEquals(new BigDecimal("20"), res.getPercentageOnTotal());
        assertEquals(otherTax.toString(), res.getTaxName());
    }

    @Test
    void deleteTax_whenNotFound_throws() {
        when(taxRepository.findTaxByFeePlanId("missing")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> feeService.deleteTax("missing"));
        verify(taxRepository, never()).delete(any());
    }

    @Test
    void deleteTax_whenOk_deletesAndReturnsDto() {
        TaxEnum anyTax = TaxEnum.values()[0];

        Tax current = tax("tax1", anyTax);
        when(taxRepository.findTaxByFeePlanId("tax1")).thenReturn(current);

        TaxResponseDTO res = feeService.deleteTax("tax1");

        verify(taxRepository).delete(current);
        assertEquals("tax1", res.getFeePlanId());
        assertEquals(anyTax.toString(), res.getTaxName());
    }
}
