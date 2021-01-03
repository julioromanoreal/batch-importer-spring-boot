package com.julioromano.batchimporterspringboot;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SalesBatchProcessingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${application.sales-data-out-dir}")
    private String salesDataOutDir;

    @Test
    void parseFile() throws Exception {
        MvcResult result = mockMvc.perform(post("/batch-processing/SALES"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(".done.dat")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String data = FileUtils.readFileToString(new File(salesDataOutDir + "/" + content), "UTF-8");
        FileUtils.cleanDirectory(new File(salesDataOutDir));

        assertEquals(data, """
                Clientes: 2
                Vendedores: 2
                Venda mais cara: 10
                Pior vendedor: Paulo
                """);
    }

    @Test
    void invalidOperationShouldReturn400() throws Exception {
        mockMvc.perform(post("/batch-processing/INVALID_OPERATION"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
