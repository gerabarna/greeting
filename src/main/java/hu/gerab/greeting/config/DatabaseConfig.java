package hu.gerab.greeting.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile("!test")  // This bean is active for all profiles except 'test', we use in-memory H2 there
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource integrationTestDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:file:./.h2/db");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
            return dataSource;
    }


    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}