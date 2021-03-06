package de.adorsys.xs2a.adapter.deutschebank;

import de.adorsys.xs2a.adapter.api.AccountInformationService;
import de.adorsys.xs2a.adapter.api.DownloadService;
import de.adorsys.xs2a.adapter.api.PaymentInitiationService;
import de.adorsys.xs2a.adapter.api.http.HttpClientConfig;
import de.adorsys.xs2a.adapter.api.http.HttpClientFactory;
import de.adorsys.xs2a.adapter.api.http.HttpLogSanitizer;
import de.adorsys.xs2a.adapter.api.model.Aspsp;
import de.adorsys.xs2a.adapter.impl.BaseDownloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

class DeutscheBankServiceProviderTest {

    private static final String URL = "https://url.com/";
    private static final String URL_WITH_PLACEHOLDER = URL + DeutscheBankServiceProvider.SERVICE_GROUP_PLACEHOLDER;

    private DeutscheBankServiceProvider serviceProvider;
    private Aspsp aspsp;

    private final HttpClientConfig clientConfig = mock(HttpClientConfig.class);
    private final HttpLogSanitizer logSanitizer = mock(HttpLogSanitizer.class);
    private final HttpClientFactory factory = mock(HttpClientFactory.class);

    ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    void setUp() {
        serviceProvider = new DeutscheBankServiceProvider();
        aspsp = spy(getAspsp());

        when(factory.getHttpClientConfig()).thenReturn(clientConfig);
        when(clientConfig.getLogSanitizer()).thenReturn(logSanitizer);
    }

    @Test
    void getAccountInformationService() {
        AccountInformationService ais = serviceProvider.getAccountInformationService(aspsp, factory, null);
        // remove after deprecated methods are purged off
        AccountInformationService deprecatedAis = serviceProvider.getAccountInformationService(aspsp, factory, null, null);

        assertInstancesOf(DeutscheBankAccountInformationService.class, Arrays.asList(ais, deprecatedAis));
        assertUrl("ais");
    }

    private void assertUrl(String service) {
        verify(aspsp, times(2)).getUrl();
        verify(aspsp, times(2)).setUrl(urlCaptor.capture());

        assertThat(urlCaptor.getValue())
            .isNotNull()
            .contains(URL + service);
    }

    private <T, V> void assertInstancesOf(Class<T> tClass, List<V> actuals) {
        if (actuals == null || actuals.size() == 0) {
            fail("no instances passed");
            return;
        }

        for (V actual : actuals) {
            assertThat(actual)
                .isNotNull()
                .isInstanceOf(tClass);
        }
    }

    @Test
    void getPaymentInitiationService() {
        PaymentInitiationService pis = serviceProvider.getPaymentInitiationService(aspsp, factory, null);
        // remove after deprecated methods are purged off
        PaymentInitiationService deprecatedPis = serviceProvider.getPaymentInitiationService(aspsp, factory, null, null);

        assertInstancesOf(DeutscheBankPaymentInitiationService.class, Arrays.asList(pis, deprecatedPis));
        assertUrl("pis");
    }

    @Test
    void getDownloadService() {
        DownloadService downloadService = serviceProvider.getDownloadService(URL, factory);
        // remove after deprecated methods are purged off
        DownloadService deprecatedDownloadService = serviceProvider.getDownloadService(URL, factory);

        assertInstancesOf(BaseDownloadService.class, Arrays.asList(downloadService, deprecatedDownloadService));
    }

    private Aspsp getAspsp() {
        Aspsp aspsp = new Aspsp();
        aspsp.setUrl(URL_WITH_PLACEHOLDER);
        return aspsp;
    }
}
