package com.parampara.bazaar.common;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parampara.bazaar.product.ProductRepository;

@RestController
public class DbDebugController {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;

    public DbDebugController(DataSource dataSource, JdbcTemplate jdbcTemplate, ProductRepository productRepository) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
    }

    @GetMapping("/debug/db")
    public Map<String, Object> db() throws Exception {
        Map<String, Object> out = new HashMap<>();

        out.put("jdbcUrl", dataSource.getConnection().getMetaData().getURL());
        out.put("dbUser", dataSource.getConnection().getMetaData().getUserName());

        String currentDb = jdbcTemplate.queryForObject("select current_database()", String.class);
        String currentSchema = jdbcTemplate.queryForObject("select current_schema()", String.class);

        out.put("currentDatabase", currentDb);
        out.put("currentSchema", currentSchema);

        out.put("productCountSeenByApp", productRepository.count());

        return out;
    }
}
