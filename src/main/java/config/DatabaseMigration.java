package config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseMigration {


    public static void migrate(){
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/teamfinder",
                    "postgres",
                    "123"
            );

            Database database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");

            System.out.println("MIGRATED");

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
