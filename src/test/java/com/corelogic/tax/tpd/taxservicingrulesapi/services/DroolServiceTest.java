package com.corelogic.tax.tpd.taxservicingrulesapi.services;

import com.corelogic.tax.tpd.taxservicingrulesapi.data.entities.DroolEntity;
import com.corelogic.tax.tpd.taxservicingrulesapi.data.repository.DroolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class DroolServiceTest {
    @Mock
    private DroolRepository droolRepository;
    @InjectMocks
    private DroolService subject;

    @Test
    @DisplayName("Test for Drool entry in Drool Table")
    void doEntryForDroolFileTest() throws IOException {

        ReflectionTestUtils.setField(subject, "rulesPath", "src/test/resources/unittestrules");
        given(droolRepository.findByFileName("FLAGSTAR_0007A_DNP_ZERO_UNSPECIFIED.drl")).willReturn(null);
        when(droolRepository.save(Mockito.any(DroolEntity.class))).thenReturn(new DroolEntity());

        subject.doEntryForDroolFile("0030994");

        verify(droolRepository, times(1)).save(Mockito.any(DroolEntity.class));

    }

}
